package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void twoVectorsAreEqual(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1,2);
        assertTrue(v1.equals(v2));
    }

    @Test
    void twoVectorsAreDiffirent(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1,-2);
        assertFalse(v1.equals(v2));
    }

    @Test
    void vectorChangeToString(){
        Vector2d v1 = new Vector2d(3,4);
        assertEquals("(3, 4)", v1.toString());
    }

    @Test
    void oneVectorPrecedesOtherVector(){
        Vector2d v1 = new Vector2d(-1,-2);
        Vector2d v2 = new Vector2d(3,4);
        assertTrue(v1.precedes(v2));
    }

    @Test
    void oneVectorNotPrecedesOtherVector(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(-3,-4);
        assertFalse(v1.precedes(v2));
    }

    @Test
    void oneVectorFollowsOtherVector(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(-3,-4);
        assertTrue(v1.follows(v2));
    }

    @Test
    void oneVectorNotFollowsOtherVector(){
        Vector2d v1 = new Vector2d(-1,-2);
        Vector2d v2 = new Vector2d(3,4);
        assertFalse(v1.follows(v2));

    }

    @Test
    void designateUpperRightCornerOfTwoVectors(){
        Vector2d v1 = new Vector2d(1,5);
        Vector2d v2 = new Vector2d(2,3);
        assertEquals(new Vector2d(2,5),v1.upperRight(v2));
    }

    @Test
    void designateLowerLeftCornerOfTwoVectors(){
        Vector2d v1 = new Vector2d(1,5);
        Vector2d v2 = new Vector2d(2,3);
        assertEquals(new Vector2d(1,3),v1.lowerLeft(v2));
    }

    @Test
    void addTwoVectors(){
        Vector2d v1 = new Vector2d(-1,-5);
        Vector2d v2 = new Vector2d(2,3);
        assertEquals(new Vector2d(1,-2),v1.add(v2));
    }

    @Test
    void subtractTwoVectors(){
        Vector2d v1 = new Vector2d(-1,-5);
        Vector2d v2 = new Vector2d(2,3);
        assertEquals(new Vector2d(-3,-8),v1.subtract(v2));
        assertEquals(new Vector2d(3,8),v2.subtract(v1));
    }

    @Test
    void oppositeVectorIsThisVectorWithMinus(){
        Vector2d v1 = new Vector2d(1,4);
        assertEquals(new Vector2d(-1,-4),v1.opposite());
    }
}