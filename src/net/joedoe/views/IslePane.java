package net.joedoe.views;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;
import static net.joedoe.utils.GameInfo.STD_COLOR;

public class IslePane extends StackPane {
    private int x, y;
    private Circle circle;
    private Label label;

    IslePane(int x, int y, int bridgeCount) {
        this.x = x;
        this.y = y;
        circle = new Circle(CIRCLE_RADIUS, STD_COLOR);
        label = new Label(Integer.toString(bridgeCount));
        getChildren().addAll(circle, label);
    }

    void setText(String text) {
        label.setText(text);
    }

    void setColor(Color color) {
        circle.setFill(color);
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "IslePane{" + "x=" + x + ", y=" + y + '}';
    }
}
