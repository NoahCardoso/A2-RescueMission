package ca.mcmaster.se2aa4.island.team013;

abstract class Tile{

    private TileType type;

    Tile(TileType type){
        this.type = type;
    }

    public TileType getTileType(){
        return this.type;
    }

}