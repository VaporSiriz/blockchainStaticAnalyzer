package rule;

import context.*;
import node.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.Position;
import util.ValidationRule;

import java.util.ArrayList;
import java.util.List;

public class ShortAddress implements ValidationRule{
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
        // 함수가 arguments로 address를 갖는다면 msg.data의 길이를 확인하도록 권고
        FunctionDefinitionContext functionDefinitionContext = new FunctionDefinitionContext();
        List<FunctionDefinition> functionDefinitions = functionDefinitionContext.getAllFunctionDefinitions();
        for(FunctionDefinition functionDefinition : functionDefinitions) {
            JSONArray parameters = (JSONArray) functionDefinition.getParameters().get("parameters");
            if(!parameters.isEmpty()) {
                // System.out.println(parameters);
                for(int i=0; i<parameters.size();i++) {
                    JSONObject parameter = (JSONObject) parameters.get(i);
                    JSONObject typeDescriptions = (JSONObject) parameter.get("typeDescriptions");
                    String typeString = (String) typeDescriptions.get("typeString");
                    if(typeString.equals("address")) {
                        characterCounts.add(functionDefinition.getSrc().split(":")[0]);
                        break;
                    }
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
        return "ShortAddress";
    }

    @Override
    public String getComment() {
        return "Potential vulnerability to Short Address. check msg.data.length. ";
    }

    @Override
    public List<String> getCharacterCounts() {
        return characterCounts;
    }
}
