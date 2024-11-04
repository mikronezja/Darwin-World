package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirections;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {

    public static void main(String[] args) {
        System.out.println("system wystartował");
        List<MoveDirections> directions = OptionsParser.translateDirections(args);
        run(directions);
        System.out.println("system zakończył działanie");

        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));

        Animal krowa = new Animal();
        System.out.println(krowa.toString());

        List<MoveDirections> directionsToSimulation = OptionsParser.translateDirections(args);
        List<Vector2d> positionsToSimulation = List.of(new Vector2d(2,2), new Vector2d(3,4));
        Simulation simulation = new Simulation(positionsToSimulation, directionsToSimulation);
        simulation.run();
    }

    private static void run(List<MoveDirections> directions) {
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
