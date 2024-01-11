package common;

import javafx.scene.paint.Color;

public class Ia extends Snake {
    Food fruit;

    public Ia(Food f, Color c) {
        super(c);
        fruit = f;
    }

    public void autoMove(Food f) {
        fruit = f;

        for (int i = body.size() - 1; i > 0; i--) {
            body.get(i).getPoint().x = body.get(i - 1).getPoint().x;
            body.get(i).getPoint().y = body.get(i - 1).getPoint().y;
        }

        if (getHead().getPoint().x < fruit.getPoint().x -15 && dir != 1){
            dir = 3;
            moveRight(); moveRight();
        }
        else if (getHead().getPoint().x > fruit.getPoint().x +15 && dir != 3){
            dir = 1;
            moveLeft(); moveLeft();
        }
        else if (getHead().getPoint().y < fruit.getPoint().y -15 && dir != 2){
            dir = 0;
            moveDown(); moveDown();
        }
        else if (getHead().getPoint().y > fruit.getPoint().y + 15 && dir != 0){
            dir = 2;
            moveUp(); moveUp();
        }
    }
}