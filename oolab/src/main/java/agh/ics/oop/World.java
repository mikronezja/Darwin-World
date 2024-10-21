package agh.ics.oop;

import agh.ics.oop.model.MoveDirections;

public class World {

    public static void main(String[] args) {
        System.out.println("system wystartował");
        MoveDirections[] directions = OptionsParser.translateDirections(args);
        run(directions);
        System.out.println("system zakończył działanie");
    }

    public static void run(MoveDirections[] directions) {
        System.out.println("Start");
        for (MoveDirections command : directions) {
            switch (command) {
                case FORWARD -> System.out.println("Zwierzak idzie do przodu");
                case BACKWARD -> System.out.println("Zwierzak idzie do tyłu");
                case RIGHT -> System.out.println("Zwierzak skręca w prawo");
                case LEFT -> System.out.println("Zwierzak skręca w lewo");
            }

        }
        System.out.println("Stop");
    }
}
