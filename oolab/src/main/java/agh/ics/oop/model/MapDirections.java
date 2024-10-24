package agh.ics.oop.model;

public enum MapDirections {
    NORTH(new Vector2d(0,1)),
    EAST(new Vector2d(1,0)),
    SOUTH(new Vector2d(0,-1)),
    WEST(new Vector2d(-1,0));

    private final Vector2d unitVector;

    private MapDirections(Vector2d unitVector) {
        this.unitVector = unitVector;
    }

    public String toString() {
        return switch (this){
            case NORTH -> "Północ";
            case EAST -> "Wschód";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
        };
    }
    public MapDirections next() {
        return MapDirections.values()[(ordinal() + 1) % 4];
    }
    public MapDirections previous() {
        return MapDirections.values()[(ordinal() + 3) % 4];
    }

    public Vector2d toUnitVector() {
        return this.unitVector;
    }
}
