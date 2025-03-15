package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class POI{

    //100 max size

    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<int[]> locations = new ArrayList<>(); 
    private String name;

    POI(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addPOI(String id, int x, int y){
        ids.add(id);
        int[] loc = {x, y};
        locations.add(loc);
    }

    public String getPOI(int index){
        return ids.get(index);
    }

    public int[] getPOILocation(String id) {
        if (ids.contains(id)) {
            int i = ids.indexOf(id);
            return locations.get(i);
        }

        throw new NoSuchElementException();
    }

    public ArrayList getAllPOIs() {
        return (ArrayList)ids.clone();
    }

    public String getClosestPOI(int x, int y) {
        if (ids.isEmpty()) {
            throw new NoSuchElementException();
        }

        String closest = ids.get(0);
        double shortestDist = -1;
        double dist;

        for (int i = 0; i < locations.size(); i++) {
            int[] loc = locations.get(i);
            dist = Math.sqrt(Math.pow((loc[0]-x), 2) + Math.pow((loc[1]-y), 2));

            if (shortestDist == -1 || dist < shortestDist) {
                shortestDist = dist;
                closest = ids.get(i);
            }
        }

        return closest;
    }

}