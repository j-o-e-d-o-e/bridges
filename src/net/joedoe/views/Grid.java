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

class Grid extends GridPane {
    private GridController gridController;
    private EventHandler<StatusEvent> statusListener;
    private IsleListener isleListener;

    Grid() {
//        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, Mocks.ROWS).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, Mocks.COLS).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        gridController = new GridController();
        isleListener = new IsleListener(this);
        addIsles();
    }

    private void addIsles() {
        for (Isle isle : gridController.getIsles()) {
            isle.getPane().setOnMouseClicked(e -> isleListener.handle(e));
            add(isle.getPane(), isle.getColumn(), isle.getRow());
        }
    }

    void addBridge(Isle startIsle, Direction direction) {
        Bridge bridge = gridController.createBridge(startIsle, direction);
        if (bridge != null)
            add(bridge.getLine(), bridge.getStartColumn(), bridge.getStartRow());
        if (gridController.gameSolved())
            statusListener.handle(new StatusEvent(null, "Gelöst!"));
    }

    void removeBridge(Isle startIsle, Direction direction) {
        Bridge bridge = gridController.removeBridge(startIsle, direction);
        if (bridge != null) {
            getChildren().remove(bridge.getLine());
        }
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        for (Isle isle : gridController.getIsles()) {
            isle.setText(showMissingBridges);
        }
    }

    void setStatusListener(EventHandler<StatusEvent> statusListener) {
        this.statusListener = statusListener;
    }
}