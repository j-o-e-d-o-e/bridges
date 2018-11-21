package net.joedoe.views;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.joedoe.logics.AutoSolver;
import net.joedoe.logics.GridController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.*;

class Grid extends GridPane {
    private GridController controller;
    private List<IslePane> panes = new ArrayList<>();
    private List<BridgeLine> lines = new ArrayList<>();
    private EventHandler<StatusEvent> statusListener;
    private IsleListener isleListener;
    private boolean showMissingBridges = true;
    private StatusChecker checker;
    private Solver solver;
    private AutoSolver autoSolver;


    Grid() {
        this(Mocks.HEIGHT, Mocks.WIDTH, Mocks.ISLES, Mocks.BRIDGES);
    }

    Grid(int height, int width, List<int[]> isles, List<Coordinate[]> bridges) {
        //        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        isleListener = new IsleListener(this);
        addIsles(isles);
        controller = new GridController();
        controller.setIsles(isles);
        checker = new StatusChecker(controller);
        solver = new Solver(controller, checker);
        autoSolver = new AutoSolver(solver);
        autoSolver.addListener(() ->
                Platform.runLater(this::getNextBridgeAuto)
        );
//        setSolution(bridges);
    }

    //for testing only
    @SuppressWarnings("unused")
    private void setSolution(List<Coordinate[]> bridgesData) {
        controller.setBridges(bridgesData);
        for (Coordinate[] data : bridgesData) {
            BridgeLine line = new BridgeLine(
                    data[0].getX(), data[0].getY(),
                    data[1].getX(), data[1].getY()
            );
            lines.add(line);
            add(line, line.getXStart(), line.getYStart());
        }
        updatePanes();
        lines.forEach(line -> line.setStroke(STD_COLOR));
    }

    private void addIsles(List<int[]> isles) {
        for (int[] isle : isles) {
            IslePane pane = new IslePane(isle[1], isle[0], isle[2]);
            pane.setOnMouseClicked(e -> isleListener.handle(e));
            panes.add(pane);
            add(pane, pane.getX(), pane.getY());
        }
    }

    void addBridge(IslePane pane, Direction direction) {
        Coordinate[] coordinates = controller.addBridge(pane.getX(), pane.getY(), direction);
        addBridge(coordinates);
    }

    private void addBridge(Coordinate[] data) {
        if (data != null) {
            updateLines();
            BridgeLine line = new BridgeLine(data[0].getX(), data[0].getY(), data[1].getX(), data[1].getY());
            lines.add(line);
            add(line, line.getXStart(), line.getYStart());
            updatePanes();
            checkStatus();
        }
    }

    void removeBridge(IslePane pane, Direction direction) {
        Coordinate[] data = controller.removeBridge(pane.getX(), pane.getY(), direction);
        if (data != null) {
            updateLines();
            BridgeLine line = lines.stream().filter(
                    l -> l.getYStart() == data[0].getY()
                            && l.getXStart() == data[0].getX()
                            && l.getYEnd() == data[1].getY()
                            && l.getXEnd() == data[1].getX()).findFirst().orElse(null);
            lines.remove(line);
            getChildren().remove(line);
            updatePanes();
            checkStatus();
        }
    }

    private void updatePanes() {
        for (IslePane pane : panes) {
            int bridgeCount = controller.getMissingBridgeCount(pane.getX(), pane.getY());
            if (bridgeCount == 0) pane.setColor(SOLVED_COLOR);
            else if (bridgeCount < 0) pane.setColor(ALERT_COLOR);
            else pane.setColor(STD_COLOR);
            if (!showMissingBridges) bridgeCount = controller.getBridgeCount(pane.getX(), pane.getY());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    private void updateLines() {
        if (lines.size() > 0)
            lines.get(lines.size() - 1).setStroke(STD_COLOR);
    }

    private void checkStatus() {
        if (checker.error())
            statusListener.handle(new StatusEvent("Enthält Fehler."));
        else if (checker.unsolvable())
            statusListener.handle(new StatusEvent("Nicht mehr lösbar."));
        else if (checker.solved()) {
            lines.get(lines.size() - 1).setStroke(STD_COLOR);
            statusListener.handle(new StatusEvent("Gelöst!"));
        } else
            statusListener.handle(new StatusEvent("Noch nicht gelöst."));
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane pane : panes) {
            int bridgeCount;
            if (showMissingBridges)
                bridgeCount = controller.getMissingBridgeCount(pane.getX(), pane.getY());
            else
                bridgeCount = controller.getBridgeCount(pane.getX(), pane.getY());
            pane.setText(Integer.toString(bridgeCount));
        }
    }

    void reset() {
        getChildren().removeAll(lines);
        lines.clear();
        for (IslePane pane : panes) {
            pane.setColor(STD_COLOR);
            pane.setText(Integer.toString(controller.getBridgeCount(pane.getX(), pane.getY())));
        }
        controller.reset();
    }

    void getNextBridge() {
        Coordinate[] next = solver.getNextBridge();
        if (next == null)
            setAlert();
        else
            addBridge(next);
    }

    private void getNextBridgeAuto() {
        Coordinate[] next = autoSolver.getNextBridge();
        if (next == null) {
            autoSolver.stop();
            setAlert();
        } else
            addBridge(next);
    }

    void startAutoSolve() {
        if (!autoSolver.isRunning())
            autoSolver.start();
    }

    void stopAutoSolve() {
        if (autoSolver.isRunning())
            autoSolver.stop();
    }

    void shutdownAutoSolve() {
        autoSolver.shutdown();
    }


    private void setAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nächste Brücke");
        alert.setHeaderText("Keine Brücke gefunden, " +
                "die sicher ergänzt werden kann.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            alert.close();
    }

    void setStatusListener(EventHandler<StatusEvent> statusListener) {
        this.statusListener = statusListener;
    }
}