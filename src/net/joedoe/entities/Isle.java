package net.joedoe.entities;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static net.joedoe.utils.GameInfo.CIRCLE_COLOR;
import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;

public class Isle extends StackPane implements GridEntity {
    private Circle circle;
    private Text text;
    private int bridges, missingBridges, row, column;
    private boolean showMissingBridges;

    @SuppressWarnings("WeakerAccess")
    public Isle(int row, int column, int bridges) {
        setAlignment(Pos.CENTER);
        Rectangle border = new Rectangle(CIRCLE_RADIUS, CIRCLE_RADIUS);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        this.row = row;
        this.column = column;
        this.bridges = bridges;
        this.missingBridges = bridges - ((int) (Math.random() * bridges) + 1);
        circle = new Circle(CIRCLE_RADIUS, CIRCLE_COLOR);
        text = new Text(Integer.toString(row) + "/" + Integer.toString(column));
        getChildren().addAll(border, circle, text);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setText(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        if (showMissingBridges)
            text.setText(Integer.toString(missingBridges));
        else
            text.setText(Integer.toString(bridges));
    }

    public int getBridges() {
        if (showMissingBridges)
            return missingBridges;
        return bridges;
    }

    @SuppressWarnings("WeakerAccess")
    public int getRow() {
        return row;
    }

    @SuppressWarnings("WeakerAccess")
    public int getColumn() {
        return column;
    }
}
