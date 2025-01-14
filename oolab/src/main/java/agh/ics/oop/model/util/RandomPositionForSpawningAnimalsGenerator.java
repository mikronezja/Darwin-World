package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.Random;

public class RandomPositionForSpawningAnimalsGenerator {

    private final int maxWidthValue;
    private final int maxHeightValue;
    private Random rand = new Random();

    public RandomPositionForSpawningAnimalsGenerator(int maxWidthValue, int maxHeightValue) {
        this.maxWidthValue = maxWidthValue;
        this.maxHeightValue = maxHeightValue;
    }

    public Vector2d getRandomPosition(){
        int x = rand.nextInt(maxWidthValue);
        int y = rand.nextInt(maxHeightValue);
        return new Vector2d(x, y);
    }

}
