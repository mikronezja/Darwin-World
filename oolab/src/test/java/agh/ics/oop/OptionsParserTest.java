package agh.ics.oop;

import agh.ics.oop.model.MoveDirections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void translateDirectionsWithAllGoodData(){
        String[] args = {"f","r","b","l"};
        MoveDirections[] directions = OptionsParser.translateDirections(args);
        assertArrayEquals(new MoveDirections[] {MoveDirections.FORWARD,MoveDirections.RIGHT,MoveDirections.BACKWARD,MoveDirections.LEFT},directions);
    }

    @Test
    void translateDirectionsWithOneBadData(){
        String[] args = {"f","Scooby-Doo","l"};
        MoveDirections[] directions = OptionsParser.translateDirections(args);
        assertArrayEquals(new MoveDirections[] {MoveDirections.FORWARD,MoveDirections.LEFT},directions);
    }

    @Test
    void translateDirectionsWithAllBadData(){
        String[] args = {"Krasnal","Scooby-Doo","Kuki","Koszula"};
        MoveDirections[] directions = OptionsParser.translateDirections(args);
        assertArrayEquals(new MoveDirections[] {},directions);
    }
}