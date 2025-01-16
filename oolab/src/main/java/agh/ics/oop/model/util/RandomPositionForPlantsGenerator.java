package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionForPlantsGenerator{


    private final Vector2d upperRightEquatorCorner;
    private final Vector2d lowerLeftEquatorCorner;
    private final List<Vector2d> positionsLessDesirable = new ArrayList<Vector2d>();
    private int lessDesirableGenerationRange;
    private final List<Vector2d> positionsMoreDesirable = new ArrayList<Vector2d>();
    private int moreDesirableGenerationRange;
    private Random rand = new Random();

    public RandomPositionForPlantsGenerator(int height, int width, Vector2d upperRightEquatorCorner, Vector2d lowerLeftEquatorCorner) {
        this.upperRightEquatorCorner = upperRightEquatorCorner;
        this.lowerLeftEquatorCorner = lowerLeftEquatorCorner;
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                Vector2d position = new Vector2d(i,j);
                if (position.follows(lowerLeftEquatorCorner) && position.precedes(upperRightEquatorCorner)){
                    positionsMoreDesirable.add(position);
                }
                else{
                    positionsLessDesirable.add(position);
                }
            }
        }
        lessDesirableGenerationRange = positionsLessDesirable.size();
        moreDesirableGenerationRange = positionsMoreDesirable.size();
    }

    //Posiible null because of no more space for plants
    // Make if in Globe to avoid plaicing in null position
    public Vector2d generatePosition() {
        int randomNumber = rand.nextInt(10);
        Vector2d position;
        if (randomNumber == 0 || randomNumber == 1){
            position = chooseFromLessDesirables();
            if (position == null){
                position = chooseFromMoreDesirables();
            }
        }
        else{
            position = chooseFromMoreDesirables();
            if (position == null){
                position = chooseFromLessDesirables();
            }
        }
        return position;
    }

    private Vector2d chooseFromLessDesirables() {
        if (lessDesirableGenerationRange == 0){
            return null;
        }
        int indexOfArray = rand.nextInt(lessDesirableGenerationRange);
        Vector2d position = positionsLessDesirable.get(indexOfArray);
        lessDesirableGenerationRange--;
        Collections.swap(positionsLessDesirable, indexOfArray, lessDesirableGenerationRange);
        positionsLessDesirable.remove(lessDesirableGenerationRange);
        return position;
    }

    private Vector2d chooseFromMoreDesirables(){
        if (moreDesirableGenerationRange == 0){
            return null;
        }
        int indexOfArray = rand.nextInt(moreDesirableGenerationRange);
        Vector2d position = positionsMoreDesirable.get(indexOfArray);
        moreDesirableGenerationRange--;
        Collections.swap(positionsMoreDesirable, indexOfArray, moreDesirableGenerationRange);
        positionsMoreDesirable.remove(moreDesirableGenerationRange);
        return position;
    }

    public void addPositionToGenerator(Vector2d position){
        if (position.follows(lowerLeftEquatorCorner) && position.precedes(upperRightEquatorCorner)){
            positionsMoreDesirable.add(position);
            moreDesirableGenerationRange++;
        }
        else{
            positionsLessDesirable.add(position);
            lessDesirableGenerationRange++;
        }
    }

}
