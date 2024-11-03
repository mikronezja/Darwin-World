package agh.ics.oop;

import agh.ics.oop.model.MoveDirections;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void translateDirectionsWithAllGoodData(){
        String[] args = {"f","r","b","l"};
        List<MoveDirections> directions = OptionsParser.translateDirections(args);
        assertEquals(List.of(MoveDirections.FORWARD, MoveDirections.RIGHT,MoveDirections.BACKWARD,MoveDirections.LEFT),directions);
    }


    @Test
    void translateDirectionsWithOneBadData(){
        String[] args = {"f","Scooby-Doo","l"};
        List<MoveDirections> directions = OptionsParser.translateDirections(args);
        assertEquals(List.of(MoveDirections.FORWARD,MoveDirections.LEFT),directions);
    }

    @Test
    void translateDirectionsWithAllBadData(){
        String[] args = {"Krasnal","Scooby-Doo","Kuki","Koszula"};
        List<MoveDirections> directions = OptionsParser.translateDirections(args);
        assertEquals(List.of(), directions);
    }
}