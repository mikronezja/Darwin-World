package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionsTest {

    @Test
    void checkIfNextMethodWriteNextDirection() {
        assertEquals(MapDirections.EAST, MapDirections.NORTH.next());
        assertEquals(MapDirections.SOUTH, MapDirections.EAST.next());
        assertEquals(MapDirections.WEST, MapDirections.SOUTH.next());
        assertEquals(MapDirections.NORTH, MapDirections.WEST.next());
    }

    @Test
    void checkIfPreviousMethodWritePreviousDirection() {
        assertEquals(MapDirections.WEST, MapDirections.NORTH.previous());
        assertEquals(MapDirections.NORTH, MapDirections.EAST.previous());
        assertEquals(MapDirections.EAST, MapDirections.SOUTH.previous());
        assertEquals(MapDirections.SOUTH, MapDirections.WEST.previous());
    }



    @Test
    void changeFromEnumToString(){
        assertEquals("Północ", MapDirections.NORTH.toString());
        assertEquals("Wschód", MapDirections.EAST.toString());
        assertEquals("Południe", MapDirections.SOUTH.toString());
        assertEquals("Zachód", MapDirections.WEST.toString());
    }

}