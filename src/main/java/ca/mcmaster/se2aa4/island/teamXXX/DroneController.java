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
    Direction scanning = Direction.EAST;
    boolean justTurned = false;
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
                if (scanning == Direction.EAST) {
                    scanEast();
                } else {
                    scanWest();
                }
            }
            else {
                locateLand();
            }
        }
        
        JSONObject move = this.moveQueue.poll();
        
        this.previousAction = move;

        return move;
        
    }

    private void locateLand() {
        if(previousAction == null){
                   
            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
            
        }
        
        else if(drone.getX() == 0 && drone.getY() == 0 && drone.getDir() == Direction.EAST){
            
            if(info.has("range")){
                this.distance = info.getInt("range");
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));

            }

        }
        else if(drone.getX() != distance && drone.getY() == 0 && drone.getDir() == Direction.EAST){
            if(info.has("found")){
                if(info.getString("found").equals("GROUND")){
                    drone.setDir(Direction.SOUTH);
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                    
                }else{
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                }
            }
            

        }
        else if (drone.getDir() == Direction.SOUTH) {
            
            if (info.has("range")) {
                
                int length = info.getInt("range");
                
                for(int i = 0; i <= length; i++){
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
            }
            this.landfound = true;
            this.moveQueue.add(new JSONObject().put("action", "scan"));
        }
    }

    private void scanEast() {
        if (justTurned && info.getString("found").equals("OUT_OF_RANGE")) {
            if (drone.getDir() == Direction.NORTH) {
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));

                drone.setDir(Direction.WEST);
                drone.fly();
                drone.fly();
                drone.fly();
                drone.setDir(Direction.SOUTH);
                drone.fly();
                drone.fly();
                drone.setDir(Direction.NORTH);
            } else {
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));

                drone.setDir(Direction.WEST);
                drone.fly();
                drone.fly();
                drone.fly();
                drone.setDir(Direction.NORTH);
                drone.fly();
                drone.fly();
                drone.setDir(Direction.SOUTH);
            }

            justTurned = false;
            scanning = Direction.WEST;
        } else if (previousAction.getString("action").equals("echo") && (previousAction.getJSONObject("parameters").getString("direction").equals("S") || previousAction.getJSONObject("parameters").getString("direction").equals("N"))) {
            int length = info.getInt("range");

            if (info.getString("found").equals("OUT_OF_RANGE")) {
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
            } else {
                for (int i = 0; i <= length; i++) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
                this.moveQueue.add(new JSONObject().put("action", "scan"));
            }
            justTurned = false;
        } else if (previousAction.getString("action").equals("fly")) {
            this.moveQueue.add(new JSONObject().put("action", "scan"));
            justTurned = false;
        } else if (previousAction.getString("action").equals("scan")) {
            if (info.getJSONArray("biomes").toList().contains("OCEAN")) {
                if (drone.getDir() == Direction.SOUTH) {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                }
            } else {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "scan"));
            }
            justTurned = false;
        } else if (previousAction.getString("action").equals("echo") && previousAction.getJSONObject("parameters").getString("direction").equals("E")) {
            int length = info.getInt("range");

            if (length >= 2 || info.getString("found").equals("OUT_OF_RANGE")) {
                if (drone.getDir() == Direction.SOUTH) {
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                    drone.setDir(Direction.EAST);
                    drone.fly();
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                    drone.setDir(Direction.NORTH);
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                    drone.setDir(Direction.EAST);
                    drone.fly();
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                    drone.setDir(Direction.SOUTH);
                }

                justTurned = true;
            } else {
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
                justTurned = false;
            }
        }
    }

    private void scanWest() {
        if (justTurned && info.getString("found").equals("OUT_OF_RANGE")) {
            this.moveQueue.add(new JSONObject().put("action", "stop"));
        } else if (previousAction.getString("action").equals("echo") && (previousAction.getJSONObject("parameters").getString("direction").equals("S") || previousAction.getJSONObject("parameters").getString("direction").equals("N"))) {
            int length = info.getInt("range");

            if (info.getString("found").equals("OUT_OF_RANGE")) {
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "W")));
            } else {
                for (int i = 0; i <= length; i++) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
                this.moveQueue.add(new JSONObject().put("action", "scan"));
            }
            justTurned = false;
        } else if (previousAction.getString("action").equals("fly")) {
            this.moveQueue.add(new JSONObject().put("action", "scan"));
            justTurned = false;
        } else if (previousAction.getString("action").equals("scan")) {
            if (info.getJSONArray("biomes").toList().contains("OCEAN")) {
                if (drone.getDir() == Direction.SOUTH) {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                }
            } else {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "scan"));
            }
            justTurned = false;
        } else if (previousAction.getString("action").equals("echo") && previousAction.getJSONObject("parameters").getString("direction").equals("W")) {
            int length = info.getInt("range");

            if (length >= 2 || info.getString("found").equals("OUT_OF_RANGE")) {
                if (drone.getDir() == Direction.SOUTH) {
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                    drone.setDir(Direction.WEST);
                    drone.fly();
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                    drone.setDir(Direction.NORTH);
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                    drone.setDir(Direction.WEST);
                    drone.fly();
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
                    drone.setDir(Direction.SOUTH);
                }

                justTurned = true;
            } else {
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "W")));
                justTurned = false;
            }
        }
    }


}