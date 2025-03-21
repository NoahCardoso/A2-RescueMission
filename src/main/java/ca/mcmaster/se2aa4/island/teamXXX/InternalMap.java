package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Arrays;


//Map Module
public class InternalMap {
    private final int limitX;
    private final int limitY;
    private Tile[][] map;
    private int mostNorth; // y value of the most north point of the island
    private int mostEast; // x value of the most east point of the island
    private int mostSouth; // y value of the most south point of the island
    private int mostWest; // x value of the most west point of the island

    InternalMap(int limitX, int limitY) {
        this.limitX = limitX;
        this.limitY = limitY;
        map = new Tile[limitY+1][limitX+1];
        
        for (int i = 0; i <= limitY; i++) {
            for (int j = 0; j <= limitX; j++) {
                map[i][j] = new OceanTile();
            }
        }
    }

    // updates the map based on the position of the drone, the direction of the echo, and the range the echo recorded
    public void updateMap(int x, int y, int range, Direction echoDir) {
        if (null == echoDir) {
            for (int i = 0; i < limitY - y + range; i++) {
                map[limitY-i][x] = new LandTile();
            }
        } else switch (echoDir) {
            case EAST -> {
                for (int i = 0; i < x + range; i++) {
                    map[y][i] = new OceanTile();
                }
            }
            case SOUTH -> {
                for (int i = 0; i < y + range; i++) {
                    map[i][x] = new OceanTile();
                }
            }
            case WEST -> {
                for (int i = 0; i < limitX - x + range; i++) {
                    map[y][limitX-i] = new OceanTile();
                }
            }
            default -> {
                for (int i = 0; i < limitY - y + range; i++) {
                    map[limitY-i][x] = new OceanTile();
                }
            }
        }
    }

    public int getLimitX(){
        return limitX;
    }
    
    public int getLimitY(){
        return limitY;
    }

    public Tile getTile(int x,int y){
        try {
            return map[x][y];
        } catch (Exception e) {
            return new UnknownTile();
        }
    }

    public void setNorth(int val) {
        mostNorth = val;
    }

    public void setEast(int val) {
        mostEast = val;
    }

    public void setSouth(int val) {
        mostSouth = val;
    }

    public void setWest(int val) {
        mostWest = val;
    }

    public int getNorth() {
        return mostNorth;
    }

    public int getEast() {
        return mostEast;
    }

    public int getSouth() {
        return mostSouth;
    }

    public int getWest() {
        return mostWest;
    }

    // returns the y value that the drone should turn at to get to the next piece of land (returns -1 if there is no more land to turn to)
    public int getNextTurn(int x, int y, Direction moving, Direction turning) {
        if (moving == Direction.NORTH) {
            if (turning == Direction.EAST) {
                for (int i = 0; i <= limitY; i++) {
                    if (map[i][x+2] == 1) {
                        return i;
                    }
                }
                return -1;
            } else {
                for (int i = 0; i <= limitY; i++) {
                    if (map[i][x-2] == 1) {
                        return i;
                    }
                }
                return -1;
            }
        } else {
            if (turning == Direction.EAST) {
                for (int i = limitY; i >= 0; i--) {
                    if (map[i][x+2] == 1) {
                        return i;
                    }
                }
                return -1;
            } else {
                for (int i = limitY; i >= 0; i--) {
                    if (map[i][x-2] == 1) {
                        return i;
                    }
                }
                return -1;
            }
        }
    }

    //Should be removed 
    // returns the y value of the next land to scan (returns -1 if no more land in front of the drone)
    public int nextLand(int x, int y, Direction moving) {
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

    public String displayMap() {
        StringBuffer m = new StringBuffer();
        m.append("\n");

        for (int i = 0; i <= limitY; i++) {
            m.append(Arrays.toString(map[i]));
            m.append("\n");
        }

        return m.toString();
    }

    // cleans the map after build (can potentially move this to be called in the setBuilt() method)
    public void cleanMap() {
        for (int i = 0; i <= limitY; i++) {
            for (int j = 0; j < mostWest; j++) {
                map[i][j] = 0;
            }
        }

        for (int i = 0; i <= limitY; i++) {
            for (int j = limitX; j > mostEast; j--) {
                map[i][j] = 0;
            }
        }

        for (int i = limitY; i > mostSouth; i--) {
            for (int j = 0; j <= limitX; j++) {
                map[i][j] = 0;
            }
        }

        for (int i = 0; i < mostNorth; i++) {
            for (int j = 0; j <= limitX; j++) {
                map[i][j] = 0;
            }
        }
    }

    // if true the drone should turn to the east, otherwise it should turn to the west (handles even or odd width islands)
    public boolean hasLandEast(int x) {
        for (int i = 0; i <= limitY; i++) {
            for (int j = x + 1; j <= limitX; j++) {
                if (map[i][j] == 1) {
                    return true;
                }
            }
        }

        return false;
    }

}
