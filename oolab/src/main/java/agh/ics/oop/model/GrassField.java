package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GrassField extends AbstractWorldMap {

    private Map<Vector2d, Grass> grasses = new HashMap<>();

    private Vector2d upperRightMapCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private Vector2d lowerLeftMapCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public GrassField(int howManyGrasses) {
        int maxWidth = (int)Math.ceil(Math.sqrt(howManyGrasses * 10));
        int maxHeight = (int)Math.ceil(Math.sqrt(howManyGrasses * 10));
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxWidth, maxHeight, howManyGrasses);
        for(Vector2d grassPosition : randomPositionGenerator) {
            upperRightMapCorner = grassPosition.upperRight(upperRightMapCorner);
            lowerLeftMapCorner = grassPosition.lowerLeft(lowerLeftMapCorner);
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (canMoveTo(position) || (!canMoveTo(position) && objectAt(position).getClass()== Grass.class)) {
                    animals.put(animal.getPosition(), animal);
                    upperRightMapCorner = position.upperRight(upperRightMapCorner);
                    lowerLeftMapCorner = position.lowerLeft(lowerLeftMapCorner);
                    return true;
        }
        return false;
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (animals.containsKey(position)){
            return animals.get(position);
        }
        if (grasses.containsKey(position)){
            return grasses.get(position);
        }
        return null;
    }

    @Override
    public String toString() {
        return visualizer.draw(lowerLeftMapCorner,upperRightMapCorner);
    }

}
