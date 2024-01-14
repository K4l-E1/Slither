package server;

import javafx.scene.input.KeyCode;

public class HandleManager {

    // Cette classe gère le comportement à l'issue d'une touche appuyée
    
    public static void handleKeyPress(KeyCode keyCode) {
        int newDirection = -1;
        int newDirection2 = -1;
    
        switch (keyCode) {
            
            // Pour le joueur 1
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

            // Pour le joueur 2
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
                GameManager.pause();

            default:
                break;
        }
    
        if (newDirection != -1) {
            GameManager.snake.setDir(newDirection);
        }

        if (newDirection2 != -1 && GameManager.snake2 != null) {
            GameManager.snake2.setDir(newDirection2);
        }
    }
}
