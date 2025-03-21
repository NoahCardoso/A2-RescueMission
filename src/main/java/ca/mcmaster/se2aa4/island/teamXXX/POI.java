package ca.mcmaster.se2aa4.island.teamXXX;

public class POI{

    private String id;
    private int x;
    private int y;

    POI(String id,int x,int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getId(){
        return this.id;
    }
}