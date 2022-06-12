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

public class Reentrancy implements ValidationRule{
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
		...
		check the weakness
		...
		*/
		/*
			1. msg.sender.call이 속한 함수가 존재.
			2. 해당 함수를 call하는 contract가 존재.
			3. 해당 contract의 fallback에서 해당 함수를 재호출.

		 */
		ExpressionContext expressionContext = new ExpressionContext();
		List<Expression> expressions = expressionContext.getAllExpressions();
		HashMap<String, String> fallbacks = new HashMap<String, String>();
		
		// 미리 fallback 함수를 모두 찾아놓음
		FunctionDefinitionContext functionDefinitionContext = new FunctionDefinitionContext();
		List<FunctionDefinition> functionDefinitions = functionDefinitionContext.getAllFunctionDefinitions();
		for(FunctionDefinition functionDefinition : functionDefinitions) {
			if((boolean) functionDefinition.getPayable() == true) {
				// 0.6 이전 버전에는 Name이 None인 것임.
				if(functionDefinition.getName().equals("None")) {
					ContractDefinition cd = (ContractDefinition) functionDefinition.getParentOfNodeType("ContractDefinition");
					fallbacks.put(cd.getName(), functionDefinition.getSrc());
				}
			}
		}

		// 함수가 call되는 fallback 함수를 미리 다 찾아 놓음.
		// HashMap<call되는 함수 , HashMap<함수가 속한 contract, HashMap<함수가 호출되는 함수, 위치>>>
		HashMap<String, HashMap<String,  String>> functionCallMapFromFallBack = new HashMap<String, HashMap<String,  String>>();
		FunctionCallContext functionCallContext = new FunctionCallContext();
		List<FunctionCall> functionCalls = functionCallContext.getAllFunctionCalls();

		for(FunctionCall functionCall : functionCalls) {
			String functionName = functionCall.getName();
			String functionMemberName = functionCall.getMemberName();
			FunctionDefinition fd = (FunctionDefinition) functionCall.getParentOfNodeType("FunctionDefinition");
			ContractDefinition cd = (ContractDefinition) functionCall.getParentOfNodeType("ContractDefinition");
			if(functionName!=null && !functionMemberName.equals("None")) {
				// fd의 이름이 None 이면 Fallback 함수이다.
				if(fd.getName().equals("None")) {
					HashMap<String, String> whichContract = new HashMap<String, String>();
					whichContract.put(cd.getName(), functionCall.getSrc());
					functionCallMapFromFallBack.put(functionCall.getMemberName(), whichContract);
				}
			}
		}

		for(Expression expression : expressions) {
			String nodeType = expression.getNodeType();
			String name = expression.getName();
			String memberName = expression.getMemberName();
			if(nodeType.equals("MemberAccess")) {
				if(memberName.equals("call") || memberName.equals("transfer") || memberName.equals("send")) {
					List<AST> children = expression.getChildren();
					// call, transfer, send가 호출된 경우 msg.(sender or address)
					Expression child = (Expression)children.get(0);
					String childTypeString = (String) child.getTypeDescriptions().get("typeString");
					// 호출된 쪽은 address여야 함.
					if(childTypeString.equals("address")) {
						// 이제 호출될 수 있는 Contract의 payable을 살펴봐야 함.
						// 0.6 이전의 버전에서는 fallback 함수는 무기명 함수이지만 0.6 이후부터는 receive를 통해 받거나
						// fallback() 함수여야 이더를 받을 수 있음.
						// 여기서는 일단은 0.6 으로 처리 => 무기명 함수라 이름이 없음.
						// 또한 어떤 이름으로 호출된 함수에 포함된 expression 인지 알아야 함.
						AST parent = expression.getParentOfNodeType("FunctionDefinition");
						FunctionDefinition fd = (FunctionDefinition) parent;
						if(functionCallMapFromFallBack.containsKey(fd.getName())) {
							HashMap<String, String> thisFunctionCallMap = functionCallMapFromFallBack.get(fd.getName());
							Set<String> keys = thisFunctionCallMap.keySet();
							characterCounts.add(expression.getSrc().split(":")[0]);
							for (String key : keys) {
								String value = thisFunctionCallMap.get(key);
								characterCounts.add(value.split(":")[0]);
							}
						}
					}
				}
			} else {
			}
		}
	}
    
	@Override
	public Criticity getRuleCriticity() {
	    return Criticity.MAJOR;
	}

	@Override
	public String getRuleName() {
	    return "Reentrancy";
	}

	@Override
	public String getComment() {
	    return "Potential vulnerability to Reentrancy attack";
	}
    
	@Override
	public List<String> getCharacterCounts() {
		return characterCounts;
	}
}
