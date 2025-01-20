package agh.ics.oop.model;

import agh.ics.oop.model.util.AnimalBornListener;
import agh.ics.oop.model.util.AnimalComparator;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.RandomPositionForPlantsGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private final boolean ifPlantsPreferDeadAnimals;
    private final RandomPositionForPlantsGenerator positionForPlantsGenerator;
    private final Random random = new Random();
    private static final Comparator<Animal> ANIMAL_COMPARATOR = new AnimalComparator();
    private Map<Vector2d, HashSet<Animal>> animals = new ConcurrentHashMap<>();
    private Map<Vector2d, Integer> recentDeadAnimals = new HashMap<>();
    private List<MapChangeListener> observators = new ArrayList<>();
    private List<AnimalBornListener> animalBornListeners = new ArrayList<>();
    private UUID id = UUID.randomUUID();
    private final boolean ifAnimalsMoveSlowerWhenOlder;



    public Globe(int height, int width, int howManyPlantsOnStart, int howManyEnergyFromPlants, int howManyPlantsEveryDay, boolean ifAnimalsMoveSlowerWhenOlder, boolean ifPlantsPreferDeadAnimals) {
        this.upperRightMapCorner = new Vector2d(width-1, height-1);
        this.lowerLeftEquatorCorner = new Vector2d(0, ((height/5)*2)+1);
        this.upperRightEquatorCorner = new Vector2d(width-1, ((height/5)*2)+((height+2)/5));
        this.everydayPlantsGrow = howManyPlantsEveryDay;
        this.howManyEnergyFromPlants=howManyPlantsOnStart;
        this.positionForPlantsGenerator = new RandomPositionForPlantsGenerator(height,width, upperRightEquatorCorner, lowerLeftEquatorCorner, ifPlantsPreferDeadAnimals);
        this.ifAnimalsMoveSlowerWhenOlder = ifAnimalsMoveSlowerWhenOlder;
        this.ifPlantsPreferDeadAnimals = ifPlantsPreferDeadAnimals;


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
        if(!canMoveTo(position))
        {
         throw new IncorrectPositionException(position);
        }
        else
        {
            animals.computeIfAbsent(position, k -> new HashSet<>())
                    .add(animal);
            mapChanged("Zwierze zostało położone na " + position.toString());
        }
    }

    @Override
    public synchronized void move(Animal animal) {
        Vector2d positionBeforeMove = animal.getPosition();
        HashSet<Animal> setOfAnimalsBeforeMove = animals.get(positionBeforeMove);

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
        animals.computeIfAbsent(animal.getPosition(), k -> new HashSet<>())
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
                        .filter(Objects::nonNull),
                animals.values().stream()
                        .flatMap(HashSet::stream)
                        .filter(Objects::nonNull)
        ).collect(Collectors.toList());
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public synchronized void killAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        HashSet<Animal> onThisSpace = animals.get(position);
        animals.get(position).remove(animal);
        positionForPlantsGenerator.addPositionOfDeadAnimal(position);
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
        // create a priority queue with an animal comparator
        // has all the animals inside


        if (animals.containsKey(position) && animals.get(position).size()>1)
        {
            PriorityQueue<Animal> compareAnimals = createPQwithAnimalComparatorAtPosition(position);
//            SortedSet<Animal> winningAnimals = animals.get(position).headSet(animals.get(position).first(),true);

            // build a sortedSet
            List<Animal> winningAnimals = getWinningList(compareAnimals);


            Animal parent1, parent2;
            if (winningAnimals.size() == 2) {
                parent1 = winningAnimals.get(0);
                parent2 = winningAnimals.get(1);
            }
            else if (winningAnimals.size() == 1) {
                parent1 = winningAnimals.getFirst();
                List<Animal> secondTurnWinningAnimals = getWinningList(compareAnimals); // powinno juz nie byc tych oryginalnych

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
            else
            {
                int howManyWinningAnimals = winningAnimals.size();
                int indexOfWinner = random.nextInt(howManyWinningAnimals);
                parent1=winningAnimals.get(indexOfWinner);
                Collections.swap(winningAnimals, indexOfWinner, winningAnimals.size()-1);
                howManyWinningAnimals--;
                indexOfWinner = random.nextInt(howManyWinningAnimals);
                parent2=winningAnimals.get(indexOfWinner);
            }
            try{
                if (parent1.getEnergy() >= parent1.getMinReproductionEnergy() && parent2.getEnergy() >= parent2.getMinReproductionEnergy()){
                    Animal child = parent1.reproduce(parent2);
                    this.place(child);
                    animalBorn(child);
                    mapChanged("Urodziło się nowe zwierzę na %s".formatted(position));
                }
            }
            catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }


    }

    private synchronized void animalsEatAt(Vector2d position) {
        if (plants.containsKey(position) && !animals.get(position).isEmpty())
        {
            PriorityQueue<Animal> compareAnimals = createPQwithAnimalComparatorAtPosition(position);

            // build a sortedSet
            List<Animal> winningAnimals = getWinningList(compareAnimals);

            Animal winningAnimal;
            if( winningAnimals.size() == 1)
            {
                winningAnimal=winningAnimals.get(0);
            }
            else {
                int howManyWinningAnimals = winningAnimals.size();
                int indexOfWinner = random.nextInt(howManyWinningAnimals);
                winningAnimal=winningAnimals.get(indexOfWinner);
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

    private void animalBorn(Animal animal){
        for (AnimalBornListener listener : animalBornListeners){
            listener.onAnimalBorn(animal);
        }
    }

    public void addObservator(MapChangeListener observator){
        observators.add(observator);
    }

    public void removeObservator(MapChangeListener observator){
        observators.remove(observator);
    }

    public void addAnimalBornListener(AnimalBornListener listener){
        animalBornListeners.add(listener);
    }

    public void removeAnimalBornListener(AnimalBornListener listener){
        animalBornListeners.remove(listener);
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
                .flatMap(HashSet::stream)
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

    private PriorityQueue<Animal> createPQwithAnimalComparatorAtPosition(Vector2d position)
    {
        PriorityQueue<Animal> pq = new PriorityQueue<>(ANIMAL_COMPARATOR);
        pq.addAll(animals.get(position));
        return pq;
    }

    private List<Animal> getWinningList(PriorityQueue<Animal> pq)
    {
        List<Animal> winningAnimals = new ArrayList<>();
        winningAnimals.add(pq.poll());

        int indexOfLastElement = 0;
        while (!pq.isEmpty())
        {
            Animal next = pq.peek();
            if ( ANIMAL_COMPARATOR.compare(next, winningAnimals.get(indexOfLastElement)) != 0)
            {
                break;
            }
            winningAnimals.add(next);
            pq.poll();
            indexOfLastElement++;
        }
        return winningAnimals;
    }
}
