package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.presenter.SimulationMenuPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationMenu.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationMenuPresenter presenter = loader.getController();
        configureStage(primaryStage, viewRoot);

        AbstractWorldMap map = new GrassField(5);
        presenter.setWorldMap(map);
        map.addObservator(presenter);
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
