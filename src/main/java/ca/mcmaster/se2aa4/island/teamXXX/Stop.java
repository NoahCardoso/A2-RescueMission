package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Stop extends Action
{
	private JSONObject stop;
	public Stop(){
		this.stop = new JSONObject().put("action", "stop");
	}
	public JSONObject getJSON(){
		return this.stop;
	}
		
	
}