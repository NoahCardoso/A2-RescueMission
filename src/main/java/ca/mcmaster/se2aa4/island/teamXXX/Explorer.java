package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();

    private Drone drone;
    private JSONObject result = null;

    private POIProcessor processor = new POIProcessor();

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        
        Integer batteryLevel = info.getInt("budget");
        this.drone = new Drone(batteryLevel, Direction.EAST);
        
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        
        JSONObject decision = drone.getNextMove();
        

        return decision.toString();
        
        
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        this.result = response;
        
        Integer cost = response.getInt("cost");
        drone.setBattery(drone.getBattery()-cost);
      
        JSONObject extraInfo = response.getJSONObject("extras");
        drone.updateResults(extraInfo);

        logData(extraInfo);
    }

    private void logData(JSONObject data) {
        if (data.has("creeks")) {
            if (data.getJSONArray("creeks").length() >= 1) {
                processor.addCreek(data.getJSONArray("creeks").getString(0), drone.getX(), drone.getY());
            }
        }
        if (data.has("sites")) {
            if (data.getJSONArray("sites").length() >= 1) {
                processor.addEmergencySite(data.getJSONArray("sites").getString(0), drone.getX(), drone.getY());
            }
        }
        
    }

    @Override
    public String deliverFinalReport() {
        logger.info("** Closest POI: {}", processor.getClosestPOI());
        return "";
    }

}
