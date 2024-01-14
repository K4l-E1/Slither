package server;

import java.util.List;

import common.Food;
import common.Snake;
import common.SnakePart;

public class CollisionManager{
    
    // Dans cette classe il y'a toutes les fonction qui gèrent les collisions dans le jeu. Entre les serpents et avec les fruits 

    static boolean collisionWithFood(Snake s, Food food) {
        SnakePart head = s.getSnakeHead();
        double headX = head.getPoint().x;
        double headY = head.getPoint().y;
        double foodX = food.getPoint().x;
        double foodY = food.getPoint().y;
    
        return (headX >= foodX - 18 && headX < foodX + 18)
                && (headY >= foodY - 18 && headY < foodY + 18);
    }

    static boolean autoCollision(Snake s) {
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

    static boolean wallCollision(Snake s){
        int headX = s.getHead().getPoint().x;
        int headY = s.getHead().getPoint().y;
        return headX < 0 || headX >= 780 || headY < 0 || headY >= 550;
    }

    public static boolean snakeCollision(Snake snake1, Snake snake2) {
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
}