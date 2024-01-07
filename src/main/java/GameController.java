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

public class GameController {

    private Snake snake;
    private Food food;
    private Pane root;
    private boolean end;
    private Random random;
    private Ia ia;
    

    public GameController() {
        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        random = new Random();
        food  = new Food(Color.RED, random.nextInt(750), random.nextInt(500));
        snake = new Snake(Color.GREEN);
        ia = new Ia(food, Color.ORANGE);
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
                    Thread.sleep(100);
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
            snake.setDir(newDirection);
        }
    }

    public void stopGame() {
        end = true;
    }

    private void checkCollision() {

        if(ia != null && snakeCollision(snake, ia)){
            stopGame();
            resetGame();
        }
        

        if(ia != null && snakeCollision(ia, snake)){
            snake.setCounter(ia.getCounter());
            ia = null;
        }
        
        if(ia != null && collisionWithFood(ia)){
                ia.grow(root);
                Random random = new Random();
                food = new Food(Color.RED, random.nextInt(760), random.nextInt(510));
        }

        if (ia != null && (wallCollision(ia) || autoCollision(ia))) {
            ia = null;
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

            snake = new Snake(Color.GREEN);
            ia = new Ia(food, Color.ORANGE);
            ia.autoMove(food);
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

    public boolean snakeCollision(Snake snake1, Snake snake2) {
        Food head1 = snake1.getHead();
    
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

}