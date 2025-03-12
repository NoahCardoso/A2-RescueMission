package ca.mcmaster.se2aa4.island.teamXXX;

class Drone{

    private int x = 0;
    private int y = 0;
    private int battery;
    private char lastDir;
    private char dir;
    private int lastScan = 0;

    Drone(int battery, char dir){
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

    public char getDir(){
        return this.dir;
    }

    public void setDir(char dir){
        this.lastDir = this.dir;
        this.dir = dir;
    }

    public char getLastDir(){
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

    public void updatePosition() {
        if (dir == 'E'){
            x++;
        } else if (dir == 'W') {
            x--;
        } else if (dir == 'N') {
            y--;
        } else {
            y++;
        }
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