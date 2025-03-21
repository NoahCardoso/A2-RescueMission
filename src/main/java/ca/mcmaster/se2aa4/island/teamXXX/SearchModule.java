package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Queue;

import org.json.JSONObject;

public class SearchModule{

    private InternalMap map;
    private boolean mapIsBuilt = false;
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
    public void initializeInternalMap(Queue<Action> moveQueue,int x, int y, JSONObject info) {
        if (map == null && previousAction == null) {
            moveQueue.add(new Echo(Direction.EAST));
        } else if (map == null && distance == null) {
            distance = info.getInt("range");
            moveQueue.add(new Echo(Direction.SOUTH));
        } else if (map == null) {
            map = new InternalMap(distance, info.getInt("range"));
            map.updateMap(x, y, info.getInt("range"), Direction.SOUTH);
            moveQueue.add(new Fly());
            moveQueue.add(new Echo(Direction.SOUTH));
        }
    }

    public void buildInternalMap(Queue<Action> moveQueue,int x, int y,JSONObject info, Direction dir) {
        if (landfound == false && dir == Direction.EAST) {
            if (info.getString("found").equals("GROUND")) {
                landfound = true;
                map.setWest(x);
                map.setNorth(info.getInt("range"));
            }
            map.updateMap(x, y, info.getInt("range"), Direction.SOUTH);
            moveQueue.add(new Fly());
            moveQueue.add(new Echo(Direction.SOUTH));

        } else if (landfound == true && dir == Direction.EAST) {
            if (info.getString("found").equals("OUT_OF_RANGE")) {
                landfound = false;
                moveQueue.add(new Heading(Direction.SOUTH));
                map.setEast(x-1);
            } else {
                if (info.getInt("range") < map.getNorth()) {
                    map.setNorth(info.getInt("range"));
                }
                map.updateMap(x, y, info.getInt("range"), Direction.SOUTH);
                moveQueue.add(new Fly());
                moveQueue.add(new Echo(Direction.SOUTH));
            }
        } else if (dir == Direction.SOUTH) {
            if (y < map.getNorth() - 1) {
                moveQueue.add(new Fly());
            } else if (landfound == false) {
                moveQueue.add(new Fly());
                moveQueue.add(new Echo(Direction.WEST));
                if (info.has("found") && info.getString("found").equals("GROUND")) {
                    landfound = true;
                }
            } else {
                if (info.has("found")) {
                    if (info.getString("found").equals("OUT_OF_RANGE")) {
                        landfound = false;
                        moveQueue.add(new Heading(Direction.WEST));
                        map.setSouth(y-1);
                    } else {
                        map.updateMap(x, y, info.getInt("range"), Direction.WEST);
                        moveQueue.add(new Fly());
                        moveQueue.add(new Echo(Direction.WEST));
                    }
                } else {
                        moveQueue.add(new Echo(Direction.WEST));
                }
            }
        } else if (dir == Direction.WEST) {
            if (x > map.getEast() + 1) {
                moveQueue.add(new Fly());
            } else if (landfound == false) {
                moveQueue.add(new Fly());
                moveQueue.add(new Echo(Direction.NORTH));
                if (info.has("found") && info.getString("found").equals("GROUND")) {
                    landfound = true;
                }
            } else {
                if (info.has("found")) {
                    if (info.getString("found").equals("OUT_OF_RANGE")) {
                        landfound = false;
                        moveQueue.add(new Heading(Direction.NORTH));

                    } else {
                        map.updateMap(x, y, info.getInt("range"), Direction.NORTH);
                        moveQueue.add(new Fly());
                        moveQueue.add(new Echo(Direction.NORTH));
                    }
                } else {
                    moveQueue.add(new Echo(Direction.NORTH));
                }
            }
        } else {
            if (y > map.getSouth() + 1) {
                moveQueue.add(new Fly());
            } else if (landfound == false) {
                moveQueue.add(new Fly());
                moveQueue.add(new Echo(Direction.EAST));
                if (info.has("found") && info.getString("found").equals("GROUND")) {
                    landfound = true;
                }
            } else {
                if (info.has("found")) {
                    if (info.getString("found").equals("OUT_OF_RANGE")) {
                        landfound = false;
                        moveQueue.add(new Heading(Direction.EAST));
                        moveQueue.add(new Heading(Direction.SOUTH));
                        map.cleanMap();
                        mapIsBuilt = true;
                    } else {
                        map.updateMap(x, y, info.getInt("range"), Direction.EAST);
                        moveQueue.add(new Fly());
                        moveQueue.add(new Echo(Direction.EAST));
                    }
                } else {
                    moveQueue.add(new Echo(Direction.EAST));
                }
            }
        }
    }

    // moves left to right, scanning wherever the internal map has a value of 1
    private void scanEast(Queue<Action> moveQueue,int x, int y,JSONObject info, Direction dir) {
        int y = map.nextLand(x, y, dir);

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




    // returns the y value of the next land to scan (returns -1 if no more land in front of the drone)
    private int nextLand(int x, int y, Direction moving) {
        if (moving == Direction.NORTH) {
            for (int i = y; i >= 0; i--) {
                if (map[i][x] == 1) {
                    return i;
                }
            }
        } else {
            for (int i = y; i <= limitY; i++) {
                if (map[i][x] == 1) {
                    return i;
                }
            }
        }

        return -1;
    }
}