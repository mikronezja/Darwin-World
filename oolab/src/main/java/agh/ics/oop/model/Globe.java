package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalComparator;
import agh.ics.oop.model.util.RandomPositionForPlantsGenerator;
import agh.ics.oop.model.util.RandomPositionForSpawningAnimalsGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Globe implements ProjectWorldMap{


    private Vector2d upperRightMapCorner;
    private Vector2d lowerLeftMapCorner = new Vector2d(0, 0);
    private Vector2d upperRightEquatorCorner;
    private Vector2d lowerLeftEquatorCorner;
    private Map<Vector2d, Plant> plants = new HashMap<>();
    private RandomPositionForPlantsGenerator positionForPlantsGenerator;
    private static final Comparator<Animal> ANIMAL_COMPARATOR = new AnimalComparator();
    private Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    private UUID id = UUID.randomUUID();

    public Globe(int height, int width, int howManyPlants, int howManyEnergyFromPlants) {
        this.upperRightMapCorner = new Vector2d(width-1, height-1);
        this.lowerLeftEquatorCorner = new Vector2d(0, ((height/5)*2)+1);
        this.upperRightEquatorCorner = new Vector2d(width-1, ((height/5)*2)+((height+2)/5));
        this.positionForPlantsGenerator = new RandomPositionForPlantsGenerator(height,width, upperRightEquatorCorner, lowerLeftEquatorCorner);


        for (int i=0; i<howManyPlants; i++) {
            Vector2d position = positionForPlantsGenerator.generatePosition();
            if (position == null){
                break;
            }
            Plant plant = new Plant(howManyEnergyFromPlants, position);
            plants.put(position, plant);
        }
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();
        if (isOccupied(position)) {
            animals.get(position).add(animal);
        }
        else {
            animals.computeIfAbsent(position, k -> new TreeSet<>(ANIMAL_COMPARATOR))
                    .add(animal);
        }

    }

    @Override
    public void move(Animal animal) {

        Vector2d positionBeforeMove = animal.getPosition();
        TreeSet<Animal> setOfAnimalsBeforeMove = animals.get(positionBeforeMove);
        setOfAnimalsBeforeMove.remove(animal);

        animal.move(this);

        if (animal.getPosition().equals(positionBeforeMove)) {
            setOfAnimalsBeforeMove.add(animal);
        }
        else{
            if (setOfAnimalsBeforeMove.isEmpty()) {
                animals.remove(positionBeforeMove);
            }
            if (animals.containsKey(animal.getPosition())) {
                animals.get(animal.getPosition()).add(animal);
            }
            animals.computeIfAbsent(animal.getPosition(), k -> new TreeSet<>(ANIMAL_COMPARATOR))
                    .add(animal);
        }

    }


    @Override
    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public List<WorldElement> objectsAt(Vector2d position) {
        List<WorldElement> objects = List.of();
        if (isOccupied(position)) {
            objects = animals.get(position).stream().collect(Collectors.toList());
        }
        return objects;
    }

    @Override
    public List<WorldElement> getElements() {
        return Stream.concat(
                        animals.values()
                                .stream()
                                .flatMap(TreeSet::stream)
                                .collect(Collectors.toList())
                                .stream(),
                        plants.values().stream())
                .collect(Collectors.toList());
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeftMapCorner) && position.precedes(upperRightMapCorner);
    }

    private void animalsReproduceAt(Vector2d position)
    {
        animals.get(position).headSet( animals.get(position).first() );
    }

    private void animalsEatAt(Vector2d position)
    {
        if (animals.containsKey(position) && !animals.get(position).isEmpty())
        {
            SortedSet<Animal> winningAnimals = animals.get(position).headSet(animals.get(position).first());
            Animal winningAnimal;
            if( winningAnimals.size() == 1)
            {
                winningAnimal.eatPlant();
            }
            else
            {
                
            }

        }
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeftMapCorner,upperRightMapCorner);
    }
}
