import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake {

    protected List<Food> body;
    private Food snakeHead;
    protected int dir = 3;
    private int DISTANCE_AVANCEMENT = 18;
    private int counter = 1;
    private Color color;


    public Snake() {
        color = Color.GREEN;
        Random random = new Random();
        body = new ArrayList<Food>();
        body.add(new Food(color, random.nextInt(600), random.nextInt(400)));
        snakeHead = body.get(0);
    }

    public Snake(Color c) {
        color = c;
        Random random = new Random();
        body = new ArrayList<Food>();
        body.add(new Food(color, random.nextInt(600), random.nextInt(400)));
        snakeHead = body.get(0);
    }

    public List<Food> getBody() {
        List<Food> l = new ArrayList<Food>();
        for (Food f : body) {
            l.add(f);
        }
        return l;
    }

    public Food getSnakeHead() {
        return snakeHead;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Color getColor() {
        return color;
    }

    public Food getHead() {
        return snakeHead;
    }

    public int getDir() {
        return dir;
    }

    public void draw(Pane root) {        
            for (Food f : body) {
                drawSnakeRectangle((int) f.getPoint().x, (int) f.getPoint().y, root);
            }
    }
    
    private void drawSnakeRectangle(int x, int y, Pane root) {
        Rectangle rectangle = new Rectangle(x, y, 20, 20);
        rectangle.setArcWidth(30);
        rectangle.setArcHeight(30);
        rectangle.setFill(color);
        root.getChildren().add(rectangle);
    }

    public void move() {

        for (int i = body.size() - 1; i > 0; i--) {
            body.get(i).getPoint().x = body.get(i - 1).getPoint().x;
            body.get(i).getPoint().y = body.get(i - 1).getPoint().y;
        }

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
    }

    public void setDir(int newDir) {
        if ((dir == 0 && newDir != 2) ||  
            (dir == 1 && newDir != 3) || 
            (dir == 2 && newDir != 0) ||
            (dir == 3 && newDir != 1)) {
            dir = newDir;
        }
    }

    public synchronized void grow(Pane root) {
        if(dir == 0) body.add(new Food(color, body.get(counter-1).getPoint().x, body.get(counter-1).getPoint().y - 20));
        else if(dir == 1) body.add(new Food(color, body.get(counter-1).getPoint().x + 20, body.get(counter-1).getPoint().y));
        else if(dir == 2) body.add(new Food(color, body.get(counter-1).getPoint().x, body.get(counter-1).getPoint().y + 20));
        else{
            body.add(new Food(color, body.get(counter-1).getPoint().x - 20, body.get(counter-1).getPoint().y));
        }
        counter++;
    }

    public void moveRight(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x + DISTANCE_AVANCEMENT, snakeHead.getPoint().y);
    }

    public void moveLeft(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x - DISTANCE_AVANCEMENT, snakeHead.getPoint().y);
    }

    public void moveUp(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x, snakeHead.getPoint().y - DISTANCE_AVANCEMENT);
    }

    void moveDown(){
        snakeHead.getPoint().setLocation(snakeHead.getPoint().x, snakeHead.getPoint().y + DISTANCE_AVANCEMENT);
    }
}