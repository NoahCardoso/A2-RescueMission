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

}