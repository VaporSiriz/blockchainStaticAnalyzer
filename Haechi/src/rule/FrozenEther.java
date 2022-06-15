package rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import context.ExpressionContext;
import context.FunctionDefinitionContext;
import node.*;
import org.json.simple.JSONObject;

import context.FunctionCallContext;
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
            0.6 이전 버전으로 체크
            1. Contract 에 붙은 fallback 함수가 존재하는지 확인한다.
            2. 존재한다면 해당 contract에 call, transfer, send가 존재하는지 확인한다.
         */

        ExpressionContext expressionContext = new ExpressionContext();
        List<Expression> expressions = expressionContext.getAllExpressions();
        HashMap<String, String> fallbacks = new HashMap<String, String>();

        // payable 함수를 미리 다 찾아 놓음.
        // 이후 해당 contract에 call, transfer, send 가 있는지를 확인.
        // HashMap<contract, HashMap<payable function, src>>
        HashMap<String, ArrayList<HashMap<String,  String>>> payableFunctionMap = new HashMap<String, ArrayList<HashMap<String,  String>>>();
        FunctionDefinitionContext functionDefinitionContext = new FunctionDefinitionContext();
        List<FunctionDefinition> functionDefinitionList = functionDefinitionContext.getAllFunctionDefinitions();
        HashMap<String, Integer> sendEtherCount = new HashMap<String, Integer>();

        for(FunctionDefinition functionDefinition : functionDefinitionList) {
            if(functionDefinition.getStateMutability().equals("payable"))  {
                ContractDefinition cd = (ContractDefinition) functionDefinition.getParentOfNodeType("ContractDefinition");
                if(!payableFunctionMap.containsKey(cd.getName())) {
                    ArrayList<HashMap<String, String>> payableFunctionList = new ArrayList<HashMap<String, String>>();
                    payableFunctionMap.put(cd.getName(), payableFunctionList);
                    sendEtherCount.put(cd.getName(), 0);
                }
                ArrayList<HashMap<String, String>> payableFunctionList = payableFunctionMap.get(cd.getName());
                HashMap<String, String> fdMap = new HashMap<String, String>();
                fdMap.put(functionDefinition.getName(), functionDefinition.getSrc());
                payableFunctionList.add(fdMap);
                payableFunctionMap.put(cd.getName(), payableFunctionList);
            }
        }

        for(Expression expression : expressions) {
            String nodeType = expression.getNodeType();
            String memberName = expression.getMemberName();
            if(nodeType.equals("MemberAccess")) {
                if(memberName.equals("call") || memberName.equals("transfer") || memberName.equals("send")) {
                    ContractDefinition cd = (ContractDefinition) expression.getParentOfNodeType("ContractDefinition");
                    if(payableFunctionMap.containsKey(cd.getName())) {
                        sendEtherCount.put(cd.getName(), sendEtherCount.get(cd.getName())+1);
                    }
                }
            }
        }

        for(String key : sendEtherCount.keySet()) {
            if(sendEtherCount.get(key) == 0) {
                ArrayList<HashMap<String,  String>> payableFunctions = payableFunctionMap.get(key);
                for(HashMap<String,  String> payableFunction : payableFunctions) {
                    for(String value : payableFunction.values()) {
                        characterCounts.add(value.split(":")[0]);
                    }
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
        return "FrozenEther";
    }

    @Override
    public String getComment() {
        return "Potential vulnerability to Frozen Ether";
    }

    @Override
    public List<String> getCharacterCounts() {
        return characterCounts;
    }
}
