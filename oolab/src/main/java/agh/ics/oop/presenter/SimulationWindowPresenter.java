package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

public class SimulationWindowPresenter implements MapChangeListener {

    private ProjectWorldMap worldMap;

    private Stage stage;

    private Image northImage = new Image("NORTH.png");
    private Image northEastImage = new Image("NORTH_EAST.png");
    private Image eastImage = new Image("EAST.png");
    private Image southEastImage = new Image("SOUTH_EAST.png");
    private Image southImage = new Image("SOUTH.png");
    private Image southWestImage = new Image("SOUTH_WEST.png");
    private Image westImage = new Image("WEST.png");
    private Image northWestImage = new Image("NORTH_WEST.png");
    private Image tile = new Image("tile.png");
    private Image equator = new Image("equator.png");
    private Image plant = new Image("plant.png");



    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private GridPane mapGrid;



    public void setupSimulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith, int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn, int maxMutationInNewborn, int genomeLength, Stage simulationStage) {
        this.worldMap = worldMap;
        stage = simulationStage;
        mainBorderPane.setMargin(mapGrid, new Insets(12,12,12,12));
        Simulation simulation = new Simulation(worldMap, howManyAnimalsToStartWith, howManyEnergyAnimalsStartWith, energyNeededToReproduce, energyGettingPassedToDescendant,minMutationInNewborn, maxMutationInNewborn, genomeLength);
        List<Simulation> simulationsList = List.of(simulation);
        SimulationEngine simulationEngine = new SimulationEngine(simulationsList);
        simulationEngine.runAsync();
    }

    public void drawMap(ProjectWorldMap map) {
        clearGrid();
        Boundary boundary = map.getCurrentBounds();
        Label label = new Label();
        GridPane.setHalignment(label, HPos.CENTER);
        int widthtOfMap = boundary.upperRightCorner().getX();
        int heightOfMap = boundary.upperRightCorner().getY();

        double windowWidthToMapWidthRatio = stage.getWidth() / widthtOfMap;
        double windowHeightToMapWidthRatio = stage.getHeight() / heightOfMap;

        int cellWidth = 50;
        int cellHight = 50;

        for (int i = 0; i <= widthtOfMap;i++){
            for (int j = 0; j <= heightOfMap;j++){
                Label coordinates = new Label();
                coordinates.setText(".");
                mapGrid.add(coordinates, i, j);
                GridPane.setHalignment(coordinates, HPos.CENTER);
            }
        }
        for (int i = 0; i < widthtOfMap+1;i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        }
        for (int i = 0; i < heightOfMap+1;i++){
            mapGrid.getRowConstraints().add(new RowConstraints(cellHight));
        }
        List<WorldElement> elements = map.getElements();
        for (WorldElement element : elements ){
            Vector2d positionOfElement = element.getPosition();
            ImageView animal = new ImageView();
            mapGrid.add(animal, positionOfElement.getX() , heightOfMap - positionOfElement.getY());
            GridPane.setHalignment(animal, HPos.CENTER);
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(ProjectWorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
        });

    }
}
