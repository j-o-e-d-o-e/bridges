package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.MockData;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static net.joedoe.GameInfo.CIRCLE_RADIUS;

class Grid1 extends GridPane {
    private List<Isle> isles = new ArrayList<>();
    private EventHandler<Event> listener;

    Grid1() {
        setAlignment(Pos.CENTER);
        setGridLinesVisible(true);
        IntStream.range(0, MockData.ROWS).mapToObj(i -> new RowConstraints(CIRCLE_RADIUS * 2)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, MockData.COLS).mapToObj(i -> new ColumnConstraints(CIRCLE_RADIUS * 2)).forEachOrdered(column -> getColumnConstraints().add(column));
        addIsles();
        addBridges();
    }

    private void addIsles() {
        isles.addAll(MockData.ISLES);
        for (Isle isle : isles) {
            isle.setOnMouseClicked(e -> {
                isle.getCircle().setFill(Color.RED);
                listener.handle(e);
            });
            GridPane.setConstraints(isle, isle.getColumn(), isle.getRow());
            // add(isle, isle.getColumn(), isle.getRow(), 2, 2); // add isles on lines, not in boxes
            add(isle, isle.getColumn(), isle.getRow());
        }
    }

    private void addBridges() {
        Isle startIsle = isles.get(0);
        Isle endIsle = isles.get(1);
        Bridge bridge;
        if (startIsle.getRow() == endIsle.getRow()) {
            bridge = new Bridge(startIsle, endIsle, Direction.RIGHT);
        } else {
            bridge = new Bridge(startIsle, endIsle, Direction.DOWN);
        }
        add(bridge, bridge.getColumn(), bridge.getRow());
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        for (Isle isle : isles) {
            isle.setText(showMissingBridges);
        }
    }

//    private void addBridges() {
//        Isle isle1 = isles.get(0);
//        Isle isle2 = isles.get(1);
////        isle1.getLayoutX()
////        Line line = new Line(isle1.getLayoutX(), isle1.getLayoutY(), isle2.getLayoutX(), isle2.getLayoutY());
////        Line line = new Line(isle1.getTranslateX(), isle1.getTranslateY(), isle2.getTranslateX(), isle2.getTranslateY());
////        Line line = new Line(100, 100, 300, 300);
////        Line l = new Line();
////        l.setTranslateX(ONE_TILE);
////        l.setTranslateY(ONE_TILE + 20);
////        l.setStartX(30);
////        l.setStartY(30);
////        l.setEndX(80);
////        l.setEndY(80);
////        l.setStroke(Color.RED);
//        Bridge bridge = new Bridge(3,3);
//        add(bridge, bridge.getColumn(), bridge.getRow());
//    }
}
