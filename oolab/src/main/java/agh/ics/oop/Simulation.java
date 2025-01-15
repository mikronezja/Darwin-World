package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> animals = new ArrayList<>();
    private final ProjectWorldMap worldMap;

    private final int dailyPlants = 0; // ile roslinek dziennie bedzie roslo


    // Moim zdaniem w tym wypadku będzie lepszym wyborem ArrayList, gdyż
    // chcemy sprawnie iterować po kolejnych elementach naszej listy, a ArrayList
    // zapewnia nam szybszy dostęp gdy odwołujemy się do elementu po indeksie

    public Simulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith, int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn, int maxMutationInNewborn, int genomeLength) {
        this.worldMap = worldMap;
        for (int i=0;i<howManyAnimalsToStartWith;i++) {
            Animal animal = new Animal();
            try {
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }

        }
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

}
