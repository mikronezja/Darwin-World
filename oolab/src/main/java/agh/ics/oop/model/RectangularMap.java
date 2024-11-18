package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap extends AbstractWorldMap {

    private final Vector2d upperRightMapCorner;
    private final Vector2d lowerLeftMapCorner = new Vector2d(0, 0);
    // Szerokość i wysokość mapy to odpowiednio x i y UPPER_RIGHT_MAP_CORNER zwiększone o 1

    public RectangularMap(int width, int height) {
        upperRightMapCorner = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightMapCorner) && position.follows(lowerLeftMapCorner) && super.canMoveTo(position);
    }

    @Override
    public String toString() {
        return visualizer.draw(lowerLeftMapCorner,upperRightMapCorner);
    }
}
