package rule;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import context.FunctionCallContext;
import node.AST;
import node.FunctionCall;
import util.ValidationRule;

public class FrozenEther implements ValidationRule{
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

        /*
            1. Contract 에 붙은 function을 찾는다.
            2. 해당 function에 payable receive 혹은 fallback 함수가 있는지 찾는다.
            3. call, send, transfer 가 있는지를 확인한다.

         */

        FunctionCallContext functionCallContext = new FunctionCallContext();
        List<FunctionCall> functionCalls = functionCallContext.getAllFunctionCalls();

        for(FunctionCall functionCall : functionCalls) {
            // Check whether transfer function is used in loop statement .
            AST parent = null;
            String parentName = null;

            parent = functionCall.getParent();
            parentName = parent.getClass().getSimpleName();

            while(!parentName.equals("FunctionDefinition") && !parentName.equals("ModifierDefinition")) {
                parent = parent.getParent();
                parentName = parent.getClass().getSimpleName();

                if((parentName.equals("WhileStatement") ||
                        parentName.equals("ForStatement") ||
                        parentName.equals("DoWhileStatement"))) {

                    JSONObject expression = functionCall.getExpression();
                    try {
                        if(expression.get("memberName").equals("transfer")) {
                            expression = (JSONObject) expression.get("expression");
                            expression = (JSONObject) expression.get("expression");
                            if(expression.get("name").equals("msg") || expression.get("name").equals("tx")){
                                String count = (String) expression.get("src");
                                count = count.split(":")[0];
                                characterCounts.add(count);
                            }
                        }
                    } catch(NullPointerException e) {
//						e.printStackTrace();
                    }

                    break;
                }
            }
        }
    }

    @Override
    public Criticity getRuleCriticity() {
        return Criticity.CRITICAL;
    }

    @Override
    public String getRuleName() {
        return "DoSAttack";
    }

    @Override
    public String getComment() {
        return "Potential vulnerability to DoS attack";
    }

    @Override
    public List<String> getCharacterCounts() {
        return characterCounts;
    }
}
