package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

        @Test
        void placeOneAnimalAndCheckIfItIsThere(){
            GrassField field = new GrassField(0);
            try {
                field.place(new Animal(new Vector2d(2, 2)));
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
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
            try {
                field.place(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
            assertEquals(animal, field.objectAt(new Vector2d(2,2)));
        }


        @Test
        void placeMultipleAnimalOnOnePlace(){
            GrassField field = new GrassField(10);
            Animal Stefan = new Animal(new Vector2d(2,2));
            try {
                field.place(Stefan);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
            try {
                field.place(new Animal(new Vector2d(2, 2)));
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
            try {
                field.place(new Animal(new Vector2d(2, 2)));
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }

            assertEquals(Stefan, field.objectAt(new Vector2d(2,2)));
        }

        @Test
        void checkPossibilityToMove(){
            GrassField field = new GrassField(10);
            assertTrue(field.canMoveTo(new Vector2d(100,-100)));
            assertTrue(field.canMoveTo(new Vector2d(-100,100)));
            Animal animalFirst =new Animal(new Vector2d(2,2));
            try {
                field.place(animalFirst);
                assertFalse(field.canMoveTo(new Vector2d(2,2)));
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }

        @Test
        void movingAround(){
            GrassField field = new GrassField(10);
            Animal leonardo = new Animal(new Vector2d(2,3));
            Animal donatello = new Animal(new Vector2d(3,2));
            Animal michelangelo = new Animal(new Vector2d(2,1));
            Animal raphael = new Animal(new Vector2d(1,2));
            try {
                field.place(leonardo);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
            try {
                field.place(donatello);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
            try {
                field.place(michelangelo);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
            try {
                field.place(raphael);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }


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