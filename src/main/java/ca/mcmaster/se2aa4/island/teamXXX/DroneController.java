package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import java.util.Queue;
import java.util.LinkedList;

public class DroneController{

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
        
        JSONObject decision = new JSONObject();
        if(moveQueue.isEmpty()){
            if(landfound){
                decision.put("action", "stop");
                this.moveQueue.add(decision);
            }
            else{
                if(previousAction == null){
                    
                    decision.put("action", "echo");
                    decision.put("parameters", new JSONObject().put("direction", "E"));
                    this.moveQueue.add(decision);
                }
                //move forward till find land to your right
                else if(drone.getX() == 0 && drone.getY() == 0 && drone.getDir() == 'E'){
                    if(info.has("range")){
                        this.distance = info.getInt("range");
                        decision.put("action", "fly");
                        this.moveQueue.add(decision);
                        decision.put("action", "echo");
                        decision.put("parameters", new JSONObject().put("direction", "S"));
                        this.moveQueue.add(decision);

                    }

                }
                else if(drone.getX() != distance && drone.getY() == 0 && drone.getDir() == 'E'){
                    if(info.has("found")){
                        if(info.getString("found").equals("GROUND")){
                            decision.put("action", "heading");
                            decision.put("parameters", new JSONObject().put("direction", "S"));
                            this.moveQueue.add(decision);
                        }else{
                            decision.put("action", "fly");
                            this.moveQueue.add(decision);
                            decision.put("action", "echo");
                            decision.put("parameters", new JSONObject().put("direction", "S"));
                            this.moveQueue.add(decision);
                        }
                    }
                    

                }
                else if (drone.getDir() == 'S') {
                    if (info.has("range")) {
                        int length = info.getInt("range");
                        decision.put("action", "fly");
                        for(int i = 0; i < length; i++){
                            this.moveQueue.add(decision);
                        }
                    }
                    landfound = true;
                }
            }
        }
        
        JSONObject move = this.moveQueue.poll();
        
        this.previousAction = move;
        return move;
        
    }


}