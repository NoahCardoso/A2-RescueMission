package ca.mcmaster.se2aa4.island.teamXXX;

abstract class Tile{

    private TileType type;

    Tile(TileType type){
        this.type = type;
    }

    public TileType getTileType(){
        return this.type;
    }

}