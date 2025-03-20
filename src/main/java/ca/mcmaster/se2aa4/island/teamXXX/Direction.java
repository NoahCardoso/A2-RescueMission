package ca.mcmaster.se2aa4.island.teamXXX;

enum Direction{
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private final String message; 

    Direction(String message) {  
        this.message = message;
    }

    public String toString() {  
        return message;
    }

    static public Direction fromString(String dir) {
        switch (dir) {
            case "N":
                return Direction.NORTH;
            case "E":
                return Direction.EAST;
            case "S":
                return Direction.SOUTH;
            default:
                break;
        }
        return Direction.WEST;


    }

}