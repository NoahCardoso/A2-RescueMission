package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Queue;

import org.json.JSONObject;

class Drone{

    private int x = 0;
    private int y = 0;
    private int battery;
    private Direction lastDir;
    private Direction dir;
    private int lastScan = 0;

    private Queue<Action> moveQueue;
    private Action previousAction;

    private SearchModule sm;
    private JSONObject results;

    Drone(int battery, Direction dir){
        this.battery = battery;
        this.dir = dir;
        this.sm = new SearchModule();
    }

    public JSONObject getNextMove(){

        
        if(moveQueue.isEmpty()){
            if (sm.getInitializeStatus() == false) {
                sm.initializeInternalMap(this.moveQueue,this.x,this.y,this.results);
            } else if (sm.getBuildStatus() == false) {
                sm.buildInternalMap(this.moveQueue,this.x,this.y,results,this.dir);
            } else if (sm.getScanningDirection() == Direction.EAST) {
                // logger.info("{}", map.displayMap());
                // this.moveQueue.add(new JSONObject().put("action", "stop"));
                sm.scanEast(this.moveQueue,this.x,this.y,this.results,this.dir);
            } else {
                sm.scanWest(this.moveQueue,this.x,this.y,this.results,this.dir);
            }
        }

        Action currentAction = this.moveQueue.remove();

        if(currentAction instanceof Fly){
            fly();
        }else if(currentAction instanceof Heading){
            heading(Direction.fromString(currentAction.getJSON().getString("direction")));
        }
        
        this.previousAction = currentAction;
        return currentAction.getJSON();
    }

    public void updateResults(JSONObject info){
        this.results = info;
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

    public void heading(Direction dir){
        fly();
        setDir(dir);
        fly();
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