package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private Drone drone;
    private JSONObject result = null;
    private String direction = null;
    private int x = 1;
    private int y = 1;
    private int limitX = 0;
    private int limitY = 0;
    private int dstX = 0;
    private int dstY = 0;
    private char dir = 'E';
    private int n = 0;
    private boolean echo = true;
    private String[] actions = {"scan", "echo", "fly", "heading"};
    private int action = -1;
    private boolean foundE = false;
    private boolean foundC = false;
    private boolean overOcean = false;
    private int triggerTurn = 0;
    private DroneController droneController;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        
        Integer batteryLevel = info.getInt("budget");
        drone = new Drone(batteryLevel, Direction.EAST);
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        this.droneController = new DroneController(drone);
    }

    @Override
    public String takeDecision() {
        
        JSONObject decision = droneController.getNextMove();
        
        logger.info("** TriggerVal: {}",triggerTurn);
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        this.result = response;
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        drone.setBattery(drone.getBattery()-cost);
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        droneController.setInfo(extraInfo);
        logger.info("Additional information received: {}", extraInfo);
        logger.info("Battery: {}", drone.getBattery());
        logger.info("X: {}", drone.getX());
        logger.info("Y: {}", drone.getY());
        logger.info("LY: {}", limitY);
        logger.info("LX: {}", limitX);
        logData(extraInfo);
    }

    private void logData(JSONObject data) {
        JSONArray creeks;
        JSONArray eSites;

        if (data.has("creeks")) {
            creeks = data.getJSONArray("creeks");
            if (creeks.length() >= 1) {
                foundC = true;
                logger.info("FOUND CREEK");
            }
        }
        if (data.has("sites")) {
            eSites = data.getJSONArray("sites");
            if (eSites.length() >= 1) {
                foundE = true;
                logger.info("FOUND SITE");
            }
        }
    }

    private JSONObject end(JSONObject decision){
        decision.put("action","stop");
        return decision;
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
