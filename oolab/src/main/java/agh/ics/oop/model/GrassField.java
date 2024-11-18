package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

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
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (Objects.equals(null, super.objectAt(position))) {
            if (grasses.containsKey(position)){
                return grasses.get(position);
            }
        }
        return super.objectAt(position);
    }

    @Override
    public String toString() {
        calculateBoundaries();
        return visualizer.draw(lowerLeftMapCorner,upperRightMapCorner);
    }

    private void calculateBoundaries(){
        for (Vector2d position : animals.keySet()){
            upperRightMapCorner = position.upperRight(upperRightMapCorner);
            lowerLeftMapCorner = position.lowerLeft(lowerLeftMapCorner);
        }
    }

    public List<WorldElement> getElements(){
        List<WorldElement> elements = super.getElements();
        elements.addAll((grasses.values()));
        return elements;
    }

}
