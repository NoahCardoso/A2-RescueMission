package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Arrays;

public class InternalMap {
    private final int limitX;
    private final int limitY;
    private int[][] map;
    private int mostNorth;
    private int mostEast;
    private int mostSouth;
    private int mostWest;
    private boolean built = false;

    InternalMap(int limitX, int limitY) {
        this.limitX = limitX;
        this.limitY = limitY;
        map = new int[limitY+1][limitX+1];
        
        for (int i = 0; i <= limitY; i++) {
            for (int j = 0; j <= limitX; j++) {
                map[i][j] = 1;
            }
        }
    }

    public void updateMap(int x, int y, int range, Direction echoDir) {
        if (echoDir == Direction.EAST) {
            for (int i = 0; i < x + range; i++) {
                map[y][i] = 0;
            }
        } else if (echoDir == Direction.SOUTH) {
            for (int i = 0; i < y + range; i++) {
                map[i][x] = 0;
            }
        } else if (echoDir == Direction.WEST) {
            for (int i = 0; i < limitX - x + range; i++) {
                map[y][limitX-i] = 0;
            }
        } else {
            for (int i = 0; i < limitY - y + range; i++) {
                map[limitY-i][x] = 0;
            }
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

    public boolean isBuilt() {
        return built;
    }

    public void setBuilt() {
        built = true;
    }

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
