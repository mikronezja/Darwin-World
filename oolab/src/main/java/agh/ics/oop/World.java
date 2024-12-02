package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static void main(String[] args) {
        System.out.println("system wystartował");
        List<MoveDirections> directions = List.of();
        try {
            directions = OptionsParser.translateDirections(args);
            run(directions);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));

        Animal krowa = new Animal();
        System.out.println(krowa.toString());

        List<MoveDirections> directionsToSimulation=List.of();
        try {
            directionsToSimulation = OptionsParser.translateDirections(args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.exit(0);
        }

        List<Vector2d> positionsToSimulation = List.of(new Vector2d(2,2), new Vector2d(3,4));
        RectangularMap map = new RectangularMap(5,5);
        map.addObservator(new ConsoleMapDisplay());
        Simulation simulation = new Simulation(positionsToSimulation, directionsToSimulation, map);
        //simulation.run();
        /* Zakomentowane by lepiej odczytać wyniki runSynci runAsync*/

        GrassField field = new GrassField(2);
        field.addObservator(new ConsoleMapDisplay());
        Simulation fieldSimulation = new Simulation(positionsToSimulation, directionsToSimulation, field);
        //fieldSimulation.run();
        /* Zakomentowane by lepiej odczytać wyniki runSynci runAsync*/

//        List<Simulation> simulations = List.of(simulation, fieldSimulation);
//        SimulationEngine engine = new SimulationEngine(simulations);
//        engine.runAsync();
//        try{
//            engine.awaitSimulationsEnd();
//        }catch (InterruptedException e){
//            System.out.println("Nastąpiło przerwanie: "+e);
//        }


        List<Vector2d> positionsToALotOfSimulations = List.of(new Vector2d(2,2), new Vector2d(3,4), new Vector2d(1,3));
        List<Simulation> aLotOfSimulations = new ArrayList<>();
        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        for (int i = 0; i < 1068; i++) {
            RectangularMap simMap = new RectangularMap(5,5);
            simMap.addObservator(new ConsoleMapDisplay());
            aLotOfSimulations.add(new Simulation(positionsToALotOfSimulations, directionsToSimulation, simMap));
        }
        for (int i = 0; i < 1069; i++){
            GrassField simField = new GrassField(5);
            simField.addObservator(new ConsoleMapDisplay());
            aLotOfSimulations.add(new Simulation(positionsToALotOfSimulations, directionsToSimulation, simField));
        }

        SimulationEngine engineWithALotOfSimulations = new SimulationEngine(aLotOfSimulations);
        engineWithALotOfSimulations.runAsyncInThreadPool();
        try{
            engineWithALotOfSimulations.awaitSimulationsEnd();
        }catch (InterruptedException e){
            System.out.println("Minąłczas na wykonanie wątków: "+e);
        }


        System.out.println("system zakończył działanie");
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
