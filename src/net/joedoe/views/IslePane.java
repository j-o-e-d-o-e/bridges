package net.joedoe.views;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;
import static net.joedoe.utils.GameInfo.STD_COLOR;

public class IslePane extends StackPane {
    private int y, x;
    private Circle circle;
    private Label label;

    IslePane(int y, int x, int bridgeCount) {
        this.y = y;
        this.x = x;
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

    int getY() {
        return y;
    }

    int getX() {
        return x;
    }

    @Override
    public String toString() {
        return "IslePane{" + "y=" + y + ", x=" + x + '}';
    }
}
