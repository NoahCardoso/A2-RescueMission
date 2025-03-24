package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONObject;

public class Drone{

    private int x = 0;
    private int y = 0;
    private int battery;
    private Direction lastDir;
    private Direction dir;
    private int lastScan = 0;

    private Queue<Command> moveQueue;
    private SearchModule sm;
    private JSONObject results;

    Drone(int battery, Direction dir){
        this.battery = battery;
        this.dir = dir;
        this.sm = new SearchModule();
        this.moveQueue = new LinkedList<>();
    }

    public JSONObject getNextMove(){

        
        if(moveQueue.isEmpty()){
            if (sm.getInitializeStatus() == false) {
                sm.initializeInternalMap(this.moveQueue,this.x,this.y,this.results);
            } else if (sm.getBuildStatus() == false) {
                sm.buildInternalMap(this.moveQueue,this.x,this.y,results,this.dir);
            } else if (sm.getScanningDirection() == Direction.EAST) {
                sm.scanEast(this.moveQueue,this.x,this.y,this.results,this.dir);
            } else {
                sm.scanWest(this.moveQueue,this.x,this.y,this.results,this.dir);
            }
        }

        Command currentAction = this.moveQueue.poll();
        
        sm.setPreviousAction(currentAction);
        return currentAction.execute(this);
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
                break;
        }
    }

    
    


}