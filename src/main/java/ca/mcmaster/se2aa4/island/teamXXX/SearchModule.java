package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Queue;

import org.json.JSONObject;

public class SearchModule{

    private Drone drone; 
    private InternalMap map;
    private boolean mapIsBuilt = false;
    Queue<Action> moveQueue; //Temporary variable until functionality is merged with drone class
    private JSONObject info;

    JSONObject previousAction;
    boolean landfound = false;
    Direction scanning = Direction.EAST;
    boolean justTurned = false;
    Integer distance;

    SearchModule(){
    }

    public boolean getBuildStatus(){
        return mapIsBuilt;
    }

    public boolean getInitializeStatus(){
        if(this.map == null){
            return false;
        }

        return true;
    }

    // scans east and south, then initializes a map with those values
    public void initializeInternalMap() {
        if (map == null && previousAction == null) {
            this.moveQueue.add(new Echo(Direction.EAST));
        } else if (map == null && distance == null) {
            distance = info.getInt("range");
            this.moveQueue.add(new Echo(Direction.SOUTH));
        } else if (map == null) {
            map = new InternalMap(distance, info.getInt("range"));
            map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.SOUTH);
            drone.fly();
            this.moveQueue.add(new JSONObject().put("action", "fly"));
            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
        }
    }

    public void buildInternalMap() {
        if (landfound == false && drone.getDir() == Direction.EAST) {
            if (info.getString("found").equals("GROUND")) {
                landfound = true;
                map.setWest(drone.getX());
                map.setNorth(info.getInt("range"));
            }
            map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.SOUTH);
            drone.fly();
            this.moveQueue.add(new JSONObject().put("action", "fly"));
            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
        } else if (landfound == true && drone.getDir() == Direction.EAST) {
            if (info.getString("found").equals("OUT_OF_RANGE")) {
                landfound = false;
                this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                map.setEast(drone.getX()-1);
                drone.fly();
                drone.setDir(Direction.SOUTH);
                drone.fly();
            } else {
                if (info.getInt("range") < map.getNorth()) {
                    map.setNorth(info.getInt("range"));
                }
                map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.SOUTH);
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
            }
        } else if (drone.getDir() == Direction.SOUTH) {
            if (drone.getY() < map.getNorth() - 1) {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
            } else if (landfound == false) {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "W")));
                if (info.has("found") && info.getString("found").equals("GROUND")) {
                    landfound = true;
                }
            } else {
                if (info.has("found")) {
                    if (info.getString("found").equals("OUT_OF_RANGE")) {
                        landfound = false;
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                        map.setSouth(drone.getY()-1);
                        drone.fly();
                        drone.setDir(Direction.WEST);
                        drone.fly();
                    } else {
                        map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.WEST);
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                        this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "W")));
                    }
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "W")));
                }
            }
        } else if (drone.getDir() == Direction.WEST) {
            if (drone.getX() > map.getEast() + 1) {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
            } else if (landfound == false) {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                if (info.has("found") && info.getString("found").equals("GROUND")) {
                    landfound = true;
                }
            } else {
                if (info.has("found")) {
                    if (info.getString("found").equals("OUT_OF_RANGE")) {
                        landfound = false;
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                        drone.fly();
                        drone.setDir(Direction.NORTH);
                        drone.fly();
                    } else {
                        map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.NORTH);
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                        this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                    }
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "N")));
                }
            }
        } else {
            if (drone.getY() > map.getSouth() + 1) {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
            } else if (landfound == false) {
                drone.fly();
                this.moveQueue.add(new JSONObject().put("action", "fly"));
                this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
                if (info.has("found") && info.getString("found").equals("GROUND")) {
                    landfound = true;
                }
            } else {
                if (info.has("found")) {
                    if (info.getString("found").equals("OUT_OF_RANGE")) {
                        landfound = false;
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                        map.cleanMap();
                        map.setBuilt();
                        drone.setDir(Direction.EAST);
                        drone.fly();
                        drone.fly();
                        drone.setDir(Direction.SOUTH);
                    } else {
                        map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.EAST);
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                        this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
                    }
                } else {
                    this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
                }
            }
        }
    }

}