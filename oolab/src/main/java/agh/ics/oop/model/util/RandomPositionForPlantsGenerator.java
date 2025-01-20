package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionForPlantsGenerator{


    private final Vector2d upperRightMapCorner;
    private final Vector2d lowerLeftMapCorner = new Vector2d(0,0);
    private final Vector2d upperRightEquatorCorner;
    private final Vector2d lowerLeftEquatorCorner;
    private final List<Vector2d> positionsLessDesirable = new ArrayList<Vector2d>();
    private int lessDesirableGenerationRange;
    private final List<Vector2d> positionsMoreDesirable = new ArrayList<Vector2d>();
    private int moreDesirableGenerationRange;
    private Random rand = new Random();
    private final boolean ifPlantsPreferDeadBodies;
    private int deadAnimalGenerationRange;
    private final List<Vector2d> positionsWithDeadAnimals = new ArrayList<Vector2d>();

    public RandomPositionForPlantsGenerator(int height, int width, Vector2d upperRightEquatorCorner, Vector2d lowerLeftEquatorCorner, boolean ifPlantsPreferDeadBodies) {
        upperRightMapCorner = new Vector2d(height-1, width-1);
        this.upperRightEquatorCorner = upperRightEquatorCorner;
        this.lowerLeftEquatorCorner = lowerLeftEquatorCorner;
        this.ifPlantsPreferDeadBodies=ifPlantsPreferDeadBodies;
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
        if(ifPlantsPreferDeadBodies){
            int deadOrEquator = rand.nextInt(2);
            //dead = 0
            if (deadOrEquator == 0){
                if (chooseFromDeadAnimalList() == null){
                    return chooseFromMoreDesirablesList();
                }
                else{
                    return chooseFromDeadAnimalList();
                }
            }
            else{
                if (chooseFromMoreDesirablesList() == null){
                    return chooseFromDeadAnimalList();
                }
                else{
                    return chooseFromMoreDesirablesList();
                }
            }
        }
        else{
            return chooseFromMoreDesirablesList();
        }
    }

    private Vector2d chooseFromMoreDesirablesList(){
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

    private Vector2d chooseFromDeadAnimalList(){
        if (deadAnimalGenerationRange == 0){
            return null;
        }
        int indexOfArray = rand.nextInt(deadAnimalGenerationRange);
        Vector2d position = positionsWithDeadAnimals.get(indexOfArray);
        deadAnimalGenerationRange--;
        Collections.swap(positionsWithDeadAnimals, indexOfArray, deadAnimalGenerationRange);
        positionsWithDeadAnimals.remove(deadAnimalGenerationRange);
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

    public void addPositionOfDeadAnimal(Vector2d position){
        List<Vector2d> positionsToAdd = List.of(
                position,
                position.add(new Vector2d(0,1)),
                position.add(new Vector2d(1,1)),
                position.add(new Vector2d(1,0)),
                position.add(new Vector2d(1,-1)),
                position.add(new Vector2d(0,-1)),
                position.add(new Vector2d(-1,-1)),
                position.add(new Vector2d(-1,0)),
                position.add(new Vector2d(-1,1))
        );
        for (Vector2d pos: positionsToAdd){
            if (pos.follows(lowerLeftMapCorner) && pos.precedes(upperRightMapCorner)){
                positionsWithDeadAnimals.add(pos);
                deadAnimalGenerationRange++;
            }
        }
    }
}
