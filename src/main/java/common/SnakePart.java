package common;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.Point;

public class SnakePart {

    private Point point;
    private Rectangle rectangle;

    public SnakePart(Color c, int x, int y) {
        point = new Point(x, y);
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