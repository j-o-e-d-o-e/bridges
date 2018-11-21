package net.joedoe.views;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.joedoe.utils.Coordinate;

import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;
import static net.joedoe.utils.GameInfo.STD_COLOR;

public class IslePane extends StackPane {
    private Coordinate pos;
    private Circle circle;
    private Label label;

    IslePane(int x, int y, int bridgeCount) {
        pos = new Coordinate(x, y);
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
        return pos.getY();
    }

    int getX() {
        return pos.getX();
    }

    @Override
    public String toString() {
        return "IslePane{" + "y=" + pos.getY() + ", x=" + pos.getX() + '}';
    }
}
