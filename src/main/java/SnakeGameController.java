import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SnakeGameController {

    private Snake snake;
    private Food food;
    private Pane root;
    private boolean end;
    private Random random;
    

    public SnakeGameController() {
        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        snake = new Snake();
        end = false;
        random = new Random();
        food  = new Food(Color.RED, random.nextInt(770), random.nextInt(570));
    }
    
    public Pane getRoot() {
        return root;
    }

    public List<Food> getBody() {
        List<Food> body = new ArrayList<>();
        body.addAll(snake.getBody());
        return body;
    }

    public void setFood(Food food) {
        this.food = food;
        root.getChildren().add(food.getRectangle());
    }

    public void startGame() {
        new Thread(() -> {
            while (!end) {
                checkCollision(); 
                Platform.runLater(() -> {
                snake.draw(root);
                food.draw(root);
            });
                snake.move();
                    try {
                    Thread.sleep(10);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void handleKeyPress(KeyCode keyCode) {
        int newDirection = -1;
    
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
        }
    
        if (newDirection != -1) {
            snake.setDirection(newDirection);
        }
    }

    public void stopGame() {
        end = true;
    }

    private void checkCollision() {
        int headX = snake.getHead().getPoint().x;
        int headY = snake.getHead().getPoint().y;

        if (headX < 0 || headX >= 770 || headY < 0 || headY >= 570) {
            end = true;
            resetGame();
        }

        if(collisionWithFood()){
                snake.grow(root);
                Random random = new Random();
                food = new Food(Color.RED, random.nextInt(770), random.nextInt(570));
        }
        
    }

    public void resetGame() {
         Platform.runLater(() -> {
            root.getChildren().clear();

            snake = new Snake();
            root.getChildren().add(food.getRectangle());

            end = false;
        });
    }

    private boolean collisionWithFood() {
        Food head = snake.getSnakeHead();
        System.out.println(head.getPoint().x);
        return (head.getPoint().x >= food.getPoint().x && head.getPoint().x <= food.getPoint().x + 30)
            && (head.getPoint().y >= food.getPoint().y && head.getPoint().y <= food.getPoint().y + 30);
    }
}