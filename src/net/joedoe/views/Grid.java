package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.controllers.GridController;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Direction;

import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.*;

public class Grid extends GridPane {
    private GridController gridController;
    //for update status label in main frame
    private EventHandler<StatusEvent> listener;

    Grid() {
        gridController = new GridController();
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
            Line line = null;
            if (direction == Direction.RIGHT) {
                int length = Math.abs(bridge.getStartColumn() - bridge.getEndColumn());
                line = new Line(0, 0, ONE_TILE * length - CIRCLE_RADIUS * 2, 0);
                line.setTranslateX(BRIDGE_EXTRA + 2 * CIRCLE_RADIUS);
                line.setTranslateY(BRIDGE_OFFSET);
                line.setStroke(Color.RED);
            }
            if (direction == Direction.LEFT) {
                int length = Math.abs(bridge.getStartColumn() - bridge.getEndColumn());
                line = new Line(0, 0, ONE_TILE * length - CIRCLE_RADIUS * 2, 0);
                line.setTranslateX(-ONE_TILE * (length - 1) - BRIDGE_EXTRA);
                line.setTranslateY(-BRIDGE_OFFSET);
                line.setStroke(Color.PURPLE);
            }
            if (direction == Direction.DOWN) {
                int length = Math.abs(bridge.getStartRow() - bridge.getEndRow());
//                line = new Line(0, 0, 0, ONE_TILE * length + BRIDGE_EXTRA);
                line = new Line(0, 0, 0, ONE_TILE + BRIDGE_EXTRA);
                line.setTranslateX(CIRCLE_RADIUS);
                line.setTranslateY(ONE_TILE - BRIDGE_EXTRA);
                line.setStroke(Color.BLUE);
            }
            if (direction == Direction.UP) {
                int length = Math.abs(bridge.getStartRow() - bridge.getEndRow());
                line = new Line(0, 0, 0, ONE_TILE + BRIDGE_EXTRA);
                line.setTranslateX(CIRCLE_RADIUS);
                line.setTranslateY(ONE_TILE - BRIDGE_EXTRA);
                line.setStroke(Color.DARKBLUE);
            }
            if (line != null) add(line, bridge.getStartColumn(), bridge.getStartRow());
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