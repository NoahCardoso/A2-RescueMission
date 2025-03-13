package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import java.util.Queue;
import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class DroneController{
    private final Logger logger = LogManager.getLogger();
    private Drone drone;
    private JSONObject info;
    Queue<JSONObject> moveQueue;
    JSONObject previousAction;
    boolean landfound = false;
    Integer distance;

    DroneController(Drone drone){
        this.drone = drone;
        this.moveQueue = new LinkedList<JSONObject>();
    }
    public void setInfo(JSONObject info){
        this.info = info;
    }
    public JSONObject getNextMove(){

        if(moveQueue.isEmpty()){
            if(landfound){

                //!scan the land!
                
                this.moveQueue.add(new JSONObject().put("action", "stop"));
            }
            else{
                if(previousAction == null){
                   
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
                    
                }
                
                else if(drone.getX() == 0 && drone.getY() == 0 && drone.getDir() == 'E'){
                    
                    if(info.has("range")){
                        this.distance = info.getInt("range");
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                        this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));

                    }

                }
                else if(drone.getX() != distance && drone.getY() == 0 && drone.getDir() == 'E'){
                    if(info.has("found")){
                        if(info.getString("found").equals("GROUND")){
                            drone.setDir('S');
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                            
                        }else{
                            drone.fly();
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                        }
                    }
                    

                }
                else if (drone.getDir() == 'S') {
                    
                    if (info.has("range")) {
                        
                        int length = info.getInt("range");
                        
                        for(int i = 0; i < length; i++){
                            drone.fly();
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                        }
                    }
                    this.landfound = true;
                }
            }
        }
        
        JSONObject move = this.moveQueue.poll();
        
        this.previousAction = move;
        return move;
        
    }


}