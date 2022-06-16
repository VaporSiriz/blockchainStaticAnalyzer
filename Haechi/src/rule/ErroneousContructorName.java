package rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Stack;
import java.lang.reflect.Method;

import context.ExpressionContext;
import context.FunctionCallContext;
import context.FunctionDefinitionContext;
import node.AST;
import node.Expression;
import node.ContractDefinition;
import node.FunctionCall;
import node.FunctionDefinition;

import util.Position;
import util.ValidationRule;

public class ErroneousContructorName implements ValidationRule{
    List<String> characterCounts = new ArrayList<String>();

    @Override
    public boolean isImplement() {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void analyze() {
        if(!characterCounts.isEmpty()) {
            characterCounts.clear();
        }
		/*
		    0.4.22 버전까지만 적용되던 이슈
			1. 소문자로 변경하였을 때 Contract의 이름과 같은 이름의 함수가 존재하는지 체크.
			2. 원본의 알파벳으로 비교 했을 때 다르면 문제.
		 */
        FunctionDefinitionContext functionDefinitionContext = new FunctionDefinitionContext();
        List<FunctionDefinition> functionDefinitionList = functionDefinitionContext.getAllFunctionDefinitions();
        for(FunctionDefinition functionDefinition : functionDefinitionList) {
            ContractDefinition cd = (ContractDefinition) functionDefinition.getParentOfNodeType("ContractDefinition");
            if(cd.getName().toLowerCase().equals(functionDefinition.getName().toLowerCase()) &&
               !cd.getName().equals(functionDefinition.getName())) {
                characterCounts.add(functionDefinition.getSrc().split(":")[0]);
            }
        }
    }

    @Override
    public Criticity getRuleCriticity() {
        return Criticity.MINOR;
    }

    @Override
    public String getRuleName() {
        return "ErroneousContructorName";
    }

    @Override
    public String getComment() {
        return "Potential vulnerability to Erroneous contract name";
    }

    @Override
    public List<String> getCharacterCounts() {
        return characterCounts;
    }
}
