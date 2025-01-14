package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    public int compare(Animal animal1, Animal animal2)
    {
        if (animal2.getEnergy() == animal1.getEnergy())
        {
            if(animal2.getHowManyDaysIsAlive() == animal1.getHowManyDaysIsAlive())
            {
                return animal2.getKidsNumber() - animal1.getKidsNumber();
            }
            return animal2.getHowManyDaysIsAlive() - animal1.getHowManyDaysIsAlive();
        }
        return animal2.getEnergy() - animal1.getEnergy();
    }
}
