package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalComparator;
import agh.ics.oop.model.util.RandomPositionForPlantsGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Globe implements ProjectWorldMap{


    private final Vector2d upperRightMapCorner;
    private final Vector2d lowerLeftMapCorner = new Vector2d(0, 0);
    private final Vector2d upperRightEquatorCorner;
    private final Vector2d lowerLeftEquatorCorner;
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final int everydayPlantsGrow;
    private final int howManyEnergyFromPlants;
    private final RandomPositionForPlantsGenerator positionForPlantsGenerator;
    private final Random random = new Random();
    private static final Comparator<Animal> ANIMAL_COMPARATOR = new AnimalComparator();
    private final Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    private Map<Vector2d, Integer> recentDeadAnimals = new HashMap<>();
    protected List<MapChangeListener> observators = new ArrayList<>();
    private UUID id = UUID.randomUUID();
    private final boolean ifAnimalsMoveSlowerWhenOlder;



    public Globe(int height, int width, int howManyPlantsOnStart, int howManyEnergyFromPlants, int howManyPlantsEveryDay, boolean ifAnimalsMoveSlowerWhenOlder) {
        this.upperRightMapCorner = new Vector2d(width-1, height-1);
        this.lowerLeftEquatorCorner = new Vector2d(0, ((height/5)*2)+1);
        this.upperRightEquatorCorner = new Vector2d(width-1, ((height/5)*2)+((height+2)/5));
        this.everydayPlantsGrow = howManyPlantsEveryDay;
        this.howManyEnergyFromPlants=howManyPlantsOnStart;
        this.positionForPlantsGenerator = new RandomPositionForPlantsGenerator(height,width, upperRightEquatorCorner, lowerLeftEquatorCorner);
        this.ifAnimalsMoveSlowerWhenOlder = ifAnimalsMoveSlowerWhenOlder;


        for (int i=0; i<howManyPlantsOnStart; i++) {
            Vector2d position = positionForPlantsGenerator.generatePosition();
            if (position == null){
                break;
            }
            Plant plant = new Plant(howManyEnergyFromPlants, position);
            plants.put(position, plant);
        }
    }

    @Override
    public synchronized void place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getPosition();
        animals.computeIfAbsent(position, k -> new TreeSet<>(ANIMAL_COMPARATOR))
                .add(animal);
        mapChanged("Zwierze zostało położone na "+position.toString());
    }

    @Override
    public synchronized void move(Animal animal) {
        Vector2d positionBeforeMove = animal.getPosition();
        TreeSet<Animal> setOfAnimalsBeforeMove = animals.get(positionBeforeMove);

        // Safeguard against null
        if (setOfAnimalsBeforeMove == null) {
            System.err.printf("Error: No animals at position %s when trying to move animal %s%n", positionBeforeMove, animal);
            return;
        }

        // Remove the animal from the current position
        setOfAnimalsBeforeMove.remove(animal);
        if (setOfAnimalsBeforeMove.isEmpty()) {
            animals.remove(positionBeforeMove);
        }

        // Move the animal
        animal.move(this);

        // Add the animal to the new position
        animals.computeIfAbsent(animal.getPosition(), k -> new TreeSet<>(ANIMAL_COMPARATOR))
                .add(animal);

        // Notify observers
        mapChanged("Zwierzę poruszyło się z %s na %s".formatted(positionBeforeMove, animal.getPosition()));
    }


    @Override
    public synchronized boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    @Override
    public synchronized List<WorldElement> objectsAt(Vector2d position) {
        List<WorldElement> objects = List.of();
        if (isOccupied(position)) {
            objects = animals.get(position).stream().collect(Collectors.toList());
        }
        return objects;
    }

    @Override
    public synchronized List<WorldElement> getElements() {
        return Stream.concat(
                plants.values().stream()
                        .filter(Objects::nonNull), // Filter out null values from plants
                animals.values().stream()
                        .flatMap(TreeSet::stream) // Flatten TreeSet<String> streams
                        .filter(Objects::nonNull) // Filter out null values from animals
        ).collect(Collectors.toList());


//        return Stream.concat(
//                        animals.values()
//                                .stream()
//                                .flatMap(TreeSet::stream)
//                                .filter(Objects::nonNull),
//                        plants.values().stream()
//                                .filter(Objects::nonNull))
//                .collect(Collectors.toList());
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public synchronized void killAnimal(Animal animal) {
        mapChanged("Lista zwierzat przed usunieciem: %s".formatted(getAnimalsList()));
        Vector2d position = animal.getPosition();
        TreeSet<Animal> onThisSpace = animals.get(position);
        onThisSpace.remove(animal);
        if (onThisSpace.isEmpty()){
            animals.remove(position);
        }
        mapChanged("Zwierzę umarło na %s".formatted(position));
    }

    @Override
    public synchronized void animalsReproducing() {
        for (Vector2d position: new ArrayList<>(animals.keySet())){
            animalsReproduceAt(position);
        }
    }

    @Override
    public synchronized void growPlants() {
        for (int i=0;i<everydayPlantsGrow;i++){
            Vector2d position = positionForPlantsGenerator.generatePosition();
            if (position != null){
                Plant plant = new Plant(howManyEnergyFromPlants, position);
                plants.put(position, plant);
                mapChanged("Roślinka wyrosła na %s".formatted(position));
            }
        }
    }

    @Override
    public synchronized void eatingPlants() {
        for (Vector2d position: new ArrayList<>(animals.keySet())){
            animalsEatAt(position);
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeftMapCorner) && position.precedes(upperRightMapCorner);
    }

    private synchronized void animalsReproduceAt(Vector2d position)
    {
        if (animals.containsKey(position) && animals.get(position).size()>1)
        {
            SortedSet<Animal> winningAnimals = animals.get(position).headSet(animals.get(position).first(),true);
            Animal parent1, parent2;
            if (winningAnimals.size() == 2) {
                parent1 = winningAnimals.getFirst();
                winningAnimals.removeFirst();
                parent2 = winningAnimals.getFirst();
                winningAnimals.add(parent1);
            }
            else if (winningAnimals.size() == 1) {
                parent1 = winningAnimals.getFirst();
                winningAnimals.removeFirst();
                SortedSet<Animal> secondTurnWinningAnimals = animals.get(position).headSet(animals.get(position).first(), true);
                winningAnimals.add(parent1);
                if (secondTurnWinningAnimals.size() == 1) {
                    parent2 = secondTurnWinningAnimals.getFirst();
                }
                else {
                    List<Animal> finalists = secondTurnWinningAnimals.stream().toList();
                    int howManyWinningAnimals = finalists.size();
                    int indexOfWinner = random.nextInt(howManyWinningAnimals);
                    parent2=finalists.get(indexOfWinner);
                }
            }
            else{
                List<Animal> finalists = winningAnimals.stream().collect(Collectors.toList());
                int howManyWinningAnimals = finalists.size();
                int indexOfWinner = random.nextInt(howManyWinningAnimals);
                parent1=finalists.get(indexOfWinner);
                Collections.swap(finalists, indexOfWinner, finalists.size()-1);
                howManyWinningAnimals--;
                indexOfWinner = random.nextInt(howManyWinningAnimals);
                parent2=finalists.get(indexOfWinner);
            }
            try{
                if (parent1.getEnergy() >= parent1.getMinReproductionEnergy() && parent2.getEnergy() >= parent2.getMinReproductionEnergy()){
                Animal child = parent1.reproduce(parent2);
                this.place(child);
                mapChanged("Urodziło się nowe zwierzę na %s".formatted(position));
                }
            }
            catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }


    }

    private synchronized void animalsEatAt(Vector2d position) {
        if (plants.containsKey(position) && !animals.get(position).isEmpty()) {
            SortedSet<Animal> winningAnimals = animals.get(position).headSet(animals.get(position).first(), true);
            Animal winningAnimal;
            if( winningAnimals.size() == 1) {
                winningAnimal=winningAnimals.first();

            }
            else {
                List<Animal> finalists = winningAnimals.stream().toList();
                int howManyWinningAnimals = finalists.size();
                int indexOfWinner = random.nextInt(howManyWinningAnimals);
                winningAnimal=finalists.get(indexOfWinner);
            }
            winningAnimal.eatPlant(plants.get(position));
            plants.remove(position);
            positionForPlantsGenerator.addPositionToGenerator(position);
            mapChanged("Zwierzę zjadło roślinkę na %s".formatted(position));

        }
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeftMapCorner,upperRightMapCorner);
    }

    private void mapChanged(String message) {
        for (MapChangeListener observator : observators) {
            observator.mapChanged(this, message);
        }
    }

    public void addObservator(MapChangeListener observator){
        observators.add(observator);
    }

    public void removeObservator(MapChangeListener observator){
        observators.remove(observator);
    }

    @Override
    public boolean isPositionMoreDesirableForPlants(Vector2d position)
    {
        return position.follows(lowerLeftEquatorCorner) && position.precedes(upperRightEquatorCorner);
    }

    @Override
    public synchronized List<Animal> getAnimalsList() {
        return animals.values()
                .stream()
                .flatMap(TreeSet::stream)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Plant> getPlantsList()
    {
        return new ArrayList<>(plants.values());
    }

    @Override
    public synchronized Set<Vector2d> occupiedPositions()
    {
        return Stream.concat(animals.keySet().stream(), plants.keySet().stream())
                .collect(Collectors.toSet());
    }
}
