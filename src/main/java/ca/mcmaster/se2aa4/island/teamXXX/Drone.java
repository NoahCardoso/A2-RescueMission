package ca.mcmaster.se2aa4.island.teamXXX;

class Drone{

    private int x;
    private int y;
    private int battery;
    private char dir; 

    Drone(int battery){
        this.battery = battery;
    }

    public void setBattery(int battery){
        this.battery = battery;
    }

    public int getBattery(){
        return this.battery;
    }

    public int getDir(){
        return this.dir;
    }

    public void setDir(char dir){
        this.dir = dir;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }


}