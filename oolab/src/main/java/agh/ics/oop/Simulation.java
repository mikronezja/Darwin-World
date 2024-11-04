package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirections;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {

    private final List<MoveDirections> directions;
    private final List<Animal> animals = new ArrayList<Animal>();
    // Moim zdaniem w tym wypadku będzie lepszym wyborem ArrayList, gdyż
    // chcemy sprawnie iterować po kolejnych elementach naszej listy, a ArrayList
    // zapewnia nam szybszy dostęp gdy odwołujemy się do elementu po indeksie

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
                int ind = i%howManyAnimals;
                animals.get(ind).move(directions.get(i));
                System.out.println("Zwierzę %d : %s".formatted(ind, animals.get(ind).toString()));
            }
        }

    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

}
