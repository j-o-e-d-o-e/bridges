package net.joedoe.entities;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import net.joedoe.utils.GameInfo;

import static net.joedoe.utils.GameInfo.CIRCLE_COLOR;
import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;

public class Isle extends StackPane implements GridEntity {
    private Circle circle;
    private Text text;
    private int bridges, missingBridges, row, column;
    private boolean showMissingBridges;
    private boolean clicked;
    private EventHandler<Event> listener;


    @SuppressWarnings("WeakerAccess")
    public Isle(int row, int column, int bridges) {
        this.row = row;
        this.column = column;
        this.bridges = bridges;
        this.missingBridges = bridges - ((int) (Math.random() * bridges) + 1);
        circle = new Circle(CIRCLE_RADIUS, CIRCLE_COLOR);
        text = new Text(Integer.toString(row) + "/" + Integer.toString(column));
        getChildren().addAll(circle, text);
        setOnMouseClicked(e -> {
            if (clicked) {
                circle.setFill(GameInfo.CIRCLE_COLOR);
                clicked = false;
            } else {
                circle.setFill(Color.RED);
                clicked = true;
            }
            listener.handle(e);
        });
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }
}
