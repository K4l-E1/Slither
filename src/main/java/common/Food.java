package common;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.Point;
import java.util.Random;

public class Food {

    private Point point;
    private Rectangle rectangle;

    public Food(Color c) {
        Random random = new Random();
        point = new Point(random.nextInt(750), random.nextInt(500));
        rectangle = new Rectangle(point.x, point.y,  20, 20);
        rectangle.setFill(c);
    }

    public void draw(Pane root) {
        root.getChildren().add(rectangle);
    }

    public Point getPoint() {
        return point;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

}