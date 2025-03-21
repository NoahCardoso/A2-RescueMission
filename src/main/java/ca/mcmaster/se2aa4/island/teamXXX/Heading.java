package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Heading extends Action
{
	private JSONObject heading;
	private Direction dir;
	
	public Heading(Direction direction){
		this.dir = direction;
		this.heading = new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", direction.toString()));
	}
	public JSONObject getJSON(){
		return this.heading;
	}
	public Direction getDirection(){
		return this.dir;
	}
	
}