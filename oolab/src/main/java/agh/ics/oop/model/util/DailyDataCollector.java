package agh.ics.oop.model.util;

import agh.ics.oop.model.*;

import java.util.*;

import static java.lang.Math.min;

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
    public Set<List<Integer>> mostPopularGenotype()
    {
        int maxGenomeCount = 0;
        Map<Genome, Integer> hashMap = new HashMap<>();
        Set<List<Integer>> resultSet = new HashSet<>();


        int currentMaxGenomeCount = 0;
        for (Animal animal : map.getAnimalsList())
        {
            currentMaxGenomeCount = 1;
            if (!hashMap.containsKey(animal.getGenome())) {
                hashMap.put(animal.getGenome(), 1);
            } else {
                hashMap.put(animal.getGenome(), hashMap.get(animal.getGenome()) + 1);
                currentMaxGenomeCount = hashMap.get(animal.getGenome());
            }

            if (maxGenomeCount < currentMaxGenomeCount)
                maxGenomeCount = currentMaxGenomeCount;
        }

        for(Map.Entry<Genome, Integer> entry : hashMap.entrySet())
        {
            if (entry.getValue() == maxGenomeCount)
            {
                int[] genomeArray = entry.getKey().getGenome();
                List<Integer> genomeList = Arrays.stream(genomeArray)
                        .boxed()
                        .toList();
                resultSet.add(genomeList);
            }
        }

        return resultSet;
    }

    public int averageEnergyLevel()
    {
        if (numberOfAliveAnimals()>0){
            return (int)(map.getAnimalsList().stream().map(Animal::getEnergy).reduce(0, Integer::sum) / numberOfAliveAnimals());
        }
        else{
            return 0;
        }
    }

    // średnia długość życia dla martwych zwierzaków
    public synchronized Optional<Integer> averageLifeSpan()
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
        if (numberOfAliveAnimals()>0){
            return (int)(totalNumberOfKids() / numberOfAliveAnimals());
        }
        else{
            return 0;
        }
    }

    private int totalNumberOfKids()
    {
        return map.getAnimalsList().stream().map(Animal::getKidsNumber).reduce(0, Integer::sum);
    }

    public UUID getCurrentMapID()
    {
        return map.getID();
    }


}
