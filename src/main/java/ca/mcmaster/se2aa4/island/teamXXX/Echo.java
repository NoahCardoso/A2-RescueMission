package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Echo implements Command
{
	private final Direction direction;

	public Echo(Direction direction){
		this.direction = direction;
		
	}

	@Override
	public JSONObject execute(Drone drone){
		
		return (new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", this.direction.toString())));
	}
		
	
}