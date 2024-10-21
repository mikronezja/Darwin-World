package agh.ics.oop;

import agh.ics.oop.model.MoveDirections;

import java.util.Arrays;

public class OptionsParser {
    public static MoveDirections[] translateDirections(String[] args) {
        MoveDirections[] directions = new MoveDirections[args.length] ;
        int i = 0;
        for (String arg : args) {
            switch (arg) {
                case "f" -> {directions[i] = MoveDirections.FORWARD; i++;}
                case "b" -> {directions[i] = MoveDirections.BACKWARD; i++;}
                case "r" -> {directions[i] = MoveDirections.RIGHT; i++;}
                case "l" -> {directions[i] = MoveDirections.LEFT; i++;}
            }

        }
        return Arrays.copyOfRange(directions, 0, i);
    }
}
