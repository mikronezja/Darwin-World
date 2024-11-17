package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {

    protected Map<Vector2d, Animal> animals = new HashMap<>();
    protected MapVisualizer visualizer = new MapVisualizer(this);

    public void move(Animal animal, MoveDirections direction) {
        if (objectAt(animal.getPosition()).equals(animal)) {
            animals.remove(animal.getPosition());
            animal.move(direction,this);
            animals.put(animal.getPosition(), animal);
        }
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position);
    }


}
