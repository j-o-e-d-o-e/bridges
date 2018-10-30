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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.*;

class Grid extends GridPane {
    private GridController gridController;
    private List<IslePane> panes = new ArrayList<>();
    private List<BridgeLine> lines = new ArrayList<>();
    private EventHandler<StatusEvent> statusListener;
    private IsleListener isleListener;
    private boolean showMissingBridges = true;

    Grid() {
        setGridLinesVisible(true);
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
            IslePane pane = new IslePane(isle.getRow(), isle.getColumn(), isle.getMissingBridgeCount());
            pane.setOnMouseClicked(e -> isleListener.handle(e));
            panes.add(pane);
            add(pane, pane.getY(), pane.getX());
        }
    }

    void addBridge(IslePane pane, Direction direction) {
        Bridge bridge = gridController.addBridge(pane.getX(), pane.getY(), direction);
        if (bridge != null) {
            updatePanes();
            updateLines();
            BridgeLine line = new BridgeLine(bridge.getStartColumn(), bridge.getEndColumn(), bridge.getStartRow(), bridge.getEndRow(), bridge.getAlignment());
            lines.add(line);
            add(line, line.getX1(), line.getY1());
            checkIfSolved();
        }
    }

    private void updatePanes() {
        for (IslePane pane : panes) {
            int bridgeCount = gridController.getMissingBridgeCount(pane.getX(), pane.getY());
            if (bridgeCount == 0)
                pane.setColor(SOLVED_COLOR);
            else if (bridgeCount < 0)
                pane.setColor(ALERT_COLOR);
            else
                pane.setColor(STD_COLOR);
            if (!showMissingBridges)
                bridgeCount = gridController.getBridgeCount(pane.getX(), pane.getY());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    private void updateLines() {
        if (lines.size() > 0)
            lines.get(lines.size() - 1).setStroke(STD_COLOR);
    }

    private void checkIfSolved() {
        if (gridController.gameSolved()) {
            lines.get(lines.size() - 1).setStroke(STD_COLOR);
            statusListener.handle(new StatusEvent(null, "Gelöst!"));
        }
    }

    void removeBridge(IslePane pane, Direction direction) {
        Bridge bridge = gridController.removeBridge(pane.getX(), pane.getY(), direction);
        if (bridge != null) {
            BridgeLine line = lines.stream().filter(l -> l.getX1() == bridge.getStartRow()
                    && l.getX2() == bridge.getEndRow()
                    && l.getY1() == bridge.getStartColumn()
                    && l.getY2() == bridge.getEndColumn()).findFirst().orElse(null);
            getChildren().remove(line);
        }
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane pane : panes) {
            int bridgeCount;
            if (showMissingBridges)
                bridgeCount = gridController.getMissingBridgeCount(pane.getX(), pane.getY());
            else
                bridgeCount = gridController.getBridgeCount(pane.getX(), pane.getY());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    void setStatusListener(EventHandler<StatusEvent> statusListener) {
        this.statusListener = statusListener;
    }
}