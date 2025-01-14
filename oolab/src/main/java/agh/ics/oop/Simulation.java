package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable{

    private final List<MoveDirections> directions;
    private final List<Animal> animals = new ArrayList<>();
    private final WorldMap worldMap;
    private static final int genomLength = 10; // cos sie tu zrobi
    private static final int startingEnergy = 10;
    private static final int minReproductionEnergy = 10; // to w globe ?
    private static final int subtractingEnergyWhileReproducing = 10;
    private static final int minNumberOfmutations = 10;
    private static final int maxNumberOfmutations = 10;
    private final int dailyPlants = 0; // ile roslinek dziennie bedzie roslo


    // Moim zdaniem w tym wypadku będzie lepszym wyborem ArrayList, gdyż
    // chcemy sprawnie iterować po kolejnych elementach naszej listy, a ArrayList
    // zapewnia nam szybszy dostęp gdy odwołujemy się do elementu po indeksie

    public Simulation(List<Vector2d> positions, List<MoveDirections> directions, WorldMap worldMap) {
        this.directions = directions;
        this.worldMap = worldMap;
        for (Vector2d position : positions) {
            Animal animal = new Animal(position);
            try {
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public Simulation(WorldMap worldMap)
    {


    }




    public void run(){
        int howManyAnimals = animals.size();
        if (howManyAnimals > 0) {
            for (int i=0; i<directions.size(); i++){
                try{
                    Thread.sleep(500);
                    int ind = i%howManyAnimals;
                    worldMap.move(animals.get(ind), directions.get(i));
                }
                catch(InterruptedException e){
                    System.out.println("Poruszanie zwierzaka zostało przerwane");
                }

            }
        }

    }



    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }
    public static int getGenomLength() { return genomLength; }
    public static int getStartingEnergy() {return startingEnergy;}
    public static int getSubtractingEnergyWhileReproducing() {return subtractingEnergyWhileReproducing;}
    public static int getMinNumberOfmutations() {return minNumberOfmutations;}
    public static int getMaxNumberOfmutations() {return maxNumberOfmutations;}
}
