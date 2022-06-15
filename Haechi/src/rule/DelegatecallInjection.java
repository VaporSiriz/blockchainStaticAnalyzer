package rule;

import context.ExpressionContext;
import node.ContractDefinition;
import node.Expression;
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
        ExpressionContext expressionContext = new ExpressionContext();
        List<Expression> expressions = expressionContext.getAllDelegateCalls();

        System.out.println("-----delegatecall------");
        for(Expression expression : expressions) {
                System.out.println(expression);
        }
        System.out.println("-----delegatecall------");
    }

    @Override
    public ValidationRule.Criticity getRuleCriticity() {
        return ValidationRule.Criticity.MAJOR;
    }

    @Override
    public String getRuleName() {
        return "Underflow";
    }

    @Override
    public String getComment() {
        return "Note the operation of integer variables";
    }

    @Override
    public List<String> getCharacterCounts() {
        return characterCounts;
    }
}
