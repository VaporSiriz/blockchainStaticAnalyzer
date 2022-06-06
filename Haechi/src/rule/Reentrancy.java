package rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.lang.reflect.Method;

import context.FunctionCallContext;
import node.AST;
import node.FunctionCall;
import util.ValidationRule;

public class Reentrancy implements ValidationRule{
	List<String> characterCounts = new ArrayList<String>();

	@Override
	public boolean isImplement() {
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void analyze() {
		characterCounts.clear();
		/*
		...
		check the weakness
		...
		*/
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
