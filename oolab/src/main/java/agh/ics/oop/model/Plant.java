package agh.ics.oop.model;

import javafx.scene.image.Image;

public class Plant implements WorldElement {

    private int energy;
    private Vector2d position;
    private Image image = new Image("plant.png");

    public Plant(int energy, Vector2d position) {
        this.energy = energy;
        this.position = position;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "#";
    }

    @Override
    public Image getStateOfImage() {
        return image;
    }
}
