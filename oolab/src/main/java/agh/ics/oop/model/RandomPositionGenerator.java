package agh.ics.oop.model;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d>{

    private final int maxWidth;
    private final int maxHeight;
    private int howMany;
    private int generationRange;
    private final List<Vector2d> positions = new ArrayList<Vector2d>();
    private Random rand = new Random();

    RandomPositionGenerator(int maxWidth, int maxHeight, int howMany) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.howMany = howMany;
        generationRange = (maxWidth+1) * (maxHeight+1);
        for (int i=0; i<=maxWidth; i++){
            for (int j=0; j<=maxHeight; j++){
                positions.add(new Vector2d(i,j));
            }
        }
    }

    @Override
    public Iterator iterator() {
        return new Iterator<Vector2d>() {

            @Override
            public boolean hasNext() {
                return howMany > 0;
            }

            @Override
            public Vector2d next() {
                int randomNumber = rand.nextInt(generationRange);
                Vector2d position = positions.get(randomNumber);
                generationRange--;
                Collections.swap(positions, randomNumber, generationRange);
                positions.remove(generationRange);
                howMany--;
                return position;
            }
        };
    }
}
