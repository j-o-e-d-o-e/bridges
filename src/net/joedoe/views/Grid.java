package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.entities.Isle;
import net.joedoe.entities.MockData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static net.joedoe.GameInfo.CIRCLE_RADIUS;

class Grid extends GridPane {
    private List<Isle> isles = new ArrayList<>();
    private EventHandler<Event> listener;

    Grid() {
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setGridLinesVisible(true);
        IntStream.range(0, MockData.ROWS).mapToObj(i -> new RowConstraints(CIRCLE_RADIUS * 2)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, MockData.COLS).mapToObj(i -> new ColumnConstraints(CIRCLE_RADIUS * 2)).forEachOrdered(column -> getColumnConstraints().add(column));
        addIsles();
        addBridges();
    }

    private void addBridges() {
        Isle isle1 = isles.get(0);
        Isle isle2 = isles.get(1);
        add(new Line(0, 0, 250, 250), 1, 1);
//        add(new Line(),isle1.getColumn(),isle1.getRow(), isle2.getColumn(), isle2.getRow());
    }

    private void addIsles() {
        isles.addAll(MockData.ISLES);
        for (Isle isle : isles) {
            isle.setOnMouseClicked(e -> {
                isle.getCircle().setFill(Color.RED);
                listener.handle(e);
            });
            GridPane.setConstraints(isle, isle.getColumn(), isle.getRow());
            // add isles on lines, not in boxes:
            // add(isle, isle.getColumn(), isle.getRow(), 2, 2);
            add(isle, isle.getColumn(), isle.getRow());
        }
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
