package agh.ics.oop.model;

import javafx.scene.image.Image;

public interface WorldElement {

    Vector2d getPosition();

    @Override
    String toString();

    Image getStateOfImage();
}
