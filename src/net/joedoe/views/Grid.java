package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.MockData;

import java.util.List;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.ONE_TILE;

class Grid extends GridPane {
    private List<Isle> isles = MockData.ISLES;
    private EventHandler<Event> listener;

    Grid() {
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setAlignment(Pos.CENTER);
        setGridLinesVisible(true);
        IntStream.range(0, MockData.ROWS).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, MockData.COLS).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        addIsles();
        addBridges();
    }

    private void addIsles() {
        for (Isle isle : isles) {
            GridPane.setConstraints(isle, isle.getColumn(), isle.getRow());
            isle.setListener(e -> listener.handle(e));
            add(isle, isle.getColumn(), isle.getRow());
        }
    }

    private void addBridges() {
        isles.get(0).addBridge(new Bridge(isles.get(0), isles.get(1)));
        isles.get(1).addBridge(new Bridge(isles.get(1), isles.get(0)));

//        addBridge(isles.get(1), isles.get(2));
//        addBridge(isles.get(2), isles.get(1));

//        addBridge(isles.get(2), isles.get(3));
//        addBridge(isles.get(3), isles.get(2));

//        addBridge(isles.get(3), isles.get(4));
//        addBridge(isles.get(4), isles.get(3));

//        addBridge(isles.get(4), isles.get(5));
//        addBridge(isles.get(5), isles.get(4));

//        addBridge(isles.get(5), isles.get(6));
//        addBridge(isles.get(6), isles.get(5));

//        addBridge(isles.get(7), isles.get(6));
//        addBridge(isles.get(6), isles.get(7));
//
//        addBridge(isles.get(2), isles.get(7));
//        addBridge(isles.get(7), isles.get(2));
    }

//    private Bridge addBridge(GridEntity startIsle, GridEntity endIsle) {
//        Bridge bridge = new Bridge(startIsle, endIsle);
//        add(bridge, bridge.getColumn(), bridge.getRow());
//    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        for (Isle isle : isles) {
            isle.setText(showMissingBridges);
        }
    }
}
