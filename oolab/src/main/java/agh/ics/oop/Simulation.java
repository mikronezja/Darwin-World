package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.WriteDaysToFile.WriteDaysToCSV;
import agh.ics.oop.model.util.RandomPositionForSpawningAnimalsGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable{


    private List<Animal> aliveAnimals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    List<Animal> animalsToRemove = new ArrayList<>();
    private int simulationDays = 0; // jak dlugo trwa symulacja
    private final ProjectWorldMap worldMap;
    boolean shouldWriteIntoCSVFile = false;

    // jak najpopularniejszy genotyp wydobyc


    public Simulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith,
                      int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn,
                      int maxMutationInNewborn, int genomeLength, boolean ifAnimalsMoveSlowerWhenOlder,
                      boolean writeIntoACSVFile

    ) {
        this.worldMap = worldMap;
        RandomPositionForSpawningAnimalsGenerator randomPositionForSpawningAnimalsGenerator = new RandomPositionForSpawningAnimalsGenerator(worldMap.getCurrentBounds().upperRightCorner().getX() + 1, worldMap.getCurrentBounds().upperRightCorner().getY() + 1);

        this.shouldWriteIntoCSVFile = writeIntoACSVFile;

        for (int i=0;i<howManyAnimalsToStartWith;i++) {
            Animal animal = new Animal(randomPositionForSpawningAnimalsGenerator.getRandomPosition(), genomeLength, howManyEnergyAnimalsStartWith, energyNeededToReproduce, energyGettingPassedToDescendant,minMutationInNewborn,maxMutationInNewborn, ifAnimalsMoveSlowerWhenOlder);
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
            synchronized (aliveAnimals) {
                for (Animal animal : new ArrayList<>(aliveAnimals)) {
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
            aliveAnimals = worldMap.getAnimalsList();
            howManyAnimalsAlive = aliveAnimals.size();

            for (Animal animal : new ArrayList<>(aliveAnimals)) {
                animal.increaseDaysAlive(); // zwieksza tylko dla zywych zwierzakow
            }


            if (shouldWriteIntoCSVFile) {
                WriteDaysToCSV writeIntoCSVFile = new WriteDaysToCSV(worldMap,deadAnimals,simulationDays);
                try {
                    writeIntoCSVFile.givenDataArray_whenConvertToCSV_thenOutputCreated();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            simulationDays++;
        }
    }

    public int getSimulationDays() {return simulationDays;}
    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(aliveAnimals);
    }
    public List<Animal> getDeadAnimals() {
        return Collections.unmodifiableList(deadAnimals);
    }
}
