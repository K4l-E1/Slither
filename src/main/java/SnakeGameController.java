import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SnakeGameController {

    private Snake snake;
    private Food food;
    private Pane root;
    private boolean end;
    private Random random;
    private Ia ia;
    

    public SnakeGameController() {
        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        random = new Random();
        food  = new Food(Color.RED, random.nextInt(750), random.nextInt(500));
        snake = new Snake();
        ia = new Ia(food);
        end = false;      
    }

    public void startGame() {
        new Thread(() -> {
            while (!end) {
                snake.move();
                if (ia != null) ia.autoMove(food);
                checkCollision(); 
                Platform.runLater(() -> {
                    root.getChildren().clear();
                    if (ia != null) ia.draw(root);
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

    public List<Food> getBody() {
        List<Food> body = new ArrayList<>();
        body.addAll(snake.getBody());
        return body;
    }

    public void setFood(Food food) {
        this.food = food;
        root.getChildren().add(food.getRectangle());
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
            default:
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
        
        if(ia != null){

            if(snakeCollision(snake, ia)){
                end = true;
                resetGame();
            }

            if(snakeCollision(ia, snake)){
                snake.setCounter(ia.getCounter());
                ia = null;
            }
            
            if(collisionWithFood(ia)){
                    ia.grow(root);
                    Random random = new Random();
                    food = new Food(Color.RED, random.nextInt(760), random.nextInt(510));
            }

            if (wallCollision(ia) || autoCollision(ia)) {
                ia = null;
            }
        }

        if (wallCollision(snake) || autoCollision(snake)) {
            end = true;
            resetGame();
        }

        if(collisionWithFood(snake)){
                snake.grow(root);
                Random random = new Random();
                food = new Food(Color.RED, random.nextInt(760), random.nextInt(510));
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

    private boolean collisionWithFood(Snake s) {
        Food head = s.getSnakeHead();
        double headX = head.getPoint().x;
        double headY = head.getPoint().y;
        double foodX = food.getPoint().x;
        double foodY = food.getPoint().y;
    
        return (headX >= foodX - 18 && headX < foodX + 18)
                && (headY >= foodY - 18 && headY < foodY + 18);
    }

    private boolean autoCollision(Snake s) {
        List<Food> body = s.getBody();
        Food head = s.getHead();
    
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

    private boolean snakeCollision(Snake s1, Snake s2){
        List<Food> body = s2.getBody();
        Food head = s1.getHead();
    
        for (int i = 1; i < body.size(); i++) {
            double bodyX = body.get(i).getPoint().x;
            double bodyY = body.get(i).getPoint().y;
    
            if (head.getPoint().x == bodyX && head.getPoint().y == bodyY) {
                return true;
            }
        }
            return false;
    }

    private void updateScore() {
        root.getChildren().removeIf(node -> node instanceof Text);
        Text scoreText = new Text("Score: " + snake.getCounter());
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreText.setX(700);
        scoreText.setY(30);

        root.getChildren().add(scoreText);
    }

}