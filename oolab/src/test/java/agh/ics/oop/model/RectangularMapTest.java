package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    @Test
    void placeOneAnimalAndCheckIfItIsThere(){
        RectangularMap map = new RectangularMap(5,5);
        map.place(new Animal(new Vector2d(2,2)));
        assertTrue(map.isOccupied(new Vector2d(2,2)));
    }

    @Test
    void checkIfNothingIsOnMap(){
        RectangularMap map = new RectangularMap(5,5);
        for (int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                Vector2d vector =new Vector2d(i,j);
                assertFalse(map.isOccupied(vector));
                assertNull(map.objectAt(vector));
            }
        }
    }

    @Test
    void checkIfObjectIsThere(){
        RectangularMap map = new RectangularMap(5,5);
        Animal animal = new Animal(new Vector2d(2,2));
        map.place(animal);
        assertEquals(animal, map.objectAt(new Vector2d(2,2)));
    }


    @Test
    void placeMultipleAnimalOnOnePlace(){
        RectangularMap map = new RectangularMap(5,5);
        Animal Stefan = new Animal(new Vector2d(2,2));
        map.place(Stefan);
        map.place(new Animal(new Vector2d(2,2)));
        map.place(new Animal(new Vector2d(2,2)));

        assertEquals(Stefan, map.objectAt(new Vector2d(2,2)));
    }

    @Test
    void checkPossibilityToMove(){
        RectangularMap map = new RectangularMap(5,5);
        assertTrue(map.canMoveTo(new Vector2d(2,2)));
        assertFalse( map.canMoveTo(new Vector2d(-1,2)));
        assertFalse( map.canMoveTo(new Vector2d(2,-1)));
        assertFalse( map.canMoveTo(new Vector2d(5,2)));
        assertFalse( map.canMoveTo(new Vector2d(2,5)));
    }

    @Test
    void movingAround(){
        RectangularMap map = new RectangularMap(5,5);
        Animal leonardo = new Animal(new Vector2d(2,3));
        Animal donatello = new Animal(new Vector2d(3,2));
        Animal michelangelo = new Animal(new Vector2d(2,1));
        Animal raphael = new Animal(new Vector2d(1,2));
        map.place(leonardo);
        map.place(donatello);
        map.place(michelangelo);
        map.place(raphael);

        map.move(leonardo,MoveDirections.FORWARD);
        
        map.move(donatello,MoveDirections.RIGHT);
        map.move(donatello,MoveDirections.FORWARD);

        map.move(michelangelo,MoveDirections.BACKWARD);

        map.move(raphael,MoveDirections.LEFT);
        map.move(raphael,MoveDirections.FORWARD);

        assertEquals(leonardo, map.objectAt(new Vector2d(2,4)));
        assertEquals(donatello, map.objectAt(new Vector2d(4,2)));
        assertEquals(michelangelo, map.objectAt(new Vector2d(2,0)));
        assertEquals(raphael, map.objectAt(new Vector2d(0,2)));
    }
}