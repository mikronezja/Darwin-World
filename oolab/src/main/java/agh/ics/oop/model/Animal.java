package agh.ics.oop.model;

public class Animal {

    private MapDirections direction = MapDirections.NORTH;
    private Vector2d position = new Vector2d(2,2);
    private static final Vector2d UPPER_RIGHT_MAP_CORNER = new Vector2d(4,4);
    private static final Vector2d LOWER_LEFT_MAP_CORNER = new Vector2d(0,0);

    public Animal(){}

    public Animal(Vector2d position){
        this.position = position;
    }

    public String toString(){
        return "Pozycja: %s, Kierunek: %s.".formatted(position.toString(),direction.toString());
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
                if (possibleMove.precedes(UPPER_RIGHT_MAP_CORNER) && possibleMove.follows(LOWER_LEFT_MAP_CORNER)){
                    this.position = possibleMove;
                }
            }
            case BACKWARD -> {
                Vector2d possibleMove = this.position.subtract(this.direction.toUnitVector());
                if (possibleMove.precedes(UPPER_RIGHT_MAP_CORNER) && possibleMove.follows(LOWER_LEFT_MAP_CORNER)){
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
