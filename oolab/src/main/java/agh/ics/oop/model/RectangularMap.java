package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {

    private Map<Vector2d, Animal> animals = new HashMap<>();
    private final Vector2d upperRightMapCorner;
    private final Vector2d lowerLeftMapCorner = new Vector2d(0, 0);
    // Szerokość i wysokość mapy to odpowiednio x i y UPPER_RIGHT_MAP_CORNER zwiększone o 1
    private MapVisualizer visualizer = new MapVisualizer(this);

    public RectangularMap(int width, int height) {
        upperRightMapCorner = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (canMoveTo(position)&&!isOccupied(position)) {
            animals.put(animal.getPosition(), animal);
            return true;
        }
        return false;
    }

    @Override
    public void move(Animal animal, MoveDirections direction) {
        if (objectAt(animal.getPosition()).equals(animal)) {
            animals.remove(animal.getPosition());
            animal.move(direction,this);
            animals.put(animal.getPosition(), animal);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public Animal objectAt(Vector2d position) {
        if (isOccupied(position)) {
            return animals.get(position);
        }
        return null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightMapCorner) && position.follows(lowerLeftMapCorner) && !isOccupied(position);
    }

    @Override
    public String toString() {
        return visualizer.draw(lowerLeftMapCorner,upperRightMapCorner);
    }

}
