package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.List;

public class SimulationWindowPresenter implements MapChangeListener {

    private WorldMap worldMap;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label whatMoveHappened;
    @FXML
    private VBox bottomElements;


    public void setWorldMap(WorldMap worldMap, String moveInput) {
        this.worldMap = worldMap;
        mainBorderPane.setMargin(bottomElements, new Insets(12,12,12,12));
        mainBorderPane.setMargin(mapGrid, new Insets(12,12,12,12));
        List<Vector2d> positions = List.of(new Vector2d(1,2),new Vector2d(3,4));
        List<MoveDirections> directions = OptionsParser.translateDirections(moveInput.split(" "));
        Simulation simulation = new Simulation(positions, directions, worldMap);
        List<Simulation> simulationsList = List.of(simulation);
        SimulationEngine simulationEngine = new SimulationEngine(simulationsList);
        simulationEngine.runAsync();
    }

    public void drawMap(WorldMap map) {
        clearGrid();
        Boundary boundary = map.getCurrentBounds();
        Label label = new Label();
        label.setText("y/x");
        mapGrid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);
        int relativeShiftOfX = 1 - boundary.lowerLeftCorner().getX();
        int relativeShiftOfY = 1 + boundary.upperRightCorner().getY();
        int widthtOfMap = boundary.upperRightCorner().getX() - boundary.lowerLeftCorner().getX() + 1;
        int heightOfMap = boundary.upperRightCorner().getY() - boundary.lowerLeftCorner().getY() + 1;

        int cellWidth = 50;
        int cellHight = 50;

        for (int i = 0; i < widthtOfMap;i++){
            Label coordinates = new Label();
            coordinates.setText(String.valueOf((boundary.lowerLeftCorner().getX()+i)));
            mapGrid.add(coordinates, i+ 1, 0);
            GridPane.setHalignment(coordinates, HPos.CENTER);
        }
        for (int i = 0; i < heightOfMap;i++){
            Label coordinates = new Label();
            coordinates.setText(String.valueOf((boundary.upperRightCorner().getY()-i)));
            mapGrid.add(coordinates, 0, i+1);
            GridPane.setHalignment(coordinates, HPos.CENTER);
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
            Label animal = new Label();
            animal.setText(element.toString());
            mapGrid.add(animal, positionOfElement.getX() + relativeShiftOfX, relativeShiftOfY - positionOfElement.getY());
            GridPane.setHalignment(animal, HPos.CENTER);
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            whatMoveHappened.setText(message);
        });

    }
}
