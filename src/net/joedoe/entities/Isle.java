package net.joedoe.entities;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

import static net.joedoe.utils.GameInfo.*;

public class Isle extends StackPane implements GridEntity {
    @SuppressWarnings("FieldCanBeLocal")
    private Circle circle;
    private Label text;
    private int bridgeCount, missingBridgeCount, row, column;
    private boolean showMissingBridges;
    @SuppressWarnings("unused")
    private boolean clicked;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private EventHandler<Event> listener;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<Bridge> bridges = new ArrayList<>();

    @SuppressWarnings("WeakerAccess")
    public Isle(int row, int column, int bridgeCount) {
//        setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.row = row;
        this.column = column;
        this.bridgeCount = bridgeCount;
        this.missingBridgeCount = bridgeCount - ((int) (Math.random() * bridgeCount) + 1);
        circle = new Circle(CIRCLE_RADIUS, CIRCLE_COLOR);
        text = new Label(Integer.toString(row) + "/" + Integer.toString(column));
        getChildren().addAll(circle, text);
        addListener();
    }

    private void addListener() {
        setOnMouseClicked(e -> {
//            System.out.println(e.getButton());
            if (ONE_TILE / 2 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE
                    && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
                System.out.println("EAST");
                return;
            }
            if (0 < e.getX() && e.getX() < ONE_TILE / 2 && 0 < e.getY() && e.getY() < ONE_TILE
                    && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
                System.out.println("WEST");
                return;
            }
            if (0 < e.getX() && e.getX() < ONE_TILE && ONE_TILE / 2 < e.getY() && e.getY() < ONE_TILE
                    && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
                System.out.println("SOUTH");
                return;
            }
            if (0 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE / 2
                    && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
                System.out.println("NORTH");
            }
        });
    }

//    private void addListener() {
//        setOnMouseClicked(e -> {
//            if (clicked) {
//                circle.setFill(GameInfo.CIRCLE_COLOR);
//                clicked = false;
//            } else {
//                circle.setFill(Color.RED);
//                clicked = true;
//            }
//            listener.handle(e);
//        });
//    }

    public void setText(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        if (showMissingBridges)
            text.setText(Integer.toString(missingBridgeCount));
        else
            text.setText(Integer.toString(bridgeCount));
    }

    public int getBridgeCount() {
        if (showMissingBridges)
            return missingBridgeCount;
        return bridgeCount;
    }

    public void addBridge(Bridge bridge) {
        bridges.add(bridge);
    }

//    public void removeBridge(int index) {
//        bridges.remove(index);
//    }

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
