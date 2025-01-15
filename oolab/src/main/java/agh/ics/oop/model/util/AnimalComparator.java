package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal animal1, Animal animal2) {
        return Comparator
                .comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(Animal::getHowManyDaysIsAlive).reversed()
                .thenComparingInt(Animal::getKidsNumber).reversed()
                .compare(animal1, animal2);
    }
}
