package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Scan extends Action
{
	private JSONObject scan;
	public Scan(){
		this.scan = new JSONObject().put("action","scan");
	}
	public JSONObject getJSON(){
		return this.scan;
	}
		
	
}