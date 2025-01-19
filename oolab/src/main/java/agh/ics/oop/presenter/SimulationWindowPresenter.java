package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.WorldElementVisualizer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class SimulationWindowPresenter implements MapChangeListener {

    private ProjectWorldMap worldMap;

    private Stage stage;

    private WorldElementVisualizer worldElementVisualizer = new WorldElementVisualizer();

    private Image tile = new Image("tile.png");
    private Image equator = new Image("equtor.png");

    private SimulationEngine simulationEngine;

    private boolean isPaused = false;



    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button pauseAndResumeButton;



    public void setupSimulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith,
                                int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn, int maxMutationInNewborn,
                                int genomeLength, boolean ifAnimalsMoveSlowerWhenOlder, boolean writeIntoACSVFile,
                                Stage simulationStage) {
        this.worldMap = worldMap;
        stage = simulationStage;
        mainBorderPane.setMargin(mapGrid, new Insets(12,12,12,12));
        Simulation simulation = new Simulation(worldMap, howManyAnimalsToStartWith, howManyEnergyAnimalsStartWith,
                energyNeededToReproduce, energyGettingPassedToDescendant,minMutationInNewborn, maxMutationInNewborn,
                genomeLength, ifAnimalsMoveSlowerWhenOlder, writeIntoACSVFile);
        Map<UUID, Simulation> simulationsMap = new HashMap<>();
        simulationsMap.put(worldMap.getID(), simulation);
        simulationEngine = new SimulationEngine(simulationsMap);
        simulationEngine.runAsync();
    }

    public void drawMap(ProjectWorldMap map) {
        clearGrid();
        Boundary boundary = map.getCurrentBounds();
        Label label = new Label();
        GridPane.setHalignment(label, HPos.CENTER);
        int widthtOfMap = boundary.upperRightCorner().getX();
        int heightOfMap = boundary.upperRightCorner().getY();

        double windowWidthToMapWidthRatio = (stage.getWidth()-100.0) / (widthtOfMap+1);
        double windowHeightToMapWidthRatio = (stage.getHeight()-100.0) / (heightOfMap+1);

        int cellSideLength = (int)(min(windowHeightToMapWidthRatio,windowWidthToMapWidthRatio));

        for (int i = 0; i <= widthtOfMap;i++){
            for (int j = 0; j <= heightOfMap;j++){

                ImageView tileView = new ImageView(tile);
                tileView.setFitHeight(cellSideLength);
                tileView.setFitWidth(cellSideLength);
                mapGrid.add(tileView, i, j);
            }
        }
        for (int i = 0; i < widthtOfMap+1;i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSideLength));
        }
        for (int i = 0; i < heightOfMap+1;i++){
            mapGrid.getRowConstraints().add(new RowConstraints(cellSideLength));
        }
        List<WorldElement> elements = map.getElements();
        for (WorldElement element : elements ){
            Vector2d positionOfElement = element.getPosition();
            ImageView animal = worldElementVisualizer.getImageView(element);
            animal.setFitHeight(cellSideLength);
            animal.setFitWidth(cellSideLength);
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

    @FXML
    private void onPauseAndResumeButtonClicked(){
        if (!isPaused){
            simulationEngine.pauseSpecificSimulation(worldMap.getID());
            pauseAndResumeButton.setText("Resume Button");
            Platform.runLater(() -> {
                drawMapOnPause(worldMap);
            });
        }
        else{
            simulationEngine.resumeSpecificSimulation(worldMap.getID());
            pauseAndResumeButton.setText("Pause Button");
        }
        isPaused = !isPaused;
    }


    public void drawMapOnPause(ProjectWorldMap map) {
        clearGrid();
        Boundary boundary = map.getCurrentBounds();
        Label label = new Label();
        GridPane.setHalignment(label, HPos.CENTER);
        int widthtOfMap = boundary.upperRightCorner().getX();
        int heightOfMap = boundary.upperRightCorner().getY();

        double windowWidthToMapWidthRatio = (stage.getWidth()-100.0) / (widthtOfMap+1);
        double windowHeightToMapWidthRatio = (stage.getHeight()-100.0) / (heightOfMap+1);

        int cellSideLength = windowHeightToMapWidthRatio<windowWidthToMapWidthRatio ? (int)windowHeightToMapWidthRatio : (int)windowWidthToMapWidthRatio;


        for (int i = 0; i <= widthtOfMap;i++){
            for (int j = 0; j <= heightOfMap;j++){

                ImageView tileView;
                if ( map.isPositionMoreDesirableForPlants(new Vector2d(i, heightOfMap - j)))
                    tileView = new ImageView(equator);
                else
                    tileView = new ImageView(tile);
                tileView.setFitHeight(cellSideLength);
                tileView.setFitWidth(cellSideLength);
                mapGrid.add(tileView, i, j);
            }
        }
        for (int i = 0; i < widthtOfMap+1;i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSideLength));
        }
        for (int i = 0; i < heightOfMap+1;i++){
            mapGrid.getRowConstraints().add(new RowConstraints(cellSideLength));
        }
        List<WorldElement> elements = map.getElements();
        for (WorldElement element : elements ){
            Vector2d positionOfElement = element.getPosition();
            ImageView animal = worldElementVisualizer.getImageView(element);
            animal.setFitHeight(cellSideLength);
            animal.setFitWidth(cellSideLength);
            mapGrid.add(animal, positionOfElement.getX() , heightOfMap - positionOfElement.getY());
            GridPane.setHalignment(animal, HPos.CENTER);
        }
    }
}
