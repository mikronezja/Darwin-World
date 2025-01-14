package agh.ics.oop.model;

public enum MapDirections {
    NORTH(new Vector2d(0, 1), "^"),
    NORTH_EAST(new Vector2d(1, 1), "◥"),
    EAST(new Vector2d(1, 0), ">"),
    SOUTH_EAST(new Vector2d(1, -1), "◢"),
    SOUTH(new Vector2d(0, -1), "v"),
    SOUTH_WEST(new Vector2d(-1, -1), "◣"),
    WEST(new Vector2d(-1, 0), "<"),
    NORTH_WEST(new Vector2d(-1, 1), "◤");

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
        return MapDirections.values()[(ordinal() + 1) % 8];
    }

    public MapDirections previous() {
        return MapDirections.values()[(ordinal() + 7) % 8];
    }

    public MapDirections nextByN(int n) {
        return MapDirections.values()[(ordinal() + n) % 8];
    }

    public Vector2d toUnitVector() {
        return this.unitVector;
    }
}