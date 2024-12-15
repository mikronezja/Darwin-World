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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SimulationMenuPresenter{

    private WorldMap worldMap;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField moveInput;
    @FXML
    private VBox bottomElements;


    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        mainBorderPane.setMargin(moveInput, new Insets(12,12,12,12));
        mainBorderPane.setMargin(bottomElements, new Insets(12,12,12,12));
    }



    @FXML
    private void onSimulationStartClicked() throws Exception {
        Stage simulationStage = new Stage();
        simulationStage.show();
        FXMLLoader simulationLoader = new FXMLLoader();
        simulationLoader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = simulationLoader.load();
        SimulationWindowPresenter simulationPresenter = simulationLoader.getController();
        configureStage(simulationStage, viewRoot);

        AbstractWorldMap map = new GrassField(5);
        map.addObservator(simulationPresenter);
        simulationPresenter.setWorldMap(map, moveInput.getText());
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
