package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.DailyDataCollector;
import agh.ics.oop.model.util.WorldElementVisualizer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.UUID;


import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class SimulationWindowPresenter implements MapChangeListener {

    private ProjectWorldMap worldMap;
    private Simulation simulation;
    private Stage stage;

    private WorldElementVisualizer worldElementVisualizer = new WorldElementVisualizer();

    private DailyDataCollector collectData;

    private Image tile = new Image("tile.png");
    private Image equator = new Image("equtor.png");

    private SimulationEngine simulationEngine;

    private boolean isPaused = false;



    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Text numberOfDays;
    @FXML
    private Text numberOfAnimals;
    @FXML
    private Text numberOfPlants;
    @FXML
    private Text numberOfFreeTiles;
    @FXML
    private Text mostPopularGenotype;
    @FXML
    private Text averageEnergyLevel;
    @FXML
    private Text averageDaysAlive;
    @FXML
    private Text averageNumberOfKids;
    @FXML
    private Button pauseAndResumeButton;



    public void setupSimulation(ProjectWorldMap worldMap, int howManyAnimalsToStartWith, int howManyEnergyAnimalsStartWith,
                                int energyNeededToReproduce, int energyGettingPassedToDescendant, int minMutationInNewborn, int maxMutationInNewborn,
                                int genomeLength, boolean ifAnimalsMoveSlowerWhenOlder, boolean writeIntoACSVFile,
                                Stage simulationStage) {
        this.worldMap = worldMap;
        stage = simulationStage;
        mainBorderPane.setMargin(mapGrid, new Insets(12,12,12,12));
        this.simulation = new Simulation(worldMap, howManyAnimalsToStartWith, howManyEnergyAnimalsStartWith,
                energyNeededToReproduce, energyGettingPassedToDescendant,minMutationInNewborn, maxMutationInNewborn,
                genomeLength, ifAnimalsMoveSlowerWhenOlder, writeIntoACSVFile);
        Map<UUID, Simulation> simulationsMap = new HashMap<>();
        simulationsMap.put(worldMap.getID(), simulation);
        simulationEngine = new SimulationEngine(simulationsMap);
        simulationEngine.runAsync();
        collectData = new DailyDataCollector(worldMap, simulation.getDeadAnimals(),simulation.getSimulationDays());
    }

    public void drawMap(ProjectWorldMap map) {
        clearGrid();
        Boundary boundary = map.getCurrentBounds();
        Label label = new Label();
        GridPane.setHalignment(label, HPos.CENTER);
        int widthtOfMap = boundary.upperRightCorner().getX();
        int heightOfMap = boundary.upperRightCorner().getY();
        double windowWidthToMapWidthRatio = (stage.getWidth()-180.0) / (widthtOfMap+1);
        double windowHeightToMapWidthRatio = (stage.getHeight()-100.0) / (heightOfMap+1);

        int cellSideLength = (int)(min(windowHeightToMapWidthRatio,windowWidthToMapWidthRatio));

        double fullHealth = cellSideLength-cellSideLength/10.0;
        Insets healthBarMargin = new Insets(0,0,0,cellSideLength/10.0);

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
        for (WorldElement element : elements)
        {
            Vector2d positionOfElement = element.getPosition();
            ImageView worldElement = worldElementVisualizer.getImageView(element);
            worldElement.setFitHeight(cellSideLength);
            worldElement.setFitWidth(cellSideLength);
            mapGrid.add(worldElement, positionOfElement.getX() , heightOfMap - positionOfElement.getY());
            GridPane.setHalignment(worldElement, HPos.CENTER);
            if (element.getClass()== Animal.class){
                Rectangle healthbar = new Rectangle(fullHealth,cellSideLength/10.0, new Color(0,0,0,1));
                Rectangle health = new Rectangle(fullHealth*(min(1.0, ((Animal) element).getEnergy()/(double)((Animal) element).getMinReproductionEnergy())),cellSideLength/10.0, new Color(0,1,0,1));
                mapGrid.add(healthbar, positionOfElement.getX() , heightOfMap - positionOfElement.getY());
                mapGrid.add(health, positionOfElement.getX() , heightOfMap - positionOfElement.getY());
                GridPane.setHalignment(healthbar, HPos.LEFT);
                GridPane.setValignment(healthbar, VPos.BOTTOM);
                GridPane.setHalignment(health, HPos.LEFT);
                GridPane.setValignment(health, VPos.BOTTOM);
                GridPane.setMargin(healthbar, healthBarMargin);
                GridPane.setMargin(health, healthBarMargin);
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void drawCurrentDayInfo(ProjectWorldMap worldMap)
    {

        numberOfDays.setText(String.valueOf(collectData.getCurrentSimulationDay()));
        numberOfAnimals.setText(String.valueOf(collectData.numberOfAliveAnimals()));
        numberOfPlants.setText(String.valueOf(collectData.numberOfPlants()));
        numberOfFreeTiles.setText(String.valueOf(collectData.numberOfFreeTiles()));

        displayMostPopularGenotype(collectData.mostPopularGenotype());

        averageEnergyLevel.setText(String.valueOf(collectData.averageEnergyLevel()));

        Optional<Integer> returnedAverageLifeSpan = collectData.averageLifeSpan();
        String displayText = returnedAverageLifeSpan
                .map(String::valueOf)
                .orElse("No data available");
        averageDaysAlive.setText(displayText);

        averageNumberOfKids.setText(String.valueOf(collectData.averageKidsNumber()));
    }

    private void displayMostPopularGenotype(Set<List<Integer>> popularGenotypes) {
        // Convert each List<Integer> in the Set to a string and join with a newline
        String genotypesText = popularGenotypes.stream()
                .map(list -> list.stream()
                        .map(String::valueOf) // Convert each Integer to String
                        .collect(Collectors.joining(" "))) // Join integers with a space
                .collect(Collectors.joining("\n")); // Join lists with a newline

        // Set the text to the generated string
        mostPopularGenotype.setText(genotypesText);
    }

    @Override
    public void mapChanged(ProjectWorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            drawCurrentDayInfo(worldMap);
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

        double windowWidthToMapWidthRatio = (stage.getWidth()-180.0) / (widthtOfMap+1);
        double windowHeightToMapWidthRatio = (stage.getHeight()-100.0) / (heightOfMap+1);

        int cellSideLength = (int)(min(windowHeightToMapWidthRatio,windowWidthToMapWidthRatio));

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(1.0);


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
        int colouredAnimals =0;
        for (WorldElement element : elements ){
            Vector2d positionOfElement = element.getPosition();
            ImageView worldElement = worldElementVisualizer.getImageView(element);
            worldElement.setFitHeight(cellSideLength);
            worldElement.setFitWidth(cellSideLength);
            if (element.getClass() == Animal.class){
                Animal animal = (Animal) element;
                Genome genome = animal.getGenome();
                int[] genomeAsList = genome.getGenome();
                boolean genomeIsPopular = false;
                for (List<Integer> popularGenome: collectData.mostPopularGenotype()){
                    for(int i = 0; i < popularGenome.size(); i++){
                        if( genomeAsList[i] == popularGenome.get(i)){
                            genomeIsPopular = true;
                        }
                        else{
                            genomeIsPopular = false;
                            break;
                        }
                    }
                    if(genomeIsPopular){
                        worldElement.setEffect(colorAdjust);
                        colouredAnimals++;
                    }
                }
            }
            mapGrid.add(worldElement, positionOfElement.getX() , heightOfMap - positionOfElement.getY());
            GridPane.setHalignment(worldElement, HPos.CENTER);
        }
        System.out.println(colouredAnimals);
    }
}
