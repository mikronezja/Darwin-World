package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.WriteDaysToFile.WriteDaysToCSV;
import agh.ics.oop.model.util.RandomPositionForSpawningAnimalsGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> aliveAnimals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    List<Animal> animalsToRemove = new ArrayList<>();
    private int simulationDays = 0; // jak dlugo trwa symulacja
    private final ProjectWorldMap worldMap;
    private final WriteDaysToCSV writeDaysToCSV = new WriteDaysToCSV();
    boolean shouldWriteIntoCSVFile = false;
    private RandomPositionForSpawningAnimalsGenerator randomPositionForSpawningAnimalsGenerator;

    private final int dailyPlants = 0; // ile roslinek dziennie bedzie roslo

    // jak najpopularniejszy genotyp wydobyc

    // Moim zdaniem w tym wypadku będzie lepszym wyborem ArrayList, gdyż
    // chcemy sprawnie iterować po kolejnych elementach naszej listy, a ArrayList
    // zapewnia nam szybszy dostęp gdy odwołujemy się do elementu po indeksie

    public Simulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith,
                      int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn,
                      int maxMutationInNewborn, int genomeLength, boolean ifAnimalsMoveSlowerWhenOlder,
                      boolean writeIntoACSVFile

    ) {
        this.worldMap = worldMap;
        shouldWriteIntoCSVFile = true;
        randomPositionForSpawningAnimalsGenerator = new RandomPositionForSpawningAnimalsGenerator(worldMap.getCurrentBounds().upperRightCorner().getX()+1, worldMap.getCurrentBounds().upperRightCorner().getY()+1);
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
            for(Animal animal : aliveAnimals) {
                if (!animal.isAlive()) {
                    worldMap.killAnimal(animal);
                    deadAnimals.add(animal);
                    animalsToRemove.add(animal);
                }
            }
            if (animalsToRemove.size()>0){
                aliveAnimals.removeAll(animalsToRemove);
                animalsToRemove.clear();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(Animal animal : new ArrayList<>(aliveAnimals)) {
                worldMap.move(animal);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
            howManyAnimalsAlive = aliveAnimals.size();
            for (Animal animal : new ArrayList<>(aliveAnimals))
            {
                animal.decreaseEnergyWithEndOfDay();
                animal.increaseDaysAlive();
            }
        }

        if (shouldWriteIntoCSVFile)
        {

        }

        simulationDays++;
    }



    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(aliveAnimals);
    }

}
