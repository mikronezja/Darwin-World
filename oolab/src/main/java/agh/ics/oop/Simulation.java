package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirections;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation {

    private List<MoveDirections> directions;
    private List<Animal> animals = new ArrayList<Animal>();

    public Simulation(List<Vector2d> positions, List<MoveDirections> directions) {
        this.directions = directions;
        for (Vector2d position : positions) {
            animals.add(new Animal(position));
        }
    }

    public void run(){
        int howManyAnimals = animals.size();
        if (howManyAnimals > 0) {
            for (int i=0; i<directions.size(); i++){
                animals.get(i%howManyAnimals).move(directions.get(i));
                System.out.println("Zwierzę %d : %s".formatted(i%howManyAnimals, animals.get(i).getPosition().toString()));
            }
        }

    }
}
