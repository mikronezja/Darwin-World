package agh.ics.oop.model;

import javafx.scene.image.Image;

public class Grass implements WorldElement {

    private final Vector2d position;
    Image grassImage = new Image("plant.png");

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition(){
        return position;
    }

    public String toString() {
        return "*";
    }

    @Override
    public Image getStateOfImage() {
        return grassImage;
    }


}
