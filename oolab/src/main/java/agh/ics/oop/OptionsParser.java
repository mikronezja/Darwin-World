package agh.ics.oop;

import agh.ics.oop.model.MoveDirections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirections> translateDirections(String[] args) {
        List<MoveDirections> moveDirections = new ArrayList<>();
        for (String arg : args) {
            switch (arg) {
                case "f" -> moveDirections.add(MoveDirections.FORWARD);
                case "b" -> moveDirections.add(MoveDirections.BACKWARD);
                case "r" -> moveDirections.add(MoveDirections.RIGHT);
                case "l" -> moveDirections.add(MoveDirections.LEFT);
            }

        }
        return moveDirections;
    }
}
