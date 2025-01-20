package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnimalTest
{
    // move
    // addDescendantsToAllParents <- to reproduce function

    Globe globe = new Globe(20,20, 10, 10, 10, false);


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
    public void testIfMoveGivesCorrectOutputOutsideOfGlobesBoundaries()
    {

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
