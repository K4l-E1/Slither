import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Snake {

    private List<Food> body;
    private Food snakeHead;
    private int dir = 3;
    int counter = 1;

    public Snake() {
        body = new ArrayList();
        body.add(new Food(Color.GREEN, 10, 10));
        snakeHead = body.get(0);
    }

    public List<Food> getBody() {
        List<Food> l = new ArrayList();
        for (Food f : body) {
            l.add(f);
        }
        return l;
    }

    public void draw(Pane root) {
            root.getChildren().clear(); // Effacez le contenu précédent
        
            for (Food f : body) {
                drawSnakeRectangle((int) f.getPoint().x, (int) f.getPoint().y, root);
            }
    }
    
    private void drawSnakeRectangle(int x, int y, Pane root) {
        Rectangle rectangle = new Rectangle(x, y, 30, 30);
        rectangle.setArcWidth(35);
        rectangle.setArcHeight(35);
        rectangle.setFill(Color.GREEN);
        root.getChildren().add(rectangle);
    }

    public void move() {

        switch(dir){
            case 0: 
                moveDown();
                break;
            case 1: 
                moveLeft();
                break;
            case 2: 
                moveUp();
                break;
            case 3: 
                moveRight();
                break;
        }

        for (int i = body.size() - 1; i > 0; i--) {
            body.get(i).getPoint().x = body.get(i - 1).getPoint().x;
            body.get(i).getPoint().y = body.get(i - 1).getPoint().y;
        }
    }

    public void setDirection(int newDir) {
        if ((dir == 0 && newDir != 2) ||  
            (dir == 1 && newDir != 3) || 
            (dir == 2 && newDir != 0) ||
            (dir == 3 && newDir != 1)) {
            dir = newDir;
        }
    }

    public void grow(Pane root) {
        body.add(new Food(Color.GREEN, 10, 10));
        counter++;
    }

    public Food getHead() {
        return snakeHead;
    }

    public void moveRight(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x + 2, snakeHead.getPoint().y);
    }

    public void moveLeft(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x - 2, snakeHead.getPoint().y);
    }

    public void moveUp(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x, snakeHead.getPoint().y - 2);
    }

    void moveDown(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x, snakeHead.getPoint().y + 2);
    }

    public Food getSnakeHead() {
        return snakeHead;
    }
}