package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Fly extends Action
{
	private JSONObject fly;
	public Fly(){
		this.fly = new JSONObject().put("action","fly");
	}
	public JSONObject getJSON(){
		return this.fly;
	}
	
}