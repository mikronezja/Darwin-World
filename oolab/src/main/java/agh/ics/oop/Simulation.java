package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirections;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {

    private final List<MoveDirections> directions;
    private final List<Animal> animals = new ArrayList<>();
    private final WorldMap worldMap;
    // Moim zdaniem w tym wypadku będzie lepszym wyborem ArrayList, gdyż
    // chcemy sprawnie iterować po kolejnych elementach naszej listy, a ArrayList
    // zapewnia nam szybszy dostęp gdy odwołujemy się do elementu po indeksie

    public Simulation(List<Vector2d> positions, List<MoveDirections> directions, WorldMap worldMap) {
        this.directions = directions;
        this.worldMap = worldMap;
        for (Vector2d position : positions) {
            Animal animal = new Animal(position);
            worldMap.place(animal);
            animals.add(animal);
        }
    }

    public void run(){
        int howManyAnimals = animals.size();
        if (howManyAnimals > 0) {
            for (int i=0; i<directions.size(); i++){
                int ind = i%howManyAnimals;
                worldMap.move(animals.get(ind), directions.get(i));
                System.out.println("Zwierzę %d : %s".formatted(ind, animals.get(ind).toString()));
            }
        }

    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

}
