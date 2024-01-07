import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Game extends Application {


    public void start(Stage primaryStage) {
     GameController gc = new GameController();

        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> gc.stopGame());
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);

        Pane rootPane = gc.getRoot();
        Scene scene = new Scene(rootPane);
        scene.setOnKeyPressed(event -> gc.handleKeyPress(event.getCode()));
        primaryStage.setScene(scene);
        gc.startGame();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}