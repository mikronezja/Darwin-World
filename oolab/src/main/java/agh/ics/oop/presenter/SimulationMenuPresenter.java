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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private CheckBox shouldWriteToCSV;
    @FXML
    private ChoiceBox configurationChoise;
    @FXML
    private TextField nameOfConfiguration;


    public void loadConfigurations() {
        System.out.println(System.getProperty("user.dir"));
        Path dirPath = Paths.get("src/main/resources/configurations");

        try {
            Files.walk(dirPath, 1)
                    .filter(path -> !path.equals(dirPath))  // Skip the directory itself
                    .forEach(path -> {
                        configurationChoise.getItems().add(path.getFileName().toString().replace(".txt",""));
                    });
        } catch (IOException e) {
            System.err.println("Error accessing directory: " + e.getMessage());
        }
        configurationChoise.setOnAction((event) -> onConfigurationChoiseChange());
    }

    @FXML
    private void onSimulationStartClicked() throws Exception {
        if(allConditionsAreGood()){
            Stage simulationStage = new Stage();
            simulationStage.show();
            FXMLLoader simulationLoader = new FXMLLoader();
            simulationLoader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
            BorderPane viewRoot = simulationLoader.load();
            SimulationWindowPresenter simulationPresenter = simulationLoader.getController();
            configureStage(simulationStage, viewRoot);
            Globe map = new Globe(parseInt(heightInput.getText()), parseInt(widthInput.getText()), parseInt(howManyPlantsInput.getText()), parseInt(howManyEnergyFromPlantInput.getText()), parseInt(howManyPlantsGrowEverydayInput.getText()),ifAnimalsMoveSlowerWhenOlderCheckbox.isSelected(), plantsPreferDeadBodiesCheckbox.isSelected());
            map.addObservator(simulationPresenter);
            FileMapDisplay fileMapDisplay = new FileMapDisplay();
            map.addObservator(fileMapDisplay);
            simulationPresenter.setupSimulation(map, parseInt(howManyAnimalsOnStartInput.getText()), parseInt(howManyStartingEnergyAnimalHaveInput.getText()), parseInt(energeyNeededToReproduceInput.getText()), parseInt(energyUsedToReproduceInput.getText()), parseInt(minNumberOfMutationInput.getText()), parseInt(maxNumberOfMutationInput.getText()),
                    parseInt(howLongGenomWillBeInput.getText()), ifAnimalsMoveSlowerWhenOlderCheckbox.isSelected(), shouldWriteToCSV.isSelected(),
                    simulationStage);
        }

    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    private boolean allConditionsAreGood() {

        String alertMessage = buildALertMessage();
        if (!alertMessage.equals("Too big values:\n\nToo small values:\n")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(alertMessage);
            alert.showAndWait();
            return false;
        }
        else{
            return true;
        }
    }

    private String buildALertMessage(){

        String tooBigValues="Too big values:\n";
        String tooSmallValues="Too small values:\n";

        if (parseInt(heightInput.getText())>50){
            tooBigValues+="- Height of Map\n";
        } else if (parseInt(heightInput.getText())<3){
            tooSmallValues+="- Height of Map\n";
        }

        if (parseInt(widthInput.getText())>50){
            tooBigValues+="- Width of Map\n";
        } else if (parseInt(widthInput.getText())<3){
            tooSmallValues+="- Width of Map\n";
        }

        int surfaceOfMap = parseInt(heightInput.getText())*parseInt(heightInput.getText());

        if (parseInt(howManyPlantsInput.getText())>surfaceOfMap){
            tooBigValues+="- How many starting plants\n";
        } else if (parseInt(howManyPlantsInput.getText())<0){
            tooSmallValues+="- How many starting plants\n";
        }
        
        if (parseInt(howManyEnergyFromPlantInput.getText())>1000){
            tooBigValues+="- Energy from eating one plant\n";
        } else if (parseInt(howManyEnergyFromPlantInput.getText())<0){
            tooSmallValues+="- Energy from eating one plant\n";
        }
        
        if (parseInt(howManyPlantsGrowEverydayInput.getText())>surfaceOfMap){
            tooBigValues+="- How many plants can grow every day\n";
        } else if (parseInt(howManyPlantsGrowEverydayInput.getText())<0){
            tooSmallValues+="- How many plants can grow every day\n";
        }
        
        if (parseInt(howManyAnimalsOnStartInput.getText())>surfaceOfMap){
            tooBigValues+="- How many animals will be on start\n";
        } else if (parseInt(howManyAnimalsOnStartInput.getText())<0){
            tooSmallValues+="- How many animals will be on start\n";
        }
        
        if (parseInt(howManyStartingEnergyAnimalHaveInput.getText())>parseInt(energeyNeededToReproduceInput.getText())*5){
            tooBigValues+="- With how many energy animals will be starting\n";
        } else if (parseInt(howManyStartingEnergyAnimalHaveInput.getText())<1){
            tooSmallValues+="- With how many energy animals will be starting\n";
        }
        
        if (parseInt(howLongGenomWillBeInput.getText())>50){
            tooBigValues+="- How long genom of one animal will be\n";
        } else if (parseInt(howLongGenomWillBeInput.getText())<1){
            tooSmallValues+="- How long genom of one animal will be\n";
        }

        if (parseInt(energeyNeededToReproduceInput.getText())>100){
            tooBigValues+="- Energy that animal will be full and ready to reproduce\n";
        } else if (parseInt(energeyNeededToReproduceInput.getText())<1){
            tooSmallValues+="- Energy that animal will be full and ready to reproduce\n";
        }
        
        if (parseInt(energyUsedToReproduceInput.getText())>parseInt(energeyNeededToReproduceInput.getText())){
            tooBigValues+="- Energy that will be used to reproduce\n";
        } else if (parseInt(energyUsedToReproduceInput.getText())<1){
            tooSmallValues+="- Energy that will be used to reproduce\n";
        }
        
        if (parseInt(minNumberOfMutationInput.getText())>parseInt(maxNumberOfMutationInput.getText())){
            tooBigValues+="- Minimum number of mutation in descendant\n";
        } else if (parseInt(minNumberOfMutationInput.getText())<0){
            tooSmallValues+="- Minimum number of mutation in descendant\n";
        }
        
        if (parseInt(maxNumberOfMutationInput.getText())>parseInt(howLongGenomWillBeInput.getText())){
            tooBigValues+="- Maximum number of mutation in descendant\n";
        } else if (parseInt(maxNumberOfMutationInput.getText())<parseInt(minNumberOfMutationInput.getText())){
            tooSmallValues+="- Maximum number of mutation in descendant\n";
        }


        return "%s\n%s".formatted(tooBigValues, tooSmallValues);
    }

    private void onConfigurationChoiseChange(){
        String selectedItem = configurationChoise.getSelectionModel().getSelectedItem().toString()+".txt";
        loadConfiguration(selectedItem);
    }

    private void loadConfiguration(String fileName){
        Path filePath = Paths.get("src/main/resources/configurations/"+fileName);
        try {
            List<String> configuration = Files.readAllLines(filePath);
            heightInput.setText(configuration.get(0));
            widthInput.setText(configuration.get(1));
            howManyPlantsInput.setText(configuration.get(2));
            howManyEnergyFromPlantInput.setText(configuration.get(3));
            howManyPlantsGrowEverydayInput.setText(configuration.get(4));
            plantsPreferDeadBodiesCheckbox.setSelected(Boolean.parseBoolean(configuration.get(5)));
            howManyAnimalsOnStartInput.setText(configuration.get(6));
            howManyStartingEnergyAnimalHaveInput.setText(configuration.get(7));
            howLongGenomWillBeInput.setText(configuration.get(8));
            ifAnimalsMoveSlowerWhenOlderCheckbox.setSelected(Boolean.parseBoolean(configuration.get(9)));
            energeyNeededToReproduceInput.setText(configuration.get(10));
            energyUsedToReproduceInput.setText(configuration.get(11));
            minNumberOfMutationInput.setText(configuration.get(12));
            maxNumberOfMutationInput.setText(configuration.get(13));
            shouldWriteToCSV.setSelected(Boolean.parseBoolean(configuration.get(14)));
            nameOfConfiguration.setText(configuration.get(15));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    @FXML
    private void onSaveConfigurationClicked(){
        File file = new File("src/main/resources/configurations/%s.txt".formatted(nameOfConfiguration.getText()));
        System.out.println("Attempting to create file: " + file.getAbsolutePath());
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            fileWriter.write("%s\n".formatted(heightInput.getText()));
            fileWriter.write("%s\n".formatted(widthInput.getText()));
            fileWriter.write("%s\n".formatted(howManyPlantsInput.getText()));
            fileWriter.write("%s\n".formatted(howManyEnergyFromPlantInput.getText()));
            fileWriter.write("%s\n".formatted(howManyPlantsGrowEverydayInput.getText()));
            fileWriter.write("%b\n".formatted(plantsPreferDeadBodiesCheckbox.isSelected()));
            fileWriter.write("%s\n".formatted(howManyAnimalsOnStartInput.getText()));
            fileWriter.write("%s\n".formatted(howManyStartingEnergyAnimalHaveInput.getText()));
            fileWriter.write("%s\n".formatted(howLongGenomWillBeInput.getText()));
            fileWriter.write("%b\n".formatted(ifAnimalsMoveSlowerWhenOlderCheckbox.isSelected()));
            fileWriter.write("%s\n".formatted(energeyNeededToReproduceInput.getText()));
            fileWriter.write("%s\n".formatted(energyUsedToReproduceInput.getText()));
            fileWriter.write("%s\n".formatted(minNumberOfMutationInput.getText()));
            fileWriter.write("%s\n".formatted(maxNumberOfMutationInput.getText()));
            fileWriter.write("%b\n".formatted(shouldWriteToCSV.isSelected()));
            fileWriter.write("%s\n".formatted(nameOfConfiguration.getText()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        configurationChoise.getItems().add(nameOfConfiguration.getText());
        configurationChoise.setValue(nameOfConfiguration.getText());
    }
}
