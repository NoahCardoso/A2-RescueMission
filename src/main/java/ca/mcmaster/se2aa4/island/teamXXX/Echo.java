package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Echo extends Action
{
	private JSONObject echo;
	public Echo(Direction direction){
		String dir = direction.toString();
		this.echo = new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", dir));
	}
	public JSONObject getJSON(){
		return this.echo;
	}
		
	
}