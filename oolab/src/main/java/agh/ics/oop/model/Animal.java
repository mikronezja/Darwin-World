package agh.ics.oop.model;

public class Animal implements WorldElement{

    private MapDirections direction = MapDirections.NORTH;
    private Vector2d position;

    // usuniete info do wymiaru mapki

    public Animal(){
        this(new Vector2d(2,2));
    }

    public Animal(Vector2d position){
        this.position = position;
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

    public MapDirections getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }
}
