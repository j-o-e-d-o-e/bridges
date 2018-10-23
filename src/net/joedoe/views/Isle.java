package net.joedoe.views;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static net.joedoe.GameInfo.CIRCLE_RADIUS;

class Isle extends StackPane {
    private Circle circle;
    private Text text;
    private int allBridges;
    private int missingBridges;
    private boolean showMissingBridges;

    Isle() {
        circle = new Circle(CIRCLE_RADIUS, Color.GREEN);
        allBridges = ((int) (Math.random() * 8) + 1);
        missingBridges = allBridges - ((int) (Math.random() * allBridges) + 1);
        text = new Text(Integer.toString(allBridges));
        getChildren().addAll(circle, text);
    }

    Circle getCircle() {
        return circle;
    }

    void setText(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        if (showMissingBridges)
            text.setText(Integer.toString(missingBridges));
        else
            text.setText(Integer.toString(allBridges));
    }

    void setBridges(int bridges){
        allBridges = bridges;
        text.setText(Integer.toString(bridges));
        missingBridges = allBridges - ((int) (Math.random() * allBridges) + 1);
    }

    int getBridges() {
        if (showMissingBridges)
            return missingBridges;
        return allBridges;
    }
}
