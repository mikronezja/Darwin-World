package agh.ics.oop.model;

import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected Map<Vector2d, Animal> animals = new HashMap<>();
    protected MapVisualizer visualizer = new MapVisualizer(this);
    protected List<MapChangeListener> observators = new ArrayList<>();
    protected UUID id = UUID.randomUUID();

    public void place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();
        if (!canMoveTo(position)) {
            throw new IncorrectPositionException(position);
        }else {
            animals.put(animal.getPosition(), animal);
            //mapChanged("Zwierzę zotało położone");
        }
    }

    public void move(Animal animal, MoveDirections direction) {
        if (objectAt(animal.getPosition()).equals(animal)) {
            animals.remove(animal.getPosition());
            //animal.move(direction, this);
            animals.put(animal.getPosition(), animal);
            //mapChanged("Zwierze poruszyło sie");
        }
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }

    public WorldElement objectAt(Vector2d position) {
        if (animals.containsKey(position)) {
            return animals.get(position);
        }
        return null;
    }

    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    public abstract Boundary getCurrentBounds();

    public final String toString() {
        Boundary boundaries = getCurrentBounds();
        return visualizer.draw(boundaries.lowerLeftCorner(),boundaries.upperRightCorner());
    }

    public void addObservator(MapChangeListener observator){
        observators.add(observator);
    }

    public void removeObservator(MapChangeListener observator){
        observators.remove(observator);
    }

//    protected void mapChanged(String message) {
//        for (MapChangeListener observator : observators) {
//            observator.mapChanged(this, message);
//        }
//    }

    public UUID getID(){
        return id;
    }
}
