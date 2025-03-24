package ca.mcmaster.se2aa4.island.team013;

import org.json.JSONObject;

public interface Command
{
	JSONObject execute(Drone drone);
}