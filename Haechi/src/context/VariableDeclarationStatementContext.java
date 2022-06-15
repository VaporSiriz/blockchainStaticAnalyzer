package context;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import node.VariableDeclarationStatement;
import util.Position;

public class VariableDeclarationStatementContext {
    public List<VariableDeclarationStatement> getAllVariableDeclarationStatementContext() {
        return VariableDeclarationStatement.getRegistry();
    }
}
