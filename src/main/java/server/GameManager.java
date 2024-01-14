package server;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import common.*;

public class GameManager {

    static Snake snake;
    static Snake snake2;
    private static Food food = new Food(Color.RED);
    private static Pane root;
    private static boolean end;
    private static boolean pause;
    private static Ia ia;
    private static String mode = "One";
    private static VBox Vbox;

    public GameManager() {

        // Cette classe s'occupe de la gestion globale du jeu

        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.getChildren().clear();

        snake = new Snake(Color.GREEN);
        ia = new Ia(food, Color.ORANGE);
        ia.autoMove(food);            
        root.getChildren().add(food.getRectangle());
        pause = false;
        end = false;
        
    }

    public static void startGame() {
        new Thread(() -> {
            while (!end && !pause) {
                snake.move();
                if (mode == "One"){
                    ia.autoMove(food);
                    checkCollision1(); 
                }
                else{
                    if(snake2!=null){
                        snake2.move();
                        checkCollision2();
                    }
                }
                
                Platform.runLater(() -> {
                    root.getChildren().clear();
                    if (ia != null) ia.draw(root);
                    else snake2.draw(root);
                    snake.draw(root);
                    food.draw(root);
                    ScoreManager.updateScore(root, snake, snake2, ia);
                });
                try {
                    Thread.sleep(80);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public static void stopGame() {
        end = true;
    }

    private static void checkCollision1() {  // Collision pour ce qui concerne le serpent 1

        if(CollisionManager.snakeCollision(snake, ia) || CollisionManager.wallCollision(snake) || CollisionManager.autoCollision(snake)){
            stopGame();
            resetGame();
        }
        
        else if(CollisionManager.snakeCollision(ia, snake) || CollisionManager.wallCollision(ia) || CollisionManager.autoCollision(ia)){
            ia = null;
        }
        
        else if(CollisionManager.collisionWithFood(ia, food)){
                ia.grow(root);
                food = new Food(Color.RED);
        }

        else if(CollisionManager.collisionWithFood(snake, food)){
                snake.grow(root);
                food = new Food(Color.RED);
        }
        
    }

    private static void checkCollision2() {  // Collision pour ce qui concerne le serpent 2 

        if(CollisionManager.snakeCollision(snake, snake2) || CollisionManager.snakeCollision(snake2, snake) || CollisionManager.wallCollision(snake2) || CollisionManager.autoCollision(snake2) || CollisionManager.wallCollision(snake) || CollisionManager.autoCollision(snake)){
            stopGame();
            resetGame();
        }
       
        else if(CollisionManager.collisionWithFood(snake2, food)){
                snake2.grow(root);
                food = new Food(Color.RED);
        }
        else if(CollisionManager.collisionWithFood(snake, food)){
                snake.grow(root);
                food = new Food(Color.RED);
        }
        
    }

    public static void resetGame() {
        Platform.runLater(() -> {
           root.getChildren().clear();

           snake = new Snake(Color.GREEN);
           if (mode == "One"){
               snake2 = null;
               ia = new Ia(food, Color.ORANGE);
               ia.autoMove(food);
           }
           else{
                   ia = null;
                   snake2 = new Snake(Color.VIOLET);
           }
           System.out.println(mode);
           
           root.getChildren().add(food.getRectangle());
           pause = false;
           end = false;
       });
   }


   static void pause(){
       pause = true;
       
       Button playButton = new Button("Play");
       Button PlayersButton;
       if(mode == "One") PlayersButton = new Button("2 Joueurs");
       else PlayersButton = new Button("1 Joueur");
       Button quitButton = new Button("Quitter");



       Vbox = new VBox(10, playButton, PlayersButton, quitButton);
       Vbox.setAlignment(Pos.CENTER);

       
       root.getChildren().add(Vbox);

       playButton.setOnAction(event -> resume());
       PlayersButton.setOnAction(event -> changeGame());
       quitButton.setOnAction(event -> Platform.exit());
   }

   static void resume(){
       root.getChildren().remove(Vbox);
       pause = false;
       startGame();
   }

   static void changeGame(){
       if(mode == "One") mode = "Two";
       else mode = "One";
       pause = false;
       resetGame();
       startGame();
   }

   

    public Pane getRoot() {
        return root;
    }

    public List<SnakePart> getBody() {
        List<SnakePart> body = new ArrayList<>();
        body.addAll(snake.getBody());
        return body;
    }

    public void setFood(Food foood) {
        food = foood;
        root.getChildren().add(food.getRectangle());
    }

}