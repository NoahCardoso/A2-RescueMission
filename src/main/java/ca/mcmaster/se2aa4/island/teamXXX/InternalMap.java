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
                map[i][j] = new LandTile();
            }
        }
    }

    // updates the map based on the position of the drone, the direction of the echo, and the range the echo recorded
    public void updateMap(int x, int y, int range, Direction echoDir) {
        switch (echoDir) {
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
                map[i][j] = new OceanTile();
            }
        }

        for (int i = 0; i <= limitY; i++) {
            for (int j = limitX; j > mostEast; j--) {
                map[i][j] = new OceanTile();
            }
        }

        for (int i = limitY; i > mostSouth; i--) {
            for (int j = 0; j <= limitX; j++) {
                map[i][j] = new OceanTile();
            }
        }

        for (int i = 0; i < mostNorth; i++) {
            for (int j = 0; j <= limitX; j++) {
                map[i][j] = new OceanTile();
            }
        }
    }

}
