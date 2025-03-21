//package agh.ics.oop;
//
//import agh.ics.oop.model.*;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//class SimulationTest {
//
//    RectangularMap map = new RectangularMap(5,5);
//
//    @Test
//    void rotatingRight(){
//        List<Vector2d> position = List.of(new Vector2d(2,2));
//        List<MoveDirections> direction = List.of(MoveDirections.RIGHT);
//        Simulation simulation = new Simulation(position, direction, map);
//
//        simulation.run();
//        assertEquals(MapDirections.EAST, simulation.getAnimals().get(0).getDirection());
//
//        simulation.run();
//        assertEquals(MapDirections.SOUTH, simulation.getAnimals().get(0).getDirection());
//
//        simulation.run();
//        assertEquals(MapDirections.WEST, simulation.getAnimals().get(0).getDirection());
//
//        simulation.run();
//        assertEquals(MapDirections.NORTH, simulation.getAnimals().get(0).getDirection());
//    }
//
//    @Test
//    void rotatingLeft(){
//        List<Vector2d> position = List.of(new Vector2d(2,2));
//        List<MoveDirections> direction = List.of(MoveDirections.LEFT);
//        Simulation simulation = new Simulation(position, direction, map);
//
//        simulation.run();
//        assertEquals(MapDirections.WEST, simulation.getAnimals().get(0).getDirection());
//
//        simulation.run();
//        assertEquals(MapDirections.SOUTH, simulation.getAnimals().get(0).getDirection());
//
//        simulation.run();
//        assertEquals(MapDirections.EAST, simulation.getAnimals().get(0).getDirection());
//
//        simulation.run();
//        assertEquals(MapDirections.NORTH, simulation.getAnimals().get(0).getDirection());
//    }
//
//    @Test
//    void correctMoveForward(){
//        List<Vector2d> position = List.of(new Vector2d(2,2));
//        List<MoveDirections> direction = List.of(MoveDirections.FORWARD, MoveDirections.RIGHT);
//        Simulation simulation = new Simulation(position, direction, map);
//
//        simulation.run();
//        assertEquals(new Vector2d(2,3),simulation.getAnimals().get(0).getPosition());
//
//        simulation.run();
//        assertEquals(new Vector2d(3,3),simulation.getAnimals().get(0).getPosition());
//
//        simulation.run();
//        assertEquals(new Vector2d(3,2),simulation.getAnimals().get(0).getPosition());
//
//        simulation.run();
//        assertEquals(new Vector2d(2,2),simulation.getAnimals().get(0).getPosition());
//    }
//
//    @Test
//    void correctMoveBackward(){
//        List<Vector2d> position = List.of(new Vector2d(2,2));
//        List<MoveDirections> direction = List.of(MoveDirections.BACKWARD, MoveDirections.RIGHT);
//        Simulation simulation = new Simulation(position, direction, map);
//
//        simulation.run();
//        assertEquals(new Vector2d(2,1),simulation.getAnimals().get(0).getPosition());
//
//        simulation.run();
//        assertEquals(new Vector2d(1,1),simulation.getAnimals().get(0).getPosition());
//
//        simulation.run();
//        assertEquals(new Vector2d(1,2),simulation.getAnimals().get(0).getPosition());
//
//        simulation.run();
//        assertEquals(new Vector2d(2,2),simulation.getAnimals().get(0).getPosition());
//    }
//
//    @Test
//    void notGoingOverMap(){
//        List<Vector2d> positions = List.of(new Vector2d(2,4), new Vector2d(4,2), new Vector2d(2,0), new Vector2d(0,2));
//        // Upper bound, right bound, lower bound, left bound
//        List<MoveDirections> directions = List.of(MoveDirections.FORWARD, MoveDirections.RIGHT, MoveDirections.BACKWARD, MoveDirections.LEFT, MoveDirections.FORWARD, MoveDirections.FORWARD,MoveDirections.BACKWARD,MoveDirections.FORWARD);
//        // 1: forward, forward
//        // 2: turn right, forward
//        // 3: backward backward
//        // 4: turn left, forward
//        Simulation simulation = new Simulation(positions, directions, map);
//        simulation.run();
//        assertEquals(new Vector2d(2,4), simulation.getAnimals().get(0).getPosition());
//        assertEquals(new Vector2d(4,2), simulation.getAnimals().get(1).getPosition());
//        assertEquals(new Vector2d(2,0), simulation.getAnimals().get(2).getPosition());
//        assertEquals(new Vector2d(0,2), simulation.getAnimals().get(3).getPosition());
//    }
//
//    @Test
//    void correctInterpretationOfInputDirection(){
//        List<Vector2d> positions = List.of(new Vector2d(1,2), new Vector2d(3,2));
//        String[] arguments = {"f", "ziemniak", "b", "r", "ogórek", "l", "f", "pomarańcza", "f"};
//        List<MoveDirections> directions = OptionsParser.translateDirections(arguments);
//
//        Simulation simulation = new Simulation(positions, directions, map);
//        simulation.run();
//
//        assertEquals(new Vector2d(2,3), simulation.getAnimals().get(0).getPosition());
//        assertEquals(new Vector2d(2,1), simulation.getAnimals().get(1).getPosition());
//    }
//}