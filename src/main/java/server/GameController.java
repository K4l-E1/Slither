package server;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import common.*;

public class GameController {

    private Snake snake;
    private Snake snake2;
    private Food food = new Food(Color.RED);
    private Pane root;
    private boolean end, pause;
    private Ia ia;
    private String mode = "One";
    private VBox Vbox;
    

    public GameController() {
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

    public void startGame() {
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
                    updateScore();
                });
                try {
                    Thread.sleep(80);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    public Pane getRoot() {
        return root;
    }

    public List<SnakePart> getBody() {
        List<SnakePart> body = new ArrayList<>();
        body.addAll(snake.getBody());
        return body;
    }

    public void setFood(Food food) {
        this.food = food;
        root.getChildren().add(food.getRectangle());
    }

    public void handleKeyPress(KeyCode keyCode) {
        int newDirection = -1;
        int newDirection2 = -1;
    
        switch (keyCode) {
            case DOWN:
                newDirection = 0; // vers le bas
                break;
            case LEFT:
                newDirection = 1; // vers la gauche
                break;
            case UP:
                newDirection = 2; // vers le haut
                break;
            case RIGHT:
                newDirection = 3; // vers la droite
                break;
            case S:
                newDirection2 = 0; // vers le bas
                break;
            case Q:
                newDirection2 = 1; // vers la gauche
                break;
            case Z:
                newDirection2 = 2; // vers le haut
                break;
            case D:
                newDirection2 = 3; // vers la droite
                break;
            case ESCAPE:
                pause();
            default:
                break;
        }
    
        if (newDirection != -1) {
            snake.setDir(newDirection);
        }

        if (newDirection2 != -1 && snake2 != null) {
            snake2.setDir(newDirection2);
        }
    }

    public void stopGame() {
        end = true;
    }

    private void checkCollision1() {

        if(snakeCollision(snake, ia) || wallCollision(snake) || autoCollision(snake)){
            stopGame();
            resetGame();
        }
        
        else if(snakeCollision(ia, snake) || wallCollision(ia) || autoCollision(ia)){
            ia = null;
        }
        
        else if(collisionWithFood(ia)){
                ia.grow(root);
                food = new Food(Color.RED);
        }

        else if(collisionWithFood(snake)){
                snake.grow(root);
                food = new Food(Color.RED);
        }
        
    }

    private void checkCollision2() {

        if(snakeCollision(snake, snake2) || snakeCollision(snake2, snake) || wallCollision(snake2) || autoCollision(snake2) || wallCollision(snake) || autoCollision(snake)){
            stopGame();
            resetGame();
        }
       
        else if(collisionWithFood(snake2)){
                snake2.grow(root);
                food = new Food(Color.RED);
        }
        else if(collisionWithFood(snake)){
                snake.grow(root);
                food = new Food(Color.RED);
        }
        
    }

    public void resetGame() {
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

    private boolean collisionWithFood(Snake s) {
        SnakePart head = s.getSnakeHead();
        double headX = head.getPoint().x;
        double headY = head.getPoint().y;
        double foodX = food.getPoint().x;
        double foodY = food.getPoint().y;
    
        return (headX >= foodX - 18 && headX < foodX + 18)
                && (headY >= foodY - 18 && headY < foodY + 18);
    }

    private boolean autoCollision(Snake s) {
        List<SnakePart> body = s.getBody();
        SnakePart head = s.getHead();
    
        for (int i = 1; i < body.size(); i++) {
            double bodyX = body.get(i).getPoint().x;
            double bodyY = body.get(i).getPoint().y;
    
            if (head.getPoint().x == bodyX && head.getPoint().y == bodyY) {
                return true;
            }
        }
    
        return false;
    }

    private boolean wallCollision(Snake s){
        int headX = s.getHead().getPoint().x;
        int headY = s.getHead().getPoint().y;
        return headX < 0 || headX >= 780 || headY < 0 || headY >= 550;
    }

    public boolean snakeCollision(Snake snake1, Snake snake2) {
        SnakePart head1 = snake1.getHead();
    
        for (int i = 1; i < snake2.getBody().size(); i++) {
            double bodyX = snake2.getBody().get(i).getPoint().x;
            double bodyY = snake2.getBody().get(i).getPoint().y;
    
            if (
                head1.getPoint().x >= bodyX - 15 && 
                head1.getPoint().x <= bodyX + 15 && 
                head1.getPoint().y >= bodyY - 15 && 
                head1.getPoint().y <= bodyY + 15 ) {
                return true;
            }
        }
    
        return false;
    }

    private void updateScore() {
        root.getChildren().removeIf(node -> node instanceof Text);
        
        Text scoreText1 = new Text("Player: " + snake.getCounter());   
        scoreText1.setFill(snake.getColor());
        scoreText1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreText1.setX(700);
        scoreText1.setY(30);
        root.getChildren().addAll(scoreText1);
        
        if(ia != null){
            Text scoreText2 = new Text("Ia: " + ia.getCounter());
            scoreText2.setFill(ia.getColor());
            scoreText2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            scoreText2.setX(735);
            scoreText2.setY(60);
            root.getChildren().addAll(scoreText2);
        }
    }

    void pause(){
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

    void resume(){
        root.getChildren().remove(Vbox);
        pause = false;
        startGame();
    }

    void changeGame(){
        if(mode == "One") mode = "Two";
        else mode = "One";
        pause = false;
        resetGame();
        startGame();
    }

}