package ca.mcmaster.se2aa4.island.team013;

import org.json.JSONObject;

public class Fly implements Command
{
	
	public Fly(){}

	@Override
	public JSONObject execute(Drone drone){
		drone.fly();
		return new JSONObject().put("action","fly");
	}
	
}