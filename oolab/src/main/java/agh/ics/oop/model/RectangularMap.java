package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {

    private Map<Vector2d, Animal> animals = new HashMap<>();
    private final Vector2d UPPER_RIGHT_MAP_CORNER;
    private final Vector2d LOWER_LEFT_MAP_CORNER = new Vector2d(0, 0);;
    // Szerokość i wysokość mapy to odpowiednio x i y UPPER_RIGHT_MAP_CORNER zwiększone o 1

    public RectangularMap(int width, int height) {
        UPPER_RIGHT_MAP_CORNER = new Vector2d(width-1, height-1);
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
        return position.precedes(UPPER_RIGHT_MAP_CORNER) && position.follows(LOWER_LEFT_MAP_CORNER) && !isOccupied(position);
    }

    @Override
    public String toString() {
        return new MapVisualizer(this).draw(LOWER_LEFT_MAP_CORNER,UPPER_RIGHT_MAP_CORNER);
    }

}
