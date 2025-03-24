package ca.mcmaster.se2aa4.island.team013;

import org.json.JSONObject;

public class Stop implements Command
{
	
	public Stop(){}

	@Override
	public JSONObject execute(Drone drone){
		return new JSONObject().put("action", "stop");
	}
		
	
}