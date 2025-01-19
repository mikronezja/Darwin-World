package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.WriteDaysToFile.WriteDaysToCSV;
import agh.ics.oop.model.util.RandomPositionForSpawningAnimalsGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable, AnimalBornListener{


    private List<Animal> aliveAnimals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    List<Animal> animalsToRemove = new ArrayList<>();
    private int simulationDays = 0; // jak dlugo trwa symulacja
    private final ProjectWorldMap worldMap;
    boolean shouldWriteIntoCSVFile = false;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    // jak najpopularniejszy genotyp wydobyc


    public Simulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith,
                      int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn,
                      int maxMutationInNewborn, int genomeLength, boolean ifAnimalsMoveSlowerWhenOlder,
                      boolean writeIntoACSVFile

    ) {
        this.worldMap = worldMap;
        worldMap.addAnimalBornListener(this);
        RandomPositionForSpawningAnimalsGenerator randomPositionForSpawningAnimalsGenerator = new RandomPositionForSpawningAnimalsGenerator(worldMap.getCurrentBounds().upperRightCorner().getX() + 1, worldMap.getCurrentBounds().upperRightCorner().getY() + 1);
        shouldWriteIntoCSVFile = writeIntoACSVFile;

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

        while (!Thread.interrupted()) {
            synchronized (aliveAnimals) {
                for (Animal animal : aliveAnimals) {
                    if (!animal.isAlive()) {
                        checkPause();
                        worldMap.killAnimal(animal);
                        deadAnimals.add(animal);
                        animalsToRemove.add(animal);
                    }
                }
                aliveAnimals.removeAll(animalsToRemove);
                animalsToRemove.clear();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                for(Animal animal : new ArrayList<>(aliveAnimals)) {
                    checkPause();
                    worldMap.move(animal);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            checkPause();
            worldMap.eatingPlants();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkPause();
            worldMap.animalsReproducing();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            checkPause();
            worldMap.growPlants();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();

            for (Animal animal : new ArrayList<>(aliveAnimals))
            {
                checkPause();
                animal.increaseDaysAlive();
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

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    private void checkPause() {
        if (paused) {
            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }
   

     public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    private void checkPause() {
        if (paused) {
            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onAnimalBorn(Animal newAnimal) {
        aliveAnimals.add(newAnimal);
    }
    public int getSimulationDays() {
        return simulationDays;
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(aliveAnimals);
    }

    @Override
    public void onAnimalBorn(Animal newAnimal) {
        aliveAnimals.add(newAnimal);

    }
}
