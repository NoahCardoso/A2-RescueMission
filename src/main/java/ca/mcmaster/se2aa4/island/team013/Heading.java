package ca.mcmaster.se2aa4.island.team013;

import org.json.JSONObject;

public class Heading implements Command
{
	private final Direction heading;

	public Heading(Direction direction){
		this.heading = direction;
	}

	@Override
	public JSONObject execute(Drone drone){
		drone.fly();
		drone.setDir(heading);
		drone.fly();
		return new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", this.heading.toString()));
	}
		
	
}