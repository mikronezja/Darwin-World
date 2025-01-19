package agh.ics.oop.model.util;

import agh.ics.oop.model.*;

import java.net.Inet4Address;
import java.util.*;

public class DailyDataCollector
{
    private final ProjectWorldMap map;
    private final List<Animal> deadAnimals;
    private final Boundary boundary;
    private final int currentSimulationDay;


    public DailyDataCollector(ProjectWorldMap map,List<Animal> deadAnimals, int currentSimulationDay)
    {
        this.map = map;
        this.deadAnimals = deadAnimals;
        this.boundary = map.getCurrentBounds();
        this.currentSimulationDay = currentSimulationDay;
    }

    public int numberOfAliveAnimals()
    {
        return map.getAnimalsList().size();
    }

    public int getCurrentSimulationDay()
    {
        return currentSimulationDay;
    }

    public int numberOfPlants()
    {
        return map.getPlantsList().size();
    }

    // number of free tiles
    public int numberOfFreeTiles()
    {
        // + 1 dlatego że numeracja jest od 0
        return (boundary.upperRightCorner().getX()+1)*(boundary.upperRightCorner().getY()+1) - map.occupiedPositions().size();
    }

    // najpopularniejszy genotyp ?
    // DOKONCZYC!!
    public List<Integer> mostPopularGenotype()
    {
//        int maxGenomeCount = 0;
//        Map<List<Integer>, Integer> hashMap = new HashMap<>();
//
//        int currentMaxGenomeCount = 0;
//        for (Animal animal : map.getAnimalsList())
//        {
//           currentMaxGenomeCount = 1;
//            if (!hashMap.containsKey(animal.getGenomeAsIntList()))
//           {
//               hashMap.put(animal.getGenomeAsIntList(),1);
//           }
//           else
//           {
//
//           }
//
//        }

        return new ArrayList<>();
    }

    public int averageEnergyLevel()
    {
        return (int)(map.getAnimalsList().stream().map(Animal::getEnergy).reduce(0, Integer::sum) / numberOfAliveAnimals());
    }

    // średnia długość życia dla martwych zwierzaków
    public Optional<Integer> averageLifeSpan()
    {
        if (deadAnimals.size() > 0) {
            return Optional.of((int) (deadAnimals.stream().map(Animal::getDaysAlive).reduce(0, Integer::sum) / deadAnimals.size()));
        }
        else
        {
            return Optional.empty();
        }
    }

    // średnia ilość dzieci
    public int averageKidsNumber()
    {
        return (int)(totalNumberOfKids() / numberOfAliveAnimals());
    }

    private int totalNumberOfKids()
    {
        return map.getAnimalsList().stream().map(Animal::getKidsNumber).reduce(0, Integer::sum);
    }


}
