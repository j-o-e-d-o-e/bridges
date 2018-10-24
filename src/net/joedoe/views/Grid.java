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

class Grid extends GridPane {
    private List<Isle> isles = new ArrayList<>();
    private EventHandler<Event> listener;

    Grid() {
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
}
