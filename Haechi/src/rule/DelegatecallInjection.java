package rule;

import context.ExpressionContext;
import node.ContractDefinition;
import node.Expression;
import org.json.simple.JSONObject;
import util.ValidationRule;

import java.util.ArrayList;
import java.util.List;

public class DelegatecallInjection implements ValidationRule{
    List<String> characterCounts = new ArrayList<String>();

    @Override
    public boolean isImplement() {
        return true;
    }

    @Override
    public void analyze() {
        characterCounts.clear();
        // stateless하지 않은 변수를 이용하여 delegatecall을 사용할 경우
        // delegatecall을 호출하는 주소가 초기화 되는지를 살펴보면 됨.
        // 여기서는 library로 호출했을 때만 체크. stateless한 경우를 이 경우가 아니고서는 만들지 못함.
        ExpressionContext expressionContext = new ExpressionContext();
        List<Expression> expressions = expressionContext.getAllDelegateCalls();
        for(Expression expression : expressions) {
             ContractDefinition cd = (ContractDefinition) expression.getParentOfNodeType("ContractDefinition");
             if(!cd.getContractKind().equals("library")) {
                 characterCounts.add(expression.getSrc().split(":")[0]);
             }
        }
    }

    @Override
    public ValidationRule.Criticity getRuleCriticity() {
        return ValidationRule.Criticity.MAJOR;
    }

    @Override
    public String getRuleName() {
        return "DelegatecallInjection";
    }

    @Override
    public String getComment() {
        return "not stateless delegatecall called. ";
    }

    @Override
    public List<String> getCharacterCounts() {
        return characterCounts;
    }
}
