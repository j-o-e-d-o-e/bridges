package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.joedoe.controllers.GridController;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Coordinate;
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
        isleListener = new IsleListener(this);
        addIsles(Mocks.ISLES);
        gridController = new GridController();
        gridController.setIsles(Mocks.ISLES);
    }

    Grid(int height, int width, List<int[]> isles, List<int[]> bridges) {
        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        isleListener = new IsleListener(this);
        addIsles(isles);
//        addBridges(bridges); // for testing only
        gridController = new GridController();
        gridController.setIsles(isles);
        gridController.setBridges(bridges);
    }

    private void addIsles(List<int[]> isles) {
        for (int[] isle : isles) {
            IslePane pane = new IslePane(isle[0], isle[1], isle[2]);
            pane.setOnMouseClicked(e -> isleListener.handle(e));
            panes.add(pane);
            add(pane, pane.getX(), pane.getY());
        }
    }

    //    for testing only
    private void addBridges(List<int[]> bridges) {
        for (int[] bridge : bridges) {
            BridgeLine line = new BridgeLine(
                    bridge[0],
                    bridge[1],
                    bridge[2],
                    bridge[3]
            );
            lines.add(line);
            add(line, line.getXStart(), line.getYStart());
        }
    }

    void addBridge(IslePane pane, Direction direction) {
        Coordinate[] coordinates = gridController.addBridge(pane.getY(), pane.getX(), direction);
        if (coordinates != null) {
            updateLines();
            BridgeLine line = new BridgeLine(
                    coordinates[0].getY(),
                    coordinates[0].getX(),
                    coordinates[1].getY(),
                    coordinates[1].getX());
            lines.add(line);
            add(line, line.getXStart(), line.getYStart());
            updatePanes();
            checkIfSolved();
        }
    }

    void removeBridge(IslePane pane, Direction direction) {
        Coordinate[] coordinates = gridController.removeBridge(pane.getY(), pane.getX(), direction);
        if (coordinates != null) {
            updateLines();
            BridgeLine line = lines.stream().filter(
                    l -> l.getYStart() == coordinates[0].getY()
                            && l.getXStart() == coordinates[0].getX()
                            && l.getYEnd() == coordinates[1].getY()
                            && l.getXEnd() == coordinates[1].getX()).findFirst().orElse(null);
            lines.remove(line);
            getChildren().remove(line);
            updatePanes();
            checkIfSolved();
        }
    }

    private void updatePanes() {
        for (IslePane pane : panes) {
            int bridgeCount = gridController.getMissingBridgeCount(pane.getY(), pane.getX());
            if (bridgeCount == 0)
                pane.setColor(SOLVED_COLOR);
            else if (bridgeCount < 0)
                pane.setColor(ALERT_COLOR);
            else
                pane.setColor(STD_COLOR);
            if (!showMissingBridges)
                bridgeCount = gridController.getBridgeCount(pane.getY(), pane.getX());
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

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane pane : panes) {
            int bridgeCount;
            if (showMissingBridges)
                bridgeCount = gridController.getMissingBridgeCount(pane.getY(), pane.getX());
            else
                bridgeCount = gridController.getBridgeCount(pane.getY(), pane.getX());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    void setStatusListener(EventHandler<StatusEvent> statusListener) {
        this.statusListener = statusListener;
    }
}