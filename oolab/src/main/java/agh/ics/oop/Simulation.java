package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionForSpawningAnimalsGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> aliveAnimals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final ProjectWorldMap worldMap;
    private RandomPositionForSpawningAnimalsGenerator randomPositionForSpawningAnimalsGenerator;

    private final int dailyPlants = 0; // ile roslinek dziennie bedzie roslo


    // Moim zdaniem w tym wypadku będzie lepszym wyborem ArrayList, gdyż
    // chcemy sprawnie iterować po kolejnych elementach naszej listy, a ArrayList
    // zapewnia nam szybszy dostęp gdy odwołujemy się do elementu po indeksie

    public Simulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith, int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn, int maxMutationInNewborn, int genomeLength) {
        this.worldMap = worldMap;
        randomPositionForSpawningAnimalsGenerator = new RandomPositionForSpawningAnimalsGenerator(worldMap.getCurrentBounds().upperRightCorner().getX()+1, worldMap.getCurrentBounds().upperRightCorner().getY()+1);
        for (int i=0;i<howManyAnimalsToStartWith;i++) {
            Animal animal = new Animal(randomPositionForSpawningAnimalsGenerator.getRandomPosition(), genomeLength, howManyEnergyAnimalsStartWith, energyNeededToReproduce, energyGettingPassedToDescendant,minMutationInNewborn,maxMutationInNewborn);
            try {
                worldMap.place(animal);
                aliveAnimals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void run(){
        int howManyAnimalsAlive = aliveAnimals.size();
        if (howManyAnimalsAlive > 0) {
            for(Animal animal : aliveAnimals) {
                if (!animal.isAlive()) {
                    worldMap.killAnimal(animal);
                    deadAnimals.add(animal);
                    aliveAnimals.remove(animal);
                }
            }
            for(Animal animal : aliveAnimals) {
                worldMap.move(animal);
            }
            worldMap.eatingPlants();
            worldMap.animalsReproducing();
            worldMap.growPlants();
        }

    }



    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(aliveAnimals);
    }

}
