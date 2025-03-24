package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class POIProcessor{

    private ArrayList<POI> creekList = new ArrayList<>();
    private POI emergencySite;

    public void addCreek(String id, int x, int y){
        creekList.add(new POI(id,x,y));
    }

    public void addEmergencySite(String id,int x,int y){
        this.emergencySite = new POI(id,x,y);
    }
    
    public String getClosestPOI() {
        if (creekList.isEmpty()) {
            throw new NoSuchElementException("creek");
        }

        if (emergencySite == null){
            throw new NoSuchElementException("site");
        }

        POI closest = creekList.get(0);
        double shortestDist = -1;
        double dist;

        for (int i = 0; i < creekList.size(); i++) {

            int tempX = creekList.get(i).getX();
            int tempY = creekList.get(i).getY();
            dist = Math.sqrt(Math.pow((tempX-emergencySite.getX()), 2) + Math.pow((tempY-emergencySite.getY()), 2));

            if (shortestDist == -1 || dist < shortestDist) {
                shortestDist = dist;
                closest = creekList.get(i);
            }
        }

        return closest.getId();
    }

}