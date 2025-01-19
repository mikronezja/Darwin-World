package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.RandomPositionForSpawningAnimalsGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable{

    private List<Animal> aliveAnimals = new ArrayList<>();
    private List<Animal> deadAnimals = new ArrayList<>();
    private List<Animal> animalsToRemove = new ArrayList<>();
    private final ProjectWorldMap worldMap;

    public Simulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith, int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn, int maxMutationInNewborn, int genomeLength) {
        this.worldMap = worldMap;
        RandomPositionForSpawningAnimalsGenerator randomPositionForSpawningAnimalsGenerator = new RandomPositionForSpawningAnimalsGenerator(worldMap.getCurrentBounds().upperRightCorner().getX() + 1, worldMap.getCurrentBounds().upperRightCorner().getY() + 1);
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
        while (howManyAnimalsAlive > 0) {
            synchronized (aliveAnimals) {
                for (Animal animal : aliveAnimals) {
                    if (!animal.isAlive()) {
                        worldMap.killAnimal(animal);
                        deadAnimals.add(animal);
                        animalsToRemove.add(animal);
                    }
                }
                aliveAnimals.removeAll(animalsToRemove);
                animalsToRemove.clear();
            }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (aliveAnimals){
                for(Animal animal : new ArrayList<>(aliveAnimals)) {
                    worldMap.move(animal);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            worldMap.eatingPlants();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            worldMap.animalsReproducing();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            worldMap.growPlants();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            aliveAnimals=worldMap.getAnimalsList();
            howManyAnimalsAlive = aliveAnimals.size();
        }

    }



    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(aliveAnimals);
    }

}
