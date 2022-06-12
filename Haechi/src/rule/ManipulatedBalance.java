package rule;

import context.ExpressionContext;
import context.FunctionCallContext;
import node.AST;
import node.Expression;
import node.FunctionCall;
import org.json.simple.JSONObject;
import util.Position;
import util.ValidationRule;

import java.util.ArrayList;
import java.util.List;

public class ManipulatedBalance implements ValidationRule{
	List<String> characterCounts = new ArrayList<String>();
	
	@Override
	public boolean isImplement() {
		return true;
	}
	
	@Override
	public void analyze() {
		if(!characterCounts.isEmpty()) {
			characterCounts.clear();
		}
		Position position = new Position();
		// Balance를 사용하고 있으면 에러
		ExpressionContext expressionContext = new ExpressionContext();
		List<Expression> expressions = expressionContext.getAllExpressions();
		for(Expression expression : expressions) {
			String memberName = expression.getMemberName();
			if(memberName != null && memberName.equals("balance")) {
				characterCounts.add(expression.getSrc().split(":")[0]);
			}
		}
	}

	@Override
	public Criticity getRuleCriticity() {
	    return Criticity.MAJOR;
	}

	@Override
	public String getRuleName() {
	    return "ManipulatedBalance";
	}

	@Override
	public String getComment() {
	    return "Potential vulnerability to ManipulatedBalance";
	}
    
	@Override
	public List<String> getCharacterCounts() {
		return characterCounts;
	}
}
