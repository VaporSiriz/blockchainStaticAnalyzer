package rule;

import java.util.ArrayList;
import java.util.List;

import context.ExpressionContext;
import node.ContractDefinition;
import node.Expression;
import util.ValidationRule;

public class Overflow implements ValidationRule{
	List<String> characterCounts = new ArrayList<String>();

	@Override
	public boolean isImplement() {
		return true;
	}

	@Override
	public void analyze() {
		characterCounts.clear();
		// Library가 아닌 곳에서 +, +=, ++ 연산을 하는 경우
		ExpressionContext expressionContext = new ExpressionContext();
		List<Expression> expressions = expressionContext.getAllOperations();
		for(Expression expression : expressions) {
			ContractDefinition cd = (ContractDefinition) expression.getParentOfNodeType("ContractDefinition");
			if(cd != null && cd.getContractKind().equals("contract")) {
				String operator = expression.getOperator();
				if(operator.equals("+=") || operator.equals("+") || operator.equals("++")) {
					characterCounts.add(expression.getSrc().split(":")[0]);
				}
			}
		}
	}

	@Override
	public Criticity getRuleCriticity() {
	    return Criticity.MAJOR;
	}

	@Override
	public String getRuleName() {
	    return "Overflow";
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
