package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.joedoe.controllers.GridController;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Direction;

import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.ONE_TILE;

public class Grid extends GridPane {
    private GridController gridController;
    //for update status label in main frame
    private EventHandler<StatusEvent> listener;

    Grid() {
        this.gridController = new GridController(this);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setAlignment(Pos.CENTER);
        setGridLinesVisible(true);
        IntStream.range(0, Mocks.ROWS).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, Mocks.COLS).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        addIsles();
    }

    private void addIsles() {
        for (Isle isle : gridController.getIsles()) {
            GridPane.setConstraints(isle, isle.getColumn(), isle.getRow());
            isle.setListener(new IsleListener(this));
            add(isle, isle.getColumn(), isle.getRow());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void addBridge(Isle startIsle, Direction direction) {
        Isle endIsle = gridController.searchForIsle(startIsle, direction);
        if (endIsle != null) {
            Bridge bridge = new Bridge(startIsle, direction, endIsle);
            add(bridge, bridge.getColumn(), bridge.getRow());
            listener.handle(new StatusEvent(null, endIsle.toString()));
        }
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        for (Isle isle : gridController.getIsles()) {
            isle.setText(showMissingBridges);
        }
    }

    void removeBridge(Isle isle, Direction direction) {
        System.out.println(isle + " " + direction);
    }

    void setListener(EventHandler<StatusEvent> listener) {
        this.listener = listener;
    }
}