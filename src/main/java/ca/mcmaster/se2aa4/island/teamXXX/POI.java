package ca.mcmaster.se2aa4.island.teamXXX;

public class POI{

    //100 max size

    private String[] ids = new String[100];
    private String name;
    private int size = 0;

    POI(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addPOI(String id){
        ids[size] = id;
        size++;
    }

    public String getPOI(int index){
        return ids[index];
    }

}