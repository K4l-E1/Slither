package server;

import common.Ia;
import common.Snake;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreManager {

    // Cette classe gère l'affichage du score des joueurs en fonction du mode
    
    static void updateScore(Pane root, Snake snake, Snake snake2, Ia ia) {
        root.getChildren().removeIf(node -> node instanceof Text);
        
        Text scoreText1 = new Text("Player: " + snake.getCounter());   
        scoreText1.setFill(snake.getColor());
        scoreText1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreText1.setX(700);
        scoreText1.setY(30);
        root.getChildren().addAll(scoreText1);

        if(snake2 != null){
            Text scoreText2 = new Text("Player: " + snake2.getCounter());
            scoreText2.setFill(snake2.getColor());
            scoreText2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            scoreText2.setX(700);
            scoreText2.setY(60);
            root.getChildren().addAll(scoreText2);
        }
        
        if(ia != null){
            Text scoreText2 = new Text("Ia: " + ia.getCounter());
            scoreText2.setFill(ia.getColor());
            scoreText2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            scoreText2.setX(735);
            scoreText2.setY(60);
            root.getChildren().addAll(scoreText2);
        }
    }
}
