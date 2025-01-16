package agh.ics.oop.model;

import java.util.List;
import java.util.UUID;

public interface ProjectWorldMap extends MoveValidator{

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     *               It throws exception instead of returning
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    List<WorldElement> objectsAt(Vector2d position);


    /**
     *
     * @return List of <WorldElement>
     *
     */

    List<WorldElement> getElements();

    /**
     *
     * @return id of map
     *
     */

    UUID getID();

    /**
     * Kill an animal - it means move it to delete it from animal map
     * @param animal
     */

    void killAnimal(Animal animal);

    /**
     * Make animals love each other
     */
    void animalsReproducing();

    /**
     * Growing plants on our Globe is very important
     */
    void growPlants();

    /**
     * When out animals are hungry
     */
    void eatingPlants();

    /**
     *
     */
    boolean isPositionMoreDesirableForPlants(Vector2d position);
}
