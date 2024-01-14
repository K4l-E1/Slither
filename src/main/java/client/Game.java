package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.GameManager;
import server.HandleManager;

public class Game extends Application {


    public void start(Stage primaryStage) {
     
        GameManager gc = new GameManager();

        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> GameManager.stopGame());
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);

        Pane rootPane = gc.getRoot();
        Scene scene = new Scene(rootPane);
        scene.setOnKeyPressed(event -> HandleManager.handleKeyPress(event.getCode()));
        primaryStage.setScene(scene);
        GameManager.startGame();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}