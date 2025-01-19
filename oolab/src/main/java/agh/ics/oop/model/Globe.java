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
    private final int everydayPlantsGrow;
    private final int howManyEnergyFromPlants;
    private RandomPositionForPlantsGenerator positionForPlantsGenerator;
    private Random random = new Random();
    private static final Comparator<Animal> ANIMAL_COMPARATOR = new AnimalComparator();
    private Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    private Map<Vector2d, Integer> recentDeadAnimals = new HashMap<>();
    protected List<MapChangeListener> observators = new ArrayList<>();
    private UUID id = UUID.randomUUID();
    private boolean ifAnimalsMoveSlowerWhenOlder;



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
        if (isOccupied(position)) {
            animals.get(position).add(animal);
        }
        else {
            animals.computeIfAbsent(position, k -> new TreeSet<>(ANIMAL_COMPARATOR))
                    .add(animal);
        }
        mapChanged("Zwierze zostało położone na "+position.toString());
    }

    @Override
    public synchronized void move(Animal animal) {

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
            else{animals.computeIfAbsent(animal.getPosition(), k -> new TreeSet<>(ANIMAL_COMPARATOR))
                    .add(animal);
            }
        }
        mapChanged("Długość listy pozycji: %s\nLista pozycji zwierząt: %s".formatted(animals.size(),animals));
        mapChanged("Zwierzę poruszyło się z %s na %s".formatted(positionBeforeMove, animal.getPosition()));
    }


    @Override
    public boolean isOccupied(Vector2d position) {
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
                        animals.values()
                                .stream()
                                .flatMap(TreeSet::stream)
                                .filter(Objects::nonNull),
                        plants.values().stream()
                                .filter(Objects::nonNull))
                .collect(Collectors.toList());
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public synchronized void killAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        TreeSet<Animal> onThisSpace = animals.get(position);
        onThisSpace.remove(animal);
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
            Plant plant = new Plant(howManyEnergyFromPlants, position);
            plants.put(position, plant);
            mapChanged("Roślinka wyrosła na %s".formatted(position));
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
        if (animals.containsKey(position) && !animals.get(position).isEmpty() && animals.get(position).size()>1)
        {
            SortedSet<Animal> winningAnimals = animals.get(position).headSet(animals.get(position).first(),true);
            Animal parent1, parent2;
            if (winningAnimals.size() == 2) {
                parent1 = winningAnimals.getFirst();
                winningAnimals.removeFirst();
                parent2 = winningAnimals.getFirst();
                winningAnimals.add(parent1);
                try{
                    Animal child = parent1.reproduce(parent2);
                    this.place(child);
                    mapChanged("Urodziło się nowe zwierzę na %s".formatted(position));
                }
                catch (IncorrectPositionException e) {
                    e.printStackTrace();
                }
            } else if (winningAnimals.size() == 1) {
                parent1 = winningAnimals.getFirst();
                winningAnimals.removeFirst();
                SortedSet<Animal> secondTurnWinningAnimals = animals.get(position).headSet(animals.get(position).first());
                winningAnimals.add(parent1);
                if (secondTurnWinningAnimals.size() == 1) {
                    parent2 = secondTurnWinningAnimals.getFirst();
                }
                else {
                    List<Animal> finalists = winningAnimals.stream().toList();
                    int howManyWinningAnimals = finalists.size();
                    int indexOfWinner = random.nextInt(howManyWinningAnimals);
                    parent2=finalists.get(indexOfWinner);
                }
                try{
                    Animal child = parent1.reproduce(parent2);
                    this.place(child);
                    mapChanged("Urodziło się nowe zwierzę na %s".formatted(position));
                }
                catch (IncorrectPositionException e) {
                    e.printStackTrace();
                }
            }
            else if(winningAnimals.size()>2){
                List<Animal> finalists = winningAnimals.stream().collect(Collectors.toList());
                int howManyWinningAnimals = finalists.size();
                int indexOfWinner = random.nextInt(howManyWinningAnimals);
                parent1=finalists.get(indexOfWinner);
                Collections.swap(finalists, indexOfWinner, finalists.size()-1);
                howManyWinningAnimals--;
                indexOfWinner = random.nextInt(howManyWinningAnimals);
                parent2=finalists.get(indexOfWinner);
                try{
                    Animal child = parent1.reproduce(parent2);
                    this.place(child);
                    mapChanged("Urodziło się nowe zwierzę na %s".formatted(position));
                }
                catch (IncorrectPositionException e) {
                    e.printStackTrace();
                }
            }



        }

    }

    private synchronized void animalsEatAt(Vector2d position)
    {
        if (plants.containsKey(position))
        {
            SortedSet<Animal> winningAnimals = animals.get(position).headSet(animals.get(position).first(), true);
            Animal winningAnimal;
            if( winningAnimals.size() == 1)
            {
                winningAnimal=winningAnimals.first();
                winningAnimal.eatPlant(plants.get(position));
                plants.remove(position);
                mapChanged("Zwierzę zjadło roślinkę na %s".formatted(position));
            } else if (winningAnimals.size() > 1)
            {
                List<Animal> finalists = winningAnimals.stream().toList();
                int howManyWinningAnimals = finalists.size();
                int indexOfWinner = random.nextInt(howManyWinningAnimals);
                winningAnimal=finalists.get(indexOfWinner);
                winningAnimal.eatPlant(plants.get(position));
                plants.remove(position);
                mapChanged("Zwierzę zjadło roślinkę na %s".formatted(position));
            }

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


}
