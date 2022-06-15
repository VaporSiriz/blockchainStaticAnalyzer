package node;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
/*
{
			"id" : 1,
			"literals" :
			[
				"solidity",
				"^",
				"0.4",
				".25"
			],
			"nodeType" : "PragmaDirective",
			"src" : "0:23:0"
		}
 */
public class PragmaDirective extends AST{
    // registry가 한 파일에 하나이므로
    public static PragmaDirective registry = new PragmaDirective();
    Object documentation; // uncertainty
    Object id;
    JSONArray literals;
    String src;

    public PragmaDirective() {

    }

    public PragmaDirective(JSONObject node) {
        nodeType = "PragmaDirective";
        registry = this;

        try {
            children = new ArrayList<AST>();
            documentation = (Object) node.get("documentation");
            id = (Object) node.get("id");
            literals = (JSONArray) node.get("literals");
            src = (String) node.get("src");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AST getParent() {
        return parent;
    }

    @Override
    public List<AST> getChildren() {
        return children;
    }

    @Override
    public String getNodeType() {
        return nodeType;
    }

    public String getCharacterCount() {
        return src.split(":")[0];
    }

    public static PragmaDirective getRegistry() {
        return registry;
    }

    public Object getDocumentation() {
        return documentation;
    }

    public Object getId() {
        return id;
    }

    public JSONArray getLiterals() {
        return literals;
    }

    public String getSrc() {
        return src;
    }

}
