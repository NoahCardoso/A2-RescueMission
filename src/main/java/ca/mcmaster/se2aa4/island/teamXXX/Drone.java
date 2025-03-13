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

    private void setX(int x){
        this.x = x;
    }

    private void setY(int y){
        this.y = y;
    }

    public void fly(){
        //Assumes 0,0 is bottom left
        switch (dir) {
            case 'N':
                setY(y+1);
                break;
            case 'E':
                setX(y-1);
                break;
            case 'S':
                setY(y-1);
                break;
            case 'W':
                setX(x-1);
                break;
            default:
                System.out.println("No known direction");
                break;
        }
    }

    public boolean echo(char echoDir){
        char heading = getRelativeDir(echoDir);
        if(heading == 'b'){
            System.out.println("Invalid echo direction");
            return false;
        }

        return true;

    }

    public void scan(){

        
    }

    //f = forward, r = right, l = left, b = backward
    private char getRelativeDir(char echoDir){
        switch (dir) {
            case 'N':
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
            case 'E':
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
            case 'S':
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
            case 'W':
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