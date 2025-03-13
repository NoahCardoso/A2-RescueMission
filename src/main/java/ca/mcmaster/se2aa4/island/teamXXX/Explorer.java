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
        drone = new Drone(batteryLevel, 'E');
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

    

    private JSONObject searchLandDown(JSONObject decision){
        JSONObject info = result.getJSONObject("extras");

        if (drone.getDir() == 'S') {
            if (overOcean == true) {
                triggerTurn = 1;
                action = 2;
                drone.updatePosition();
                decision.put("action","fly");
                return decision;
            }

            overOcean = true;

            if (drone.getX() < limitX/2) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "E"));
                drone.setDir('E');
                drone.updatePosition();
            } else {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "W"));
                drone.setDir('W');
                drone.updatePosition();
            }
            return decision;
        }

        if (info.has("biomes")) {
            JSONArray biomes = info.getJSONArray("biomes");

            if (!(biomes.getString(0).equals("OCEAN"))) {
                overOcean = false;
            }


            if (drone.getDir() == 'E' && drone.getX() >= limitX/2 && biomes.length() == 1 && biomes.getString(0).equals("OCEAN")) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "S"));
                drone.setDir('S');
                drone.updatePosition();
                return decision;
            }

            if (drone.getDir() == 'W' && drone.getX() < limitX/2 && biomes.length() == 1 && biomes.getString(0).equals("OCEAN")) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "S"));
                drone.setDir('S');
                drone.updatePosition();
                return decision;
            }
        }

        if (action == 3 || action == 2) {
            action = 0;
            decision.put("action","scan");
            return decision;
        } else {
            action = 2;
            drone.updatePosition();
            decision.put("action","fly");
            return decision;
        }
    }

    private JSONObject searchLandUp(JSONObject decision){
        JSONObject info = result.getJSONObject("extras");

        if (drone.getDir() == 'N') {
            if (drone.getX() < limitX/2) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "E"));
                drone.setDir('E');
                drone.updatePosition();
            } else {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "W"));
                drone.setDir('W');
                drone.updatePosition();
            }
            return decision;
        }

        if (info.has("biomes")) {
            JSONArray biomes = info.getJSONArray("biomes");

            if (drone.getDir() == 'E' && drone.getX() >= limitX/2 && biomes.length() == 1 && biomes.getString(0).equals("OCEAN")) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "N"));
                drone.setDir('N');
                drone.updatePosition();
                return decision;
            }

            if (drone.getDir() == 'W' && drone.getX() < limitX/2 && biomes.length() == 1 && biomes.getString(0).equals("OCEAN")) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "N"));
                drone.setDir('N');
                drone.updatePosition();
                return decision;
            }
        }

        if (action == 3 || action == 2) {
            action = 0;
            decision.put("action","scan");
            return decision;
        } else {
            action = 2;
            drone.updatePosition();
            decision.put("action","fly");
            return decision;
        }
    }

    private JSONObject turn(JSONObject decision) {
        if (triggerTurn == 1) {
            if (drone.getX() < limitX/2) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "E"));
                drone.setDir('E');
                drone.updatePosition();
            } else {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "W"));
                drone.setDir('W');
                drone.updatePosition();
            }
            triggerTurn++;
            return decision;
        } else if (triggerTurn == 2) {
            action = 3;
            drone.updatePosition();
            decision.put("action", "heading");
            decision.put("parameters", new JSONObject().put("direction", "N"));
            drone.setDir('N');
            drone.updatePosition();
            triggerTurn++;
            return decision;
        } else {
            if (drone.getX() < limitX/2) {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "E"));
                drone.setDir('E');
                drone.updatePosition();
            } else {
                action = 3;
                drone.updatePosition();
                decision.put("action", "heading");
                decision.put("parameters", new JSONObject().put("direction", "W"));
                drone.setDir('W');
                drone.updatePosition();
            }
            triggerTurn++;
            return decision;
        }
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
