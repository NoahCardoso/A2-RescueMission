package ca.mcmaster.se2aa4.island.teamXXX;

class Drone{

    private int x = 0;
    private int y = 0;
    private int battery;
    private Direction lastDir;
    private Direction dir;
    private int lastScan = 0;

    //Not used yet
    private POI creek = new POI("creek");
    private POI eSite = new POI("Emergency Site");

    Drone(int battery, Direction dir){
        this.battery = battery;
        this.dir = dir;
    }

    public void setBattery(int battery){
        this.battery = battery;
    }

    public void subBattery(int val) {
        this.battery -= val;
    }

    public int getBattery(){
        return this.battery;
    }

    public Direction getDir(){
        return this.dir;
    }

    public void setDir(Direction dir){
        this.lastDir = this.dir;
        this.dir = dir;
    }

    public Direction getLastDir(){
        return this.lastDir;
    }

    public int getLastScan(){
        return this.lastScan;
    }

    public void incLastScan(){
        this.lastScan++;
    }

    public void resetLastScan() {
        this.lastScan = 0;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    private void setX(int x){
        this.x = x;
    }

    private void setY(int y){
        this.y = y;
    }

    public void decreaseBattery(int bat){
        this.battery -= bat;
    }

    public void fly(){
        //Assumes 0,0 is top left
        switch (dir) {
            case NORTH:
                setY(y-1);
                break;
            case EAST:
                setX(x+1);
                break;
            case SOUTH:
                setY(y+1);
                break;
            case WEST:
                setX(x-1);
                break;
            default:
                System.out.println("No known direction");
                break;
        }
    }

    //f = forward, r = right, l = left, b = backward
    private char getRelativeDir(char echoDir){
        switch (dir) {
            case NORTH:
                switch (echoDir){
                    case 'N':
                        return 'f';
                    case 'E':
                        return 'r';
                    case 'W':
                        return 'l';
                    default:
                        return 'b';
                }
            case EAST:
                switch (echoDir){
                    case 'E':
                        return 'f';
                    case 'S':
                        return 'r';
                    case 'N':
                        return 'l';
                    default:
                        return 'b';
                } 
            case SOUTH:
                switch (echoDir){
                    case 'S':
                        return 'f';
                    case 'W':
                        return 'r';
                    case 'E':
                        return 'l';
                    default:
                        return 'b';
                }
            case WEST:
                switch (echoDir){
                    case 'W':
                        return 'f';
                    case 'N':
                        return 'r';
                    case 'S':
                        return 'l';
                    default:
                        return 'b';
                }
            default:
                System.out.println("No known direction");
                return 'b';
        }
    }


}