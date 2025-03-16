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
    public InternalMap map;

    DroneController(Drone drone){
        this.drone = drone;
        this.moveQueue = new LinkedList<JSONObject>();
    }

    public void setInfo(JSONObject info){
        this.info = info;
    }

    public JSONObject getNextMove(){

        if(moveQueue.isEmpty()){
            if (map == null) {
                initializeInternalMap();
            } else if (map.isBuilt() == false) {
                buildInternalMap();
            } else if (scanning == Direction.EAST) {
                // logger.info("{}", map.displayMap());
                // this.moveQueue.add(new JSONObject().put("action", "stop"));
                scanEast();
            } else {
                scanWest();
            }
        }
        
        JSONObject move = this.moveQueue.poll();
        
        this.previousAction = move;

        return move;
        
    }

    private void initializeInternalMap() {
        if (map == null && previousAction == null) {
            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "E")));
        } else if (map == null && distance == null) {
            distance = info.getInt("range");
            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
        } else if (map == null) {
            map = new InternalMap(distance, info.getInt("range"));
            map.updateMap(drone.getX(), drone.getY(), info.getInt("range"), Direction.SOUTH);
            drone.fly();
            this.moveQueue.add(new JSONObject().put("action", "fly"));
            this.moveQueue.add(new JSONObject().put("action", "echo").put("parameters", new JSONObject().put("direction", "S")));
        }
    }

    private void buildInternalMap() {
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

    private void scanEast() {
        int y = map.nextLand(drone.getX(), drone.getY(), drone.getDir());

        if (y != -1) {
            if (drone.getDir() == Direction.SOUTH) {
                if (drone.getY() < y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else if (drone.getY() == y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "scan"));
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
            } else {
                if (drone.getY() > y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else if (drone.getY() == y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "scan"));
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
            }
        } else {
            y = map.getNextTurn(drone.getX(), drone.getY(), drone.getDir(), Direction.EAST);
            if (y != -1) {
                if (drone.getDir() == Direction.SOUTH) {
                    if (drone.getY() >= y) {
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                        drone.setDir(Direction.EAST);
                        drone.fly();
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                        drone.setDir(Direction.NORTH);
                    } else {
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                    }
                } else {
                    if (drone.getY() <= y) {
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                        drone.setDir(Direction.EAST);
                        drone.fly();
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                        drone.setDir(Direction.SOUTH);
                    } else {
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                    }
                }
            } else {
                if (map.hasLandEast(drone.getX())) {
                    y = map.getNextTurn(drone.getX()-1, drone.getY(), drone.getDir(), Direction.EAST);
                    if (drone.getDir() == Direction.SOUTH) {
                        if (drone.getY() >= y) {
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                            drone.setDir(Direction.EAST);
                            drone.fly();
                            drone.setDir(Direction.NORTH);
                            scanning = Direction.WEST;
                        } else {
                            drone.fly();
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                        }
                    } else {
                        if (drone.getY() <= y) {
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            drone.setDir(Direction.EAST);
                            drone.fly();
                            drone.setDir(Direction.SOUTH);
                            scanning = Direction.WEST;
                        } else {
                            drone.fly();
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                        }
                    }
                } else {
                    y = map.getNextTurn(drone.getX()+1, drone.getY(), drone.getDir(), Direction.WEST);
                    if (drone.getDir() == Direction.SOUTH) {
                        if (drone.getY() >= y) {
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                            drone.setDir(Direction.WEST);
                            drone.fly();
                            drone.setDir(Direction.NORTH);
                            scanning = Direction.WEST;
                        } else {
                            drone.fly();
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                        }
                    } else {
                        if (drone.getY() <= y) {
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "E")));
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                            this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                            drone.setDir(Direction.WEST);
                            drone.fly();
                            drone.setDir(Direction.SOUTH);
                            scanning = Direction.WEST;
                        } else {
                            drone.fly();
                            this.moveQueue.add(new JSONObject().put("action", "fly"));
                        }
                    }
                }
            }
        }
    }

    private void scanWest() {
        int y = map.nextLand(drone.getX(), drone.getY(), drone.getDir());

        if (y != -1) {
            if (drone.getDir() == Direction.SOUTH) {
                if (drone.getY() < y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else if (drone.getY() == y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "scan"));
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
            } else {
                if (drone.getY() > y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else if (drone.getY() == y) {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "scan"));
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                } else {
                    drone.fly();
                    this.moveQueue.add(new JSONObject().put("action", "fly"));
                }
            }
        } else {
            y = map.getNextTurn(drone.getX(), drone.getY(), drone.getDir(), Direction.WEST);
            if (y != -1) {
                if (drone.getDir() == Direction.SOUTH) {
                    if (drone.getY() >= y) {
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                        drone.setDir(Direction.WEST);
                        drone.fly();
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "N")));
                        drone.setDir(Direction.NORTH);
                    } else {
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                    }
                } else {
                    if (drone.getY() <= y) {
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "W")));
                        drone.setDir(Direction.WEST);
                        drone.fly();
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "heading").put("parameters", new JSONObject().put("direction", "S")));
                        drone.setDir(Direction.SOUTH);
                    } else {
                        drone.fly();
                        this.moveQueue.add(new JSONObject().put("action", "fly"));
                    }
                }
            } else {
                this.moveQueue.add(new JSONObject().put("action", "stop"));
            }
        }
    }


}