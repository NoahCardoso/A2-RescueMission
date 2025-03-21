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
    private void scanEast(Queue<Action> moveQueue,int droneX, int droneY,JSONObject info, Direction dir) {
        int y = nextLand(droneX, droneY, dir);

        if (y != -1) {
            if (dir == Direction.SOUTH) {
                if (droneY < y) {
                    moveQueue.add(new Fly());
                } else if (droneY == y) {
                    moveQueue.add(new Scan());
                    moveQueue.add(new Fly());
                } else {
                    moveQueue.add(new Fly());
                }
            } else {
                if (droneY > y) {
                    moveQueue.add(new Fly());
                } else if (droneY == y) {
                    moveQueue.add(new Scan());
                    moveQueue.add(new Fly());
                } else {
                    moveQueue.add(new Fly());
                }
            }
        } else {
            y = map.getNextTurn(droneX, droneY, dir, Direction.EAST);
            if (y != -1) {
                if (dir == Direction.SOUTH) {
                    if (droneY >= y) {
                        moveQueue.add(new Heading(Direction.EAST));
                        moveQueue.add(new Heading(Direction.NORTH));
                    } else {
                        moveQueue.add(new Fly());
                    }
                } else {
                    if (droneY <= y) {
                        moveQueue.add(new Heading(Direction.EAST));
                        moveQueue.add(new Heading(Direction.SOUTH));
                    } else {
                        moveQueue.add(new Fly());
                    }
                }
            } else {
                if (map.hasLandEast(droneX)) {
                    y = map.getNextTurn(droneX-1, droneY, dir, Direction.EAST);
                    if (dir == Direction.SOUTH) {
                        if (droneY >= y) {
                            moveQueue.add(new Heading(Direction.WEST));
                            moveQueue.add(new Fly());
                            moveQueue.add(new Heading(Direction.NORTH));
                            moveQueue.add(new Heading(Direction.EAST));
                            moveQueue.add(new Heading(Direction.SOUTH));
                            moveQueue.add(new Heading(Direction.EAST));
                            moveQueue.add(new Heading(Direction.NORTH));
                            scanning = Direction.WEST;
                        } else {
                            moveQueue.add(new Fly());
                        }
                    } else {
                        if (droneY <= y) {
                            moveQueue.add(new Heading(Direction.WEST));
                            moveQueue.add(new Fly());
                            moveQueue.add(new Heading(Direction.SOUTH));
                            moveQueue.add(new Heading(Direction.EAST));
                            moveQueue.add(new Heading(Direction.NORTH));
                            moveQueue.add(new Heading(Direction.EAST));
                            moveQueue.add(new Heading(Direction.SOUTH));
                            scanning = Direction.WEST;
                        } else {
                            moveQueue.add(new Fly());
                        }
                    }
                } else {
                    y = map.getNextTurn(droneX+1, droneY, dir, Direction.WEST);
                    if (dir == Direction.SOUTH) {
                        if (droneY >= y) {
                            moveQueue.add(new Heading(Direction.WEST));
                            moveQueue.add(new Heading(Direction.NORTH));
                            moveQueue.add(new Heading(Direction.WEST));
                            moveQueue.add(new Heading(Direction.SOUTH));
                            moveQueue.add(new Heading(Direction.EAST));
                            moveQueue.add(new Fly());
                            moveQueue.add(new Heading(Direction.NORTH));
                            scanning = Direction.WEST;
                        } else {
                            moveQueue.add(new Fly());
                        }
                    } else {
                        if (droneY <= y) {
                            moveQueue.add(new Heading(Direction.WEST));
                            moveQueue.add(new Heading(Direction.SOUTH));
                            moveQueue.add(new Heading(Direction.WEST));
                            moveQueue.add(new Heading(Direction.NORTH));
                            moveQueue.add(new Heading(Direction.EAST));
                            moveQueue.add(new Fly());
                            moveQueue.add(new Heading(Direction.SOUTH));
                            scanning = Direction.WEST;
                        } else {
                            moveQueue.add(new Fly());
                        }
                    }
                }
            }
        }
    }


    private void scanWest(Queue<Action> moveQueue,int droneX, int droneY,JSONObject info, Direction dir) {
        int y = map.nextLand(droneX, droneY, dir);

        if (y != -1) {
            if (dir == Direction.SOUTH) {
                if (droneY < y) {
                    moveQueue.add(new Fly());
                } else if (droneY == y) {
                    moveQueue.add(new Scan());
                    moveQueue.add(new Fly());
                } else {
                    moveQueue.add(new Fly());
                }
            } else {
                if (droneY > y) {
                    moveQueue.add(new Fly());
                } else if (droneY == y) {
                    moveQueue.add(new Scan());
                    moveQueue.add(new Fly());
                } else {
                    moveQueue.add(new Fly());
                }
            }
        } else {
            y = map.getNextTurn(droneX, droneY, dir, Direction.WEST);
            if (y != -1) {
                if (dir == Direction.SOUTH) {
                    if (droneY >= y) {
                        moveQueue.add(new Heading(Direction.WEST));
                        moveQueue.add(new Heading(Direction.NORTH));
                    } else {
                        moveQueue.add(new Fly());
                    }
                } else {
                    if (droneY <= y) {
                        moveQueue.add(new Heading(Direction.WEST));
                        moveQueue.add(new Heading(Direction.SOUTH));
                    } else {
                        moveQueue.add(new Fly());
                    }
                }
            } else {
                this.moveQueue.add(new JSONObject().put("action", "stop"));
            }
        }
    }

    // returns the y value of the next land to scan (returns -1 if no more land in front of the drone)
    private int nextLand(int x, int y, Direction moving) {
        if (moving == Direction.NORTH) {
            for (int i = y; i >= 0; i--) {
                if (map.getTile(i,x).getTileType() == TileType.LAND) {
                    return i;
                }
            }
        } else {
            for (int i = y; i <= map.getLimitY(); i++) {
                if (map.getTile(i,x).getTileType() == TileType.LAND) {
                    return i;
                }
            }
        }

        return -1;
    }


}