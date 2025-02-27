package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private JSONObject result = null;
    private String direction = null;
    private int count = 0;
    private int dst = 0;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        
        JSONObject decision = new JSONObject();
        findLand(decision);
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    private void findLand(JSONObject decision){
        decision.put("action", "fly");

        if(count == 31){
            
            decision.put("action", "heading");
            decision.put("parameters", new JSONObject().put("direction", "S"));
    
        }
        else if(count == 32){
            decision.put("action", "echo");
            decision.put("parameters", new JSONObject().put("direction", "S"));

        }
        else if(count == 33){
            JSONObject extra = result.getJSONObject("extras");
            String found = extra.getString("found");
            if(found.equals("GROUND")){
                this.dst = extra.getInt("range");
            }
        }
        /*
        else if(count == 34+dst){
            decision.put("action", "scan");
        }*/
        else if(count == 34+dst){
            decision.put("action", "stop");
            
        }
        
        count++;
        logger.info(count);
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        this.result = response;
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
