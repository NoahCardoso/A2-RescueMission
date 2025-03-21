package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Heading extends Action
{
	private JSONObject heading;
	public Heading(Direction direction){
		String dir = direction.toString();
		this.heading = new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", dir));
	}
	public JSONObject getJSON(){
		return this.heading;
	}
		
	
}