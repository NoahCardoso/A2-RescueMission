package ca.mcmaster.se2aa4.island.teamXXX;

enum Direction{
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");
    private final String message;  // Field to store the string

    Direction(String message) {  // Constructor
        this.message = message;
    }

    public String toString() {  // Getter method
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