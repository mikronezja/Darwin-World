package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobeTest {

    Globe globe = new Globe(10,10, 10, 10, 10, false);

    @Test
    void checkIfGlobePlacesExactlyAsManyAnimalsAsIWant() {
        for (int i = 0; i < 40; i++)
        {
            try
            {
            globe.place( new Animal( new Vector2d((int)Math.random()*10,(int)Math.random()*10),3, 2, 2,2, 2,2, false ));
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
    void moveRemovesExactlyThisAnimalFromPosition()
    {

    }

    @Test
    void objectsAt() {
    }

    @Test
    void killAnimal() {
    }

    @Test
    void reproduce()
    {

    }

    @Test
    void canMoveTo() {

    }
}