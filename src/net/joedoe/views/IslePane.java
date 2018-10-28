package net.joedoe.views;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.joedoe.entities.Isle;

import static net.joedoe.utils.GameInfo.STD_COLOR;
import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;

public class IslePane extends StackPane {
    private Isle isle;
    private Circle circle;
    private Label label;

    public IslePane(Isle isle) {
        this.isle = isle;
        circle = new Circle(CIRCLE_RADIUS, STD_COLOR);
        label = new Label();
        getChildren().addAll(circle, label);
    }

    public void setText(String text) {
        label.setText(text);
    }

    Isle getIsle() {
        return isle;
    }

    public void setColor(Color color) {
        circle.setFill(color);
    }
}
