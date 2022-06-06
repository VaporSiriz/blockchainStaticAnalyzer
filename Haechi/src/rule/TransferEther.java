package rule;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import context.FunctionCallContext;
import node.FunctionCall;
import util.ValidationRule;

public class TransferEther implements ValidationRule{
	List<String> characterCounts = new ArrayList<String>();

	@Override
	public boolean isImplement() {
		return false;
	}

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
	    return "Transfer-Ether";
	}

	@Override
	public String getComment() {
	    return "Incorrect function usage in Ether transmission, Use transfer() instead";
	}
    
	@Override
	public List<String> getCharacterCounts() {
		return characterCounts;
	}
}
