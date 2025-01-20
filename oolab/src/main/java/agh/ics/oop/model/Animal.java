package agh.ics.oop.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Animal implements WorldElement
{



    private MapDirections direction;
    private Vector2d position;
    private int energy;
    private int initialStartingGenomeIndex;
    private int consumedPlants = 0;

    private int daysAlive = 0;

    private boolean ifAnimalsMoveSlowerWhenOlder = false;
    private int probabilityOfNotMoving = 0;

    private static int howManyAnimals = 0;
    private final int index;


    private Set<Animal> kids = new HashSet<>();
    private Set<Animal> descendants = new HashSet<>();

    private final Animal[] parents; // zależy czy jest pierwszy czy ma jakichś rodziców
    private Genome genome;
    private int currentGenomeIndex;


    private final int minReproductionEnergy;
    private final int subtractingEnergyWhileReproducing;
    private final int minNumberOfmutations;
    private final int maxNumberOfmutations;

    // starting position of Animal
    public Animal(Vector2d position, int genomLength, int startingEnergy, int minReproductionEnergy, int subtractingEnergyWhileReproducing, int minNumberOfmutations, int maxNumberOfmutations, boolean ifAnimalsMoveSlowerWhenOlder)
    {
        this.position = position;
        this.minReproductionEnergy = minReproductionEnergy;
        this.subtractingEnergyWhileReproducing = subtractingEnergyWhileReproducing;
        this.minNumberOfmutations = minNumberOfmutations;
        this.maxNumberOfmutations = maxNumberOfmutations;
        this.energy = startingEnergy;
        this.genome = new Genome(genomLength, minNumberOfmutations, maxNumberOfmutations);
        parents = null;
        generateStartingGenomeIndex();
        direction = MapDirections.values()[ this.getGenomeAsIntList()[currentGenomeIndex] ]; // randomly generates how its turned
        index=howManyAnimals;
        howManyAnimals++;
    }

    // if Animal has been created by
    public Animal(Vector2d position, Animal[] parents)
    {
        this.position = position;
        this.energy = parents[0].getSubtractingEnergyWhileReproducing() * 2;
        this.minReproductionEnergy = parents[0].getMinReproductionEnergy();
        this.subtractingEnergyWhileReproducing = parents[0].getSubtractingEnergyWhileReproducing();
        this.minNumberOfmutations = parents[0].getMinNumberOfmutations();
        this.maxNumberOfmutations = parents[0].getMaxNumberOfmutations();
        this.parents = parents;
        this.genome = new Genome(parents[0].getGenomeAsIntList(),parents[0].getEnergy(),parents[1].getGenomeAsIntList(),parents[1].getEnergy(),minNumberOfmutations, maxNumberOfmutations);
        generateStartingGenomeIndex();
        direction = MapDirections.values()[ this.getGenomeAsIntList()[currentGenomeIndex] ];
        index=howManyAnimals;
        howManyAnimals++;
    }

    public String toString(){
        return direction.toString();
    }



    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }


    public void move(MoveValidator moveValidator)
    {
        // corrected position based on map coordinates
        if (!ifAnimalsMoveSlowerWhenOlder || Math.min(daysAlive / 100, 0.8) < Math.random()) {
                this.direction = direction.nextByN(this.getGenomeAsIntList()[this.currentGenomeIndex]); // obrot zwierzaka w danym kierunku
                Vector2d possibleMove = this.position.add(this.direction.toUnitVector()); // pozycja do ktorej chce sie poruszyc

                if (!moveValidator.canMoveTo(possibleMove)) {
                    Boundary boundary = moveValidator.getCurrentBounds();
                    if (possibleMove.getX() < boundary.lowerLeftCorner().getX()) // lewo
                    {
                        possibleMove = new Vector2d(boundary.upperRightCorner().getX(), possibleMove.getY());
                    }
                    if (possibleMove.getY() > boundary.upperRightCorner().getY() ||
                            possibleMove.getY() < boundary.lowerLeftCorner().getY()
                    ) /// gora lub dol
                    {
                        possibleMove = this.position;
                        this.direction = direction.nextByN(4); // obrot o 180 stopni
                    }
                    if (possibleMove.getX() > boundary.upperRightCorner().getX()) {
                        possibleMove = new Vector2d(boundary.lowerLeftCorner().getX(), possibleMove.getY());
                    }
                }
            this.position = possibleMove;
        }
        --energy;
        increaseGenomeIndex();
    }


    private void generateStartingGenomeIndex()
    {
        this.currentGenomeIndex = Math.min(Math.max(0,(int)Math.round(Math.random() * (genome.getGenome().length - 1))),genome.getGenome().length);
        initialStartingGenomeIndex = currentGenomeIndex;
    }

    private void increaseGenomeIndex()
    {
        this.currentGenomeIndex = (this.currentGenomeIndex + 1) % genome.getGenome().length;
    }

    private void decreaseEnergyLevelSinceAnimalReproduced()
    {
        this.energy -= this.subtractingEnergyWhileReproducing;
    }

    private void addKid(Animal kid)
    {
        kids.add(kid);
    }

    private void addDescendantsToAllParents(Animal descendant)
    {
        this.descendants.add(descendant);
        if (parents!=null){
            for (Animal parent : parents)
            {
                parent.addDescendantsToAllParents(descendant);
            }
        }

    }

    public Animal reproduce(Animal parent1)
    {
        this.decreaseEnergyLevelSinceAnimalReproduced();
        parent1.decreaseEnergyLevelSinceAnimalReproduced();

        Animal kid = new Animal(this.getPosition(), new Animal[]{this,parent1});
        this.addKid(kid);
        parent1.addKid(kid);

        // idz do wszystkich parentow obu i dodaj potomka do nich
        this.addDescendantsToAllParents(kid);
        parent1.addDescendantsToAllParents(kid);

        return kid;
    }

    public void increaseDaysAlive()
    {
        ++daysAlive;
    }

    public void eatPlant(Plant plant)
    {
        this.energy += plant.getEnergy();
        this.consumedPlants += 1;
    }

    public boolean isAlive()
    {
        return this.energy > 0;
    }

    public int getEnergy() { return energy; }
    public MapDirections getDirection() {
        return direction;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }
    public int[] getGenomeAsIntList() { return genome.getGenome(); }
    public int getKidsNumber() { return kids.size(); }
    public int getDescendantsNumber() { return descendants.size(); }
    public int getDaysAlive() { return daysAlive; }
    public Genome getGenome() {return genome;}
    public int getSubtractingEnergyWhileReproducing() {
        return subtractingEnergyWhileReproducing;
    }
    public int getMinNumberOfmutations() {
        return minNumberOfmutations;
    }
    public int getMaxNumberOfmutations() {
        return maxNumberOfmutations;
    }
    public int getMinReproductionEnergy() {
        return minReproductionEnergy;
    }
    public int getInitialStartingGenomeIndex() {return initialStartingGenomeIndex;}
    public int getIndex() {
        return index;
    }
}
