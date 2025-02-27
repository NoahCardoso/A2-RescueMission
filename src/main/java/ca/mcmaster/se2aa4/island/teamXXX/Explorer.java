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
    private int x = 1;
    private int y = 1;
    private int limitX = 0;
    private int limitY = 0;
    private int dstX = 0;
    private int dstY = 0;
    private char dir = 'E';
    private int n = 0;
    private boolean echo = true;

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
        decision = findLand(decision);
        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    private JSONObject findLand(JSONObject decision){
        
        if (n == 1){
            decision.put("action","stop");
        }
        //find boarders
        if (limitX == 0 && limitY == 0){
            if(result == null){
                decision.put("action", "echo");
                decision.put("parameters", new JSONObject().put("direction", "E"));
            }
            else{
                JSONObject extra = result.getJSONObject("extras");
                this.limitX = extra.getInt("range");
                decision.put("action", "echo");
                decision.put("parameters", new JSONObject().put("direction", "S"));
            }
        }
        else if(limitY == 0){
            JSONObject extra = result.getJSONObject("extras");
            this.limitY = extra.getInt("range");
        }
        if(limitY != 0){
            
            if(x < limitX/2){
                if(x < limitX/2 - 1){
                    x++;
                    decision.put("action", "fly");
                    return decision;
                }
                x++;
                if(x == limitX/2){
                    decision.put("action", "heading");
                    decision.put("parameters", new JSONObject().put("direction", "S"));
                    dir = 'S';
                    y++;
                }
                return decision;
            }
            else if(echo){
                JSONObject extra = result.getJSONObject("extras");
                if(extra.has("found")){
                    String found = extra.getString("found");
                    if(found.equals("GROUND")){
                        dstY = extra.getInt("range")+1;
                    }
                    echo = false;
                }
                decision.put("action", "echo");
                decision.put("parameters", new JSONObject().put("direction", "S"));
                return decision;
            }
            else if(dstY > 0){
                y++;
                dstY--;
                decision.put("action","fly");
                return decision;
            }
            else{
                if( n == 0){
                    decision.put("action","scan");
                    n = 1;
                }
                
                
            }
            
            
        }
        
        return decision;
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
