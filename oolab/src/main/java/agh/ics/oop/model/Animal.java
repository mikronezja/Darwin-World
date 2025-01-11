package agh.ics.oop.model;

import agh.ics.oop.Simulation;

public class Animal implements WorldElement
{

    private MapDirections direction;
    private Vector2d position;
    private int energy;
    private int ConsumedPlants = 0;
    private int howManyDaysIsAlive = 0;
    private int kidsNumber = 0;
    private Animal[] parents; // zależy czy jest pierwszy czy ma jakichś rodziców
    private Genome genome;
    private int currentGenomeIndex;

    // usuniete info do wymiaru mapki

//    public Animal(){
//        this(new Vector2d(2,2));
//    }

    // starting position of Animal
    public Animal(Vector2d position)
    {
        this.position = position; // generates a random position
        this.energy = Simulation.getStartingEnergy();
        this.genome = new Genome();
        generateStartingGenomeIndex();
        direction = MapDirections.values()[ this.getGenomeAsIntList()[currentGenomeIndex] ]; // randomly generates how its turned
    }

    public Animal(Vector2d position, int startEnergyLevel, Animal[] parents)
    {
        this.position = parents[0].getPosition(); // generates a position determined by its parents
        this.energy = startEnergyLevel;
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

    public void move()
    {
        direction.nextByN(this.getGenomeAsIntList()[ this.currentGenomeIndex ]); // obrot zwierzaka w danym kierunku
        Vector2d position = this.position.add(this.direction.toUnitVector()); // pozycja do ktorej chce sie poruszyc
        // ...
    }


    private void generateStartingGenomeIndex()
    {
        this.currentGenomeIndex = (int)Math.round(Math.random() * (Simulation.getGenomLength() - 1) );
    }

    private void increaseGenomIndex()
    {
        this.currentGenomeIndex = (this.currentGenomeIndex + 1) % Simulation.getGenomLength();
    }

    public int getEnergy() { return energy; }
    public MapDirections getDirection() {
        return direction;
    }
    public Vector2d getPosition() {
        return position;
    }
    public int[] getGenomeAsIntList() { return genome.getGenome(); }
}
