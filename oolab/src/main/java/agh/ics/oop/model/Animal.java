package agh.ics.oop.model;

public class Animal {

    private MapDirections direction = MapDirections.NORTH;
    private Vector2d position;
    private static final Vector2d UPPER_RIGHT_MAP_CORNER = new Vector2d(4,4);
    private static final Vector2d LOWER_LEFT_MAP_CORNER = new Vector2d(0,0);

    public Animal(){
        this(new Vector2d(2,2));
    }

    public Animal(Vector2d position){
        this.position = position;
    }

    public String toString(){
        return "Pozycja: %s, Orientacja: %s".formatted(position.toString(),direction.toString());
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void move(MoveDirections direction){
        switch (direction){
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            case FORWARD -> {
                Vector2d possibleMove = this.position.add(this.direction.toUnitVector());
                if (ifMovePossible(possibleMove)){
                    this.position = possibleMove;
                }
            }
            case BACKWARD -> {
                Vector2d possibleMove = this.position.subtract(this.direction.toUnitVector());
                if (ifMovePossible(possibleMove)){
                    this.position = possibleMove;
                }
            }
        }
    }

    private boolean ifMovePossible(Vector2d position){
        return position.precedes(UPPER_RIGHT_MAP_CORNER) && position.follows(LOWER_LEFT_MAP_CORNER);
    }

    public MapDirections getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }
}
