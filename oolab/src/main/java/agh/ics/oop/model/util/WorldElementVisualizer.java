package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WorldElementVisualizer {

    private Image northImage = new Image("NORTH.png");
    private Image northEastImage = new Image("NORTH_EAST.png");
    private Image eastImage = new Image("EAST.png");
    private Image southEastImage = new Image("SOUTH_EAST.png");
    private Image southImage = new Image("SOUTH.png");
    private Image southWestImage = new Image("SOUTH_WEST.png");
    private Image westImage = new Image("WEST.png");
    private Image northWestImage = new Image("NORTH_WEST.png");


    private Image plant = new Image("plant.png");

    private Image defaultImage = new Image("ananas.png");

    //pusty konstruktor bo nie potrzebujemy przesyłać żadnych argumentów
    public WorldElementVisualizer(){}

    public ImageView getImageView(WorldElement element) {
        return switch (element.toString()){
            case "^" -> new ImageView(northImage);
            case "◥" -> new ImageView(northEastImage);
            case ">" -> new ImageView(eastImage);
            case "◢" -> new ImageView(southEastImage);
            case "v" -> new ImageView(southImage);
            case "◣" -> new ImageView(southWestImage);
            case "<" -> new ImageView(westImage);
            case "◤" -> new ImageView(northWestImage);
            case "#" -> new ImageView(plant);
            default -> new ImageView(defaultImage);
        };
    }

}
