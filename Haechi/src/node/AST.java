package node;

import java.util.List;

public class AST {
	AST parent = null;
	List<AST> children;
	String nodeType;
	
	public AST getParent() {
		return parent;
	}
	
	public List<AST> getChildren() {
		return children;
	}
	
	public String getNodeType() {
		return nodeType;
	}

	public AST getParentOfNodeType(String nodeType) {
		AST temp_parent = parent;
		String parentNodeType = temp_parent.getNodeType();
		while(!parentNodeType.equals(nodeType)) {
			temp_parent = temp_parent.getParent();
			parentNodeType = temp_parent.getNodeType();
			if(parentNodeType==null) return null;
		}
		return temp_parent;
	}
}

