package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GlobeTest {

    Globe globe = new Globe(5,5, 25, 10, 10, false);
    Globe globe1 = new Globe(5,5, 4, 10, 5, false);
    Globe globe2 = new Globe(5,5, 0, 10, 5, false);


    @Test
    void checkIfGlobePlacesExactlyAsManyAnimalsAsIWant() {
        for (int i = 0; i < 40; i++)
        {
            try
            {
            globe.place( new Animal( new Vector2d((int)Math.random()*5,(int)Math.random()*5),3, 2, 2,2, 2,2, false ));
            } catch (IncorrectPositionException e)
            {
                e.printStackTrace();
                fail("IncorrectPositionException");
            }
        }

        int counter = 0;
        for (Animal animal : globe.getAnimalsList())
        {
            counter++;
        }
        assertEquals(40, counter);
    }

    @Test
    void placeShouldNotPutTheSameAnimalTwice()
    {
        Animal animal1 = new Animal( new Vector2d((int)Math.random()*5,(int)Math.random()*5),3, 2, 2,2, 2,2, false );

        try {
            globe.place(animal1);
            globe.place(animal1);
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }

        assertTrue(globe.getAnimalsList().size() == 1);
    }

    @Test
    void placeShouldThrowAnErrorIfThePositionIsIncorrect()
    {
        Animal animal1 = new Animal( new Vector2d(6,6) ,3, 2, 2,2, 2,2, false );
        try {
            globe.place(animal1);
            fail("Animal has been placed when it should not be");
        } catch (IncorrectPositionException e) {
            // works fine !
        }
    }


    @Test
    void objectsAtShouldReturnAllTheElementsThatAreInGlobe() {
        Vector2d position1 = new Vector2d(1,1);
        Vector2d position2 = new Vector2d(2,2);
        Vector2d position3 = new Vector2d(3,3); // only plants should grow here
        try {
            globe.place( new Animal( position1,3, 2, 2,2, 2,2, false ));
            globe.place( new Animal( position2,3, 2, 2,2, 2,2, false ));
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }

        /// at each and every position there is a plant for this tests sake

        int position1Counter = 0;
        int position2Counter = 0;
        int position3Counter = 0;
        for (WorldElement worldElement : globe.getElements())
        {
            if (worldElement.getPosition().equals(position1))
            {
                position1Counter++;
            }
            else if (worldElement.getPosition().equals(position2))
            {
                position2Counter++;
            }
            else if (worldElement.getPosition().equals(position3))
            {
                position3Counter++;
            }
        }
        assertTrue(position1Counter == 2);
        assertTrue(position2Counter == 2);
        assertTrue(position3Counter == 1);
    }

    @Test
    void killAnimalShouldRemoveAnimalFromEveryListInGlobe() {
       Vector2d position1 = new Vector2d(1,1);
       Animal animal1 = new Animal(position1, 3, 2, 2, 2, 2, 2, false);
       try {
           globe.place(animal1);
       } catch (IncorrectPositionException e) {
           e.printStackTrace();
       }
      assertTrue(globe.getAnimalsList().size() == 1);

       globe.killAnimal(animal1);
       assertTrue(globe.getAnimalsList().size() == 0);

       for ( WorldElement worldElement : globe.getElements() )
       {
           if (worldElement instanceof Animal)
           {
               fail();
           }
       }
    }


    @Test
    void reproducingFunctionShouldProduceAnAnimalIfEnergyGreaterThanMinimum()
    {
        Vector2d position1 = new Vector2d(1,1);

        Animal animal1 = new Animal(position1, 3, 5, 2, 2, 2, 2, false);
        Animal animal2 = new Animal(position1, 3, 4, 2, 2, 2, 2, false);

        try
        {
            globe.place(animal1);
            globe.place(animal2);
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }
        globe.animalsReproducing();
        assertTrue(globe.getAnimalsList().size() == 3);
    }


    @Test
    void reproducingFunctionShouldProduceAnAnimalIfEnergyEqualToMinimum()
    {
        Vector2d position1 = new Vector2d(1,1);

        Animal animal1 = new Animal(position1, 3, 2, 2, 2, 2, 2, false);
        Animal animal2 = new Animal(position1, 3, 2, 2, 2, 2, 2, false);

        try
        {
            globe.place(animal1);
            globe.place(animal2);
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }
        globe.animalsReproducing();
        assertTrue(globe.getAnimalsList().size() == 3);
    }

    @Test
    void reproducingFunctionShouldNotProduceAnAnimalIfEnergyLesserThanMinimum()
    {
        Vector2d position1 = new Vector2d(1,1);

        Animal animal1 = new Animal(position1, 3, 1, 2, 2, 2, 2, false);
        Animal animal2 = new Animal(position1, 3, 1, 2, 2, 2, 2, false);

        try
        {
            globe.place(animal1);
            globe.place(animal2);
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }
        globe.animalsReproducing();
        assertTrue(globe.getAnimalsList().size() == 2);
    }


    @Test
    void canMoveToShouldReturnFalseIfPositionOnTheRight()
    {
        Boundary boundary = globe.getCurrentBounds();
        assertFalse(globe.canMoveTo(new Vector2d(boundary.upperRightCorner().getX(),boundary.upperRightCorner().getY() + 1)));
        assertFalse(globe.canMoveTo(new Vector2d(boundary.upperRightCorner().getX()+1,boundary.upperRightCorner().getY())));
        assertFalse(globe.canMoveTo(new Vector2d(boundary.upperRightCorner().getX()+1,(int)(boundary.upperRightCorner().getY() + 1 / 2) )));
    }

    @Test
    void canMoveToShouldReturnFalseIfPositionOnTheLeft()
    {
        Boundary boundary = globe.getCurrentBounds();

        assertFalse(globe.canMoveTo(new Vector2d(-1,0)));
        assertFalse(globe.canMoveTo(new Vector2d(-1,0)));
        assertFalse(globe.canMoveTo(new Vector2d(boundary.lowerLeftCorner().getX() - 1,boundary.upperRightCorner().getY())));
        assertFalse(globe.canMoveTo(new Vector2d(boundary.lowerLeftCorner().getX() - 1,(int)(boundary.upperRightCorner().getY()/2) )));
    }

    @Test
    void canMoveToShouldReturnFalseIfPositionOnTheTop()
    {
        Boundary boundary = globe.getCurrentBounds();

        assertFalse(globe.canMoveTo(new Vector2d(0,boundary.upperRightCorner().getY() + 1)));
        assertFalse(globe.canMoveTo(new Vector2d(2,boundary.upperRightCorner().getY() + 1)));
    }


    @Test
    void canMoveToShouldReturnFalseIfPositionOnTheBottom()
    {
        Boundary boundary = globe.getCurrentBounds();

        assertFalse(globe.canMoveTo(new Vector2d(0,boundary.lowerLeftCorner().getY() - 1)));
        assertFalse(globe.canMoveTo(new Vector2d(2,boundary.lowerLeftCorner().getY() - 1)));
    }

    @Test
    void canMoveToShouldReturnTrueIfPositionIsInBounds() {
        Boundary boundary = globe.getCurrentBounds();

        // it is somewhere in the middle of map
        // our map has width = 10
        // heigth = 10
        // in this test
        assertTrue(globe.canMoveTo(new Vector2d(2, 3)));
    }

    @Test
    void growPlantsShouldGrowAllThePlantsIfThereIsSpace()
    {
        globe1.growPlants();
        assertTrue(globe1.getPlantsList().size() == 9);
    }

    @Test
    void growPlantsShowNotGrowMorePlantsThanThereIsSpace()
    {
        globe.growPlants();
        assertTrue(globe.getPlantsList().size() == 25);
    }

    @Test
    void eatingPlantsShouldGiveEnergyToAnimals()
    {
        Vector2d position1 = new Vector2d(1,1);

        Animal animal1 = new Animal(position1, 3, 1, 2, 2, 2, 2, false);

        assertTrue(animal1.getEnergy() == 1);
        // in this case plants should give 10 energy to animals

        try {
            globe.place(animal1);
        }
        catch (IncorrectPositionException e) { e.printStackTrace();
        }
        globe.eatingPlants();

        assertTrue(animal1.getEnergy() == 11);
    }

    @Test
    void canMoveToShouldReturnTrueIfPositionInTheBoundaries() {
        Boundary boundary = globe.getCurrentBounds();

        // in the corners
        assertTrue(globe.canMoveTo(new Vector2d(boundary.upperRightCorner().getX(),boundary.upperRightCorner().getY())));
        assertTrue(globe.canMoveTo(new Vector2d(boundary.lowerLeftCorner().getX(),boundary.lowerLeftCorner().getY())));
        assertTrue(globe.canMoveTo(new Vector2d(boundary.upperRightCorner().getX(),boundary.lowerLeftCorner().getY())));
        assertTrue(globe.canMoveTo(new Vector2d(boundary.lowerLeftCorner().getX(),boundary.upperRightCorner().getY())));

        // on the boudaries but not directly on corners
        assertTrue(globe.canMoveTo(new Vector2d(0,boundary.lowerLeftCorner().getY())));
        assertTrue(globe.canMoveTo(new Vector2d(boundary.upperRightCorner().getX(),2)));
    }

    @Test
    void occupiedPositionsShouldReturnPositionsOccupiedByPlantsAndAnimals()
    {
        // from plants only
        assertTrue(globe.occupiedPositions().size() == 25);
        assertTrue(globe1.occupiedPositions().size() == 4);

        // also from animals
        Vector2d position1 = new Vector2d(1,1);
        Animal animal1 = new Animal(position1, 3, 1, 2, 2, 2, 2, false);
        try
        {
            globe2.place(animal1);
        }
        catch (IncorrectPositionException e) { e.printStackTrace();}
        assertTrue(globe2.occupiedPositions().size() == 1);

        // from animals and plants
        try
        {
            globe.place(animal1);
        }catch (IncorrectPositionException e) { e.printStackTrace();}
        assertTrue(globe.occupiedPositions().size() == 25);

    }
}