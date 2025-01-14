package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Animal implements WorldElement
{

    private MapDirections direction;
    private Vector2d position;
    private int energy;
    private int consumedPlants = 0;
    private int howManyDaysIsAlive = 0;
    private int probabilityOfNotMoving = 0;

    private Set<Animal> kids = new HashSet<>();
    private Set<Animal> descendants = new HashSet<>();

    private final Animal[] parents; // zależy czy jest pierwszy czy ma jakichś rodziców
    private Genome genome;
    private int currentGenomeIndex;
    // usuniete info do wymiaru mapki

    // starting position of Animal
    public Animal(Vector2d position)
    {
        this.position = position; // generates a random position
        this.energy = Simulation.getStartingEnergy();
        this.genome = new Genome();
        parents = null;
        generateStartingGenomeIndex();
        direction = MapDirections.values()[ this.getGenomeAsIntList()[currentGenomeIndex] ]; // randomly generates how its turned
    }

    // if Animal has been created by
    public Animal(Vector2d position, Animal[] parents)
    {
        this.position = parents[0].getPosition(); // generates a position determined by its parents
        this.energy = Simulation.getSubtractingEnergyWhileReproducing() * 2;
        this.parents = parents;
        this.genome = new Genome(parents[0].getGenomeAsIntList(),parents[0].getEnergy(),parents[1].getGenomeAsIntList(),parents[1].getEnergy());
        generateStartingGenomeIndex();
        direction = MapDirections.values()[ this.getGenomeAsIntList()[currentGenomeIndex] ];
    }

    public String toString(){
        return direction.toString();
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void move(MoveDirections direction, MoveValidator moveValidator){
        switch (direction) {
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            case FORWARD -> {
                Vector2d possibleMove = this.position.add(this.direction.toUnitVector());
                if (moveValidator.canMoveTo(possibleMove)) {
                    this.position = possibleMove;
                }
            }
            case BACKWARD -> {
                Vector2d possibleMove = this.position.subtract(this.direction.toUnitVector());
                if (moveValidator.canMoveTo(possibleMove)) {
                    this.position = possibleMove;
                }
            }
        }
    }

    public void move(MoveValidator moveValidator)
    {
        // corrected position based on map coordinates
        this.direction = direction.nextByN(this.getGenomeAsIntList()[ this.currentGenomeIndex ]); // obrot zwierzaka w danym kierunku
        Vector2d possibleMove = this.position.add(this.direction.toUnitVector()); // pozycja do ktorej chce sie poruszyc

        if (!moveValidator.canMoveTo(possibleMove))
        {
                if (possibleMove.getX() < moveValidator.lowerLeftCorner().getX()) // lewo
                {
                    possibleMove = new Vector2d(moveValidator.upperRightCorner().getX(), possibleMove.getY());
                }
                if (possibleMove.getY() > moveValidator.upperRightCorner().getY() ||
                    possibleMove.getY() < moveValidator.lowerLeftCorner().getY()
                ) /// gora lub dol
                {
                    possibleMove = this.position;
                    this.direction = direction.nextByN(4); // obrot o 180 stopni
                }
                if (possibleMove.getX() > moveValidator.upperRightCorner().getX())
                {
                    possibleMove = new Vector2d(moveValidator.lowerLeftCorner().getX(), possibleMove.getY());
                }
        }
        this.position = possibleMove;
    }


    private void generateStartingGenomeIndex()
    {
        this.currentGenomeIndex = (int)Math.round(Math.random() * (Simulation.getGenomLength() - 1) );
    }

    private void increaseGenomeIndex()
    {
        this.currentGenomeIndex = (this.currentGenomeIndex + 1) % Simulation.getGenomLength();
    }

    private void decreaseEnergyLevelSinceAnimalReproduced()
    {
        this.energy -= Simulation.getSubtractingEnergyWhileReproducing();
    }

    private void addKid(Animal kid)
    {
        kids.add(kid);
    }

    private void addDescendantsToAllParents(Animal descendant)
    {
        this.descendants.add(descendant);
        for (Animal parent : parents)
        {
            parent.addDescendantsToAllParents(descendant);
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
    public Vector2d getPosition() {
        return position;
    }
    public int[] getGenomeAsIntList() { return genome.getGenome(); }
    public int getKidsNumber() { return kids.size(); }
    public int getDescendantsNumber() { return descendants.size(); }
}
