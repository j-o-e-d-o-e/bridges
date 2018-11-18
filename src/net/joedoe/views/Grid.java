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
    private GridController controller;
    private List<IslePane> panes = new ArrayList<>();
    private List<BridgeLine> lines = new ArrayList<>();
    private EventHandler<StatusEvent> statusListener;
    private IsleListener isleListener;
    private boolean showMissingBridges = true;
    private Thread thread;

    Grid() {
//        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, Mocks.ROWS).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, Mocks.COLS).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        isleListener = new IsleListener(this);
        addIsles(Mocks.ISLES);
        controller = new GridController();
        controller.setIsles(Mocks.ISLES);
    }

    Grid(int height, int width, List<int[]> isles, List<int[]> bridges) {
//        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        isleListener = new IsleListener(this);
        addIsles(isles);
//        addBridges(bridges); // for testing only
        controller = new GridController();
        controller.setIsles(isles);
        controller.setSolution(bridges);
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
    @SuppressWarnings("unused")
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
        Coordinate[] coordinates = controller.addBridge(pane.getY(), pane.getX(), direction);
        addBridge(coordinates);
    }

    private void addBridge(Coordinate[] coordinates) {
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
            checkStatus();
        }
    }

    void removeBridge(IslePane pane, Direction direction) {
        Coordinate[] coordinates = controller.removeBridge(pane.getY(), pane.getX(), direction);
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
            checkStatus();
        }
    }

    private void updatePanes() {
        for (IslePane pane : panes) {
            int bridgeCount = controller.getMissingBridgeCount(pane.getY(), pane.getX());
            if (bridgeCount == 0) pane.setColor(SOLVED_COLOR);
            else if (bridgeCount < 0) pane.setColor(ALERT_COLOR);
            else pane.setColor(STD_COLOR);
            if (!showMissingBridges) bridgeCount = controller.getBridgeCount(pane.getY(), pane.getX());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    private void updateLines() {
        if (lines.size() > 0)
            lines.get(lines.size() - 1).setStroke(STD_COLOR);
    }

    private void checkStatus() {
        if (controller.gameSolved()) {
            lines.get(lines.size() - 1).setStroke(STD_COLOR);
            statusListener.handle(new StatusEvent(null, "Gelöst!"));
        } else if (controller.gameUnsolvable())
            statusListener.handle(new StatusEvent(null, "Nicht mehr lösbar."));
        else if (controller.errorOccured())
            statusListener.handle(new StatusEvent(null, "Enthält einen Fehler."));
        else
            statusListener.handle(new StatusEvent(null, "Noch nicht gelöst."));
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane pane : panes) {
            int bridgeCount;
            if (showMissingBridges)
                bridgeCount = controller.getMissingBridgeCount(pane.getY(), pane.getX());
            else
                bridgeCount = controller.getBridgeCount(pane.getY(), pane.getX());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    void setStatusListener(EventHandler<StatusEvent> statusListener) {
        this.statusListener = statusListener;
    }

    void reset() {
        getChildren().removeAll(lines);
        lines.clear();
        for (IslePane pane : panes) {
            pane.setColor(STD_COLOR);
            pane.setText(Integer.toString(controller.getBridgeCount(pane.getY(), pane.getX())));
        }
        controller.reset();
    }

    void showNextBridge() {
        if (!controller.gameSolved())
            addBridge(controller.showNextBridge());
    }

    void solve() {
//        thread = new Thread();
    }

    void stopThread() {
        System.out.println("Stop Thread");
        if (thread != null) thread.stop();
    }
}