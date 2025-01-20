package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionsTest {

    @Test
    void checkIfNextMethodWritesNextDirection()
    {
        assertEquals(MapDirections.NORTH.next(), MapDirections.NORTH_EAST);
        assertEquals(MapDirections.NORTH_EAST.next(), MapDirections.EAST);
        assertEquals(MapDirections.EAST.next(), MapDirections.SOUTH_EAST);
        assertEquals(MapDirections.SOUTH_EAST.next(), MapDirections.SOUTH);
        assertEquals(MapDirections.SOUTH.next(), MapDirections.SOUTH_WEST);
        assertEquals(MapDirections.SOUTH_WEST.next(), MapDirections.WEST);
        assertEquals(MapDirections.WEST.next(), MapDirections.NORTH_WEST);
        assertEquals(MapDirections.NORTH_WEST.next(), MapDirections.NORTH);
    }


    @Test
    void checkIfPreviousMethodWritePreviousDirection()
    {
        assertEquals(MapDirections.NORTH.previous(), MapDirections.NORTH_WEST);
        assertEquals(MapDirections.NORTH_WEST.previous(), MapDirections.WEST);
        assertEquals(MapDirections.WEST.previous(), MapDirections.SOUTH_WEST);
        assertEquals(MapDirections.SOUTH_WEST.previous(), MapDirections.SOUTH);
        assertEquals(MapDirections.SOUTH.previous(), MapDirections.SOUTH_EAST);
        assertEquals(MapDirections.SOUTH_EAST.previous(), MapDirections.EAST);
        assertEquals(MapDirections.EAST.previous(), MapDirections.NORTH_EAST);
        assertEquals(MapDirections.NORTH_EAST.previous(), MapDirections.NORTH);
    }

    @Test
    void changeFromEnumToString()
    {
        assertEquals(MapDirections.NORTH.toString(), "^");
        assertEquals(MapDirections.NORTH_EAST.toString(), "◥");
        assertEquals(MapDirections.EAST.toString(), ">");
        assertEquals(MapDirections.SOUTH_EAST.toString(), "◢");
        assertEquals(MapDirections.SOUTH.toString(), "v");
        assertEquals(MapDirections.SOUTH_WEST.toString(), "◣");
        assertEquals(MapDirections.WEST.toString(), "<");
        assertEquals(MapDirections.NORTH_WEST.toString(), "◤");
    }

    @Test
    void checkIfNextByNGivesCorrectValues()
    {
        MapDirections mapDirections = MapDirections.NORTH;
        assertEquals(mapDirections.ordinal(), 0);

        MapDirections newDirection = mapDirections.nextByN(5);
        assertEquals(newDirection.ordinal(), 5);

        // does it give correct output when out of the 0-7 scope
        MapDirections againNewDirection = newDirection.nextByN(5);
        assertEquals(againNewDirection.ordinal(), (5+5) % 8);
    }

}



//
//class MapDirectionsTest {
//
//    @Test
//    void checkIfNextMethodWriteNextDirection() {
//        assertEquals(MapDirections.EAST, MapDirections.NORTH.next());
//        assertEquals(MapDirections.SOUTH, MapDirections.EAST.next());
//        assertEquals(MapDirections.WEST, MapDirections.SOUTH.next());
//        assertEquals(MapDirections.NORTH, MapDirections.WEST.next());
//    }
//
//    @Test
//    void checkIfPreviousMethodWritePreviousDirection() {
//        assertEquals(MapDirections.WEST, MapDirections.NORTH.previous());
//        assertEquals(MapDirections.NORTH, MapDirections.EAST.previous());
//        assertEquals(MapDirections.EAST, MapDirections.SOUTH.previous());
//        assertEquals(MapDirections.SOUTH, MapDirections.WEST.previous());
//    }
//
//
//
//    @Test
//    void changeFromEnumToString(){
//        assertEquals("Północ", MapDirections.NORTH.toString());
//        assertEquals("Wschód", MapDirections.EAST.toString());
//        assertEquals("Południe", MapDirections.SOUTH.toString());
//        assertEquals("Zachód", MapDirections.WEST.toString());
//    }
//
//}