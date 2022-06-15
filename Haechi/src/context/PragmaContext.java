package context;

import node.PragmaDirective;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.Position;

import java.util.List;

public class PragmaContext {
    public PragmaDirective getPragmaDirective() {
        return PragmaDirective.getRegistry();
    }

    public JSONObject getPragmaInfo() {

        Position position = new Position();
        PragmaDirective allContractDefinition = getPragmaDirective();
        JSONObject pragmaInfo = new JSONObject();

        JSONArray literals = allContractDefinition.getLiterals();
        String version = "";
        for(Object literal : literals) {
            version += (String)literal;
        }
        pragmaInfo.put("version", version);

        return pragmaInfo;
    }
}
