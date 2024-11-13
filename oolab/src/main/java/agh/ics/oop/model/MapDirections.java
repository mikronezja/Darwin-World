package agh.ics.oop.model;

public enum MapDirections {
    NORTH(new Vector2d(0,1), "^"),
    EAST(new Vector2d(1,0), ">"),
    SOUTH(new Vector2d(0,-1), "v"),
    WEST(new Vector2d(-1,0), "<");

    private final Vector2d unitVector;
    private final String direction;

    private MapDirections(Vector2d unitVector, String direction) {
        this.unitVector = unitVector;
        this.direction = direction;
    }

    public String toString() {
        return this.direction;
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
