package net.joedoe.views;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.joedoe.entities.Isle;

import static net.joedoe.utils.GameInfo.CIRCLE_COLOR;
import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;

public class IslePane extends StackPane {
    private Isle isle;
    private Label label;

    public IslePane(Isle isle) {
        this.isle = isle;
        label = new Label();
        getChildren().addAll(new Circle(CIRCLE_RADIUS, CIRCLE_COLOR), label);
    }

    public void setText(String text) {
        label.setText(text);
    }

    Isle getIsle() {
        return isle;
    }
}
