package main;

import javafx.scene.paint.Color;

public class Obstacle {

    public Vector2d position;
    public Color color;

    public Obstacle(Vector2d position) {
        this.position = position;
        color = Color.BLACK;
    }

    public Vector2d getPosition() {
        return position;
    }
    public Color getColor() {
        return color;
    }
}
