package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionForPlantsGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Globe extends AbstractWorldMap{

    private Vector2d upperRightMapCorner;
    private Vector2d lowerLeftMapCorner = new Vector2d(0, 0);
    private Vector2d upperRightEquatorCorner;
    private Vector2d lowerLeftEquatorCorner;
    private Map<Vector2d, Plant> plants = new HashMap<>();
    private RandomPositionForPlantsGenerator positionForPlantsGenerator;
    protected Map<Vector2d, List<Animal>> animals = new HashMap<>();

    public Globe(int height, int width, int howManyPlants, int howManyEnergyFromPlants) {
        this.upperRightMapCorner = new Vector2d(width-1, height-1);
        this.lowerLeftEquatorCorner = new Vector2d(0, ((height/5)*2)+1);
        this.upperRightEquatorCorner = new Vector2d(width-1, ((height/5)*2)+((height+2)/5));
        this.positionForPlantsGenerator = new RandomPositionForPlantsGenerator(height,width, upperRightEquatorCorner, lowerLeftEquatorCorner);
        for (int i=0; i<howManyPlants; i++) {
            Vector2d position = positionForPlantsGenerator.generatePosition();
            if (position == null){
                break;
            }
            Plant plant = new Plant(howManyEnergyFromPlants, position);
            plants.put(position, plant);
        }
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeftMapCorner, upperRightMapCorner);
    }
}
