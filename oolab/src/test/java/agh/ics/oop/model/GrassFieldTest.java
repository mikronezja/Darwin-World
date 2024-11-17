package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

        @Test
        void placeOneAnimalAndCheckIfItIsThere(){
            GrassField field = new GrassField(0);
            field.place(new Animal(new Vector2d(2,2)));
            assertTrue(field.isOccupied(new Vector2d(2,2)));
        }

        @Test
        void checkIfNothingIsOnfield(){
            GrassField field = new GrassField(0);
            for (int i=0;i<5;i++) {
                for (int j = 0; j < 5; j++) {
                    Vector2d vector =new Vector2d(i,j);
                    assertFalse(field.isOccupied(vector));
                    assertNull(field.objectAt(vector));
                }
            }
        }

        @Test
        void checkIfObjectIsThere(){
            GrassField field = new GrassField(10);
            Animal animal = new Animal(new Vector2d(2,2));
            field.place(animal);
            assertEquals(animal, field.objectAt(new Vector2d(2,2)));
        }


        @Test
        void placeMultipleAnimalOnOnePlace(){
            GrassField field = new GrassField(10);
            Animal Stefan = new Animal(new Vector2d(2,2));
            field.place(Stefan);
            field.place(new Animal(new Vector2d(2,2)));
            field.place(new Animal(new Vector2d(2,2)));

            assertEquals(Stefan, field.objectAt(new Vector2d(2,2)));
        }

        @Test
        void checkPossibilityToMove(){
            GrassField field = new GrassField(10);
            assertTrue(field.canMoveTo(new Vector2d(100,-100)));
            assertTrue(field.canMoveTo(new Vector2d(-100,100)));
            Animal animalFirst =new Animal(new Vector2d(2,2));
            if (field.place(animalFirst)){
                assertFalse(field.canMoveTo(new Vector2d(2,2)));
            }

        }

        @Test
        void movingAround(){
            GrassField field = new GrassField(10);
            Animal leonardo = new Animal(new Vector2d(2,3));
            Animal donatello = new Animal(new Vector2d(3,2));
            Animal michelangelo = new Animal(new Vector2d(2,1));
            Animal raphael = new Animal(new Vector2d(1,2));
            field.place(leonardo);
            field.place(donatello);
            field.place(michelangelo);
            field.place(raphael);

            field.move(leonardo,MoveDirections.FORWARD);

            field.move(donatello,MoveDirections.RIGHT);
            field.move(donatello,MoveDirections.FORWARD);

            field.move(michelangelo,MoveDirections.BACKWARD);

            field.move(raphael,MoveDirections.LEFT);
            field.move(raphael,MoveDirections.FORWARD);

            assertEquals(leonardo, field.objectAt(new Vector2d(2,4)));
            assertEquals(donatello, field.objectAt(new Vector2d(4,2)));
            assertEquals(michelangelo, field.objectAt(new Vector2d(2,0)));
            assertEquals(raphael, field.objectAt(new Vector2d(0,2)));
        }
}