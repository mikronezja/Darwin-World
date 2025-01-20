package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static agh.ics.oop.model.MapDirections.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest
{
    Globe globe = new Globe(20,20, 10, 10, 10, false, false);
    Globe globe2 = new Globe(2,2, 0, 0, 0, false, false);

    // we generate randomly the starting index so I cannot check that
    @Test
    public void testIfMoveGivesCorrectOutput()
    {
        Animal animal1 = new Animal(new Vector2d(10,10), 10, 3, 2, 2,0,0, false );

        int moves[] = animal1.getGenomeAsIntList();

        MapDirections previousDirection = animal1.getDirection();
        Vector2d previousPos = animal1.getPosition();

        int initialIndex = animal1.getInitialStartingGenomeIndex();
        int actualIndex;
        for (int i = 0; i < moves.length; i++)
        {
            actualIndex = (i + initialIndex) % moves.length;
            animal1.move(globe);

            previousDirection = previousDirection.nextByN(moves[actualIndex]);
            previousPos = previousPos.add(previousDirection.toUnitVector());

            assertEquals(previousPos, animal1.getPosition());
            assertEquals(previousDirection, animal1.getDirection());
        }



    }

    @Test
    public void testIfMoveGivesCorrectOutputOutsideOfGlobesBoundaries() {
        Animal animal1 = new Animal(new Vector2d(1, 1), 20, 3, 2, 2, 0, 0, false);

        int[] moves = animal1.getGenomeAsIntList();

        Vector2d previousPos;
        MapDirections previousDirection = animal1.getDirection();
        int initialIndex = animal1.getInitialStartingGenomeIndex();
        int currentIndex;
        for (int i = 0; i < 20; ++i) {

            currentIndex = (i + initialIndex) % moves.length;

            previousPos = animal1.getPosition();
            previousDirection = animal1.getDirection().nextByN(moves[currentIndex]);
            animal1.move(globe2);
            if (previousPos.equals(new Vector2d(0, 0))) {
                switch (previousDirection) {
                    case WEST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 0));
                        assertEquals(animal1.getDirection(), WEST);
                    }
                    case SOUTH_WEST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 0));
                        assertEquals(animal1.getDirection(), NORTH_EAST);
                    }
                    case SOUTH -> {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 0));
                        assertEquals(animal1.getDirection(), NORTH);
                    }
                    case SOUTH_EAST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 0));
                        assertEquals(animal1.getDirection(), NORTH_WEST);
                    }
                }

            } else if (previousPos.equals(new Vector2d(0, 1))) {
                switch (previousDirection) {
                    case WEST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 1));
                        assertEquals(animal1.getDirection(), WEST);
                    }
                    case NORTH_WEST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 1));
                        assertEquals(animal1.getDirection(), SOUTH_EAST);
                    }
                    case NORTH -> {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 1));
                        assertEquals(animal1.getDirection(), SOUTH);
                    }
                    case NORTH_EAST ->
                    {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 1));
                        assertEquals(animal1.getDirection(), SOUTH_WEST);
                    }
                }

            } else if (previousPos.equals(new Vector2d(1, 0))) {
                switch (previousDirection) {
                    case EAST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 0));
                        assertEquals(animal1.getDirection(), EAST);
                    }
                    case SOUTH_EAST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 0));
                        assertEquals(animal1.getDirection(), NORTH_WEST);
                    }
                    case SOUTH -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 0));
                        assertEquals(animal1.getDirection(), NORTH);
                    }
                    case SOUTH_WEST ->
                    {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 0));
                        assertEquals(animal1.getDirection(), NORTH_EAST);
                    }
                }
            } else if (previousPos.equals(new Vector2d(1, 1))) {
                switch (previousDirection) {
                    case NORTH -> {
                        assertEquals(animal1.getPosition(), new Vector2d(1, 1));
                        assertEquals(animal1.getDirection(), SOUTH);
                    }
                    case NORTH_EAST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 1));
                        assertEquals(animal1.getDirection(), SOUTH_WEST);
                    }
                    case EAST -> {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 1));
                        assertEquals(animal1.getDirection(), EAST);
                    }
                    case NORTH_WEST ->
                    {
                        assertEquals(animal1.getPosition(), new Vector2d(0, 1));
                        assertEquals(animal1.getDirection(), SOUTH_EAST);
                    }
                }
            } else {
                fail();
            }
        }
    }

    @Test
    public void testingIfAddDescendantsToAllParentsWorksAsExpected()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );
        Animal animal2 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );

        Animal animal3 = animal1.reproduce(animal2);
        assertTrue(animal1.getDescendantsNumber() == 1);
        assertTrue(animal2.getDescendantsNumber() == 1);
        assertTrue(animal3.getDescendantsNumber() == 0);

        Animal animal4 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );

        Animal animal5 = animal4.reproduce(animal3);
        assertTrue(animal1.getDescendantsNumber() == 2);
        assertTrue(animal2.getDescendantsNumber() == 2);
        assertTrue(animal3.getDescendantsNumber() == 1);
        assertTrue(animal4.getDescendantsNumber() == 1);
        assertTrue(animal5.getDescendantsNumber() == 0);
    }
}
