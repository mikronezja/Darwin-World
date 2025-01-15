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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

public class SimulationMenuPresenter{

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField heightInput;
    @FXML
    private TextField widthInput;

    @FXML
    private TextField howManyPlantsInput;
    @FXML
    private TextField howManyEnergyFromPlantInput;
    @FXML
    private TextField howManyPlantsGrowEverydayInput;
    @FXML
    private CheckBox plantsPreferDeadBodiesCheckbox;

    @FXML
    private TextField howManyAnimalsOnStartInput;
    @FXML
    private TextField howManyStartingEnergyAnimalHaveInput;
    @FXML
    private TextField howLongGenomWillBeInput;
    @FXML
    private CheckBox ifAnimalsMoveSlowerWhenOlderCheckbox;
    @FXML
    private TextField energeyNeededToReproduceInput;
    @FXML
    private TextField energyUsedToReproduceInput;
    @FXML
    private TextField minNumberOfMutationInput;
    @FXML
    private TextField maxNumberOfMutationInput;





    @FXML
    private void onSimulationStartClicked() throws Exception {
        Stage simulationStage = new Stage();
        simulationStage.show();
        FXMLLoader simulationLoader = new FXMLLoader();
        simulationLoader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = simulationLoader.load();
        SimulationWindowPresenter simulationPresenter = simulationLoader.getController();
        configureStage(simulationStage, viewRoot);
        Globe map = new Globe(parseInt(heightInput.getText()), parseInt(widthInput.getText()), parseInt(howManyPlantsInput.getText()), parseInt(howManyEnergyFromPlantInput.getText()), parseInt(howManyPlantsGrowEverydayInput.getText()));
        map.addObservator(simulationPresenter);
        simulationPresenter.setupSimulation(map, parseInt(howManyAnimalsOnStartInput.getText()), parseInt(howManyStartingEnergyAnimalHaveInput.getText()), parseInt(energeyNeededToReproduceInput.getText()), parseInt(energyUsedToReproduceInput.getText()), parseInt(minNumberOfMutationInput.getText()), parseInt(maxNumberOfMutationInput.getText()), parseInt(howLongGenomWillBeInput.getText()), simulationStage);
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

}
