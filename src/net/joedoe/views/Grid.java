package net.joedoe.views;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import net.joedoe.logics.AutoSolver;
import net.joedoe.logics.GridController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.*;

class Grid extends GridPane {
    private GridController controller;
    private List<IslePane> isles = new ArrayList<>();
    private List<BridgeLine> bridges = new ArrayList<>();
    private EventHandler<StatusEvent> statusListener;
    private IsleListener isleListener;
    private boolean showMissingBridges = true;
    private StatusChecker checker;
    private Solver solver;
    private AutoSolver autoSolver;

    Grid(EventHandler<StatusEvent> statusListener, int width, int height, Object[][] islesData,
            Object[][] bridgesData) {
        // setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(
                new BorderStroke(STD_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE))
                .forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE))
                .forEachOrdered(column -> getColumnConstraints().add(column));
        this.statusListener = statusListener;
        isleListener = new IsleListener(this);
        controller = new GridController();
        checker = new StatusChecker(controller);
        solver = new Solver(controller);
        autoSolver = new AutoSolver(solver);
        autoSolver.addListener(() -> Platform.runLater(this::getNextBridgeAuto));
        setIsles(islesData);
        if (bridgesData != null) setBridges(bridgesData);
    }

    void addBridge(IslePane isle, Direction direction) {
        Coordinate[] coordinates = controller.addBridge(isle.getPos(), direction);
        addBridge(coordinates);
    }

    private void addBridge(Coordinate[] pos) {
        if (pos != null) {
            updateBridges();
            List<BridgeLine> bridges = getBridges(pos[0], pos[1]);
            if (bridges.size() == 0) {
                BridgeLine bridge = new BridgeLine(pos[0], pos[1], false);
                this.bridges.add(bridge);
                add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            } else if (bridges.size() == 1) {
                BridgeLine bridge = bridges.get(0);
                bridge.setOffset(true);
                bridge = new BridgeLine(pos[1], pos[0], true);
                this.bridges.add(bridge);
                add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            } else {
                return;
            }
            updateIsles();
            checkStatus();
        }
    }

    void removeBridge(IslePane isle, Direction direction) {
        Coordinate[] pos = controller.removeBridge(isle.getPos(), direction);
        removeBridge(pos);
    }

    private void removeBridge(Coordinate[] pos) {
        if (pos != null) {
            updateBridges();
            List<BridgeLine> bridges = getBridges(pos[0], pos[1]);
            if (bridges.size() == 0) {
                return;
            } else if (bridges.size() == 1) {
                BridgeLine bridge = bridges.get(0);
                this.bridges.remove(bridge);
                getChildren().remove(bridge.getLine());
            } else {
                BridgeLine bridge = bridges.get(0);
                bridge.setOffset(false);
                bridge = bridges.get(1);
                this.bridges.remove(bridge);
                getChildren().remove(bridge.getLine());
            }
            updateIsles();
            checkStatus();
        }
    }

    private List<BridgeLine> getBridges(Coordinate start, Coordinate end) {
        return bridges.stream().filter(bridge -> bridge.contains(start, end)).collect(Collectors.toList());
    }

    private void updateBridges() {
        if (bridges.size() > 0) bridges.get(bridges.size() - 1).setStdColor();
    }

    private void updateIsles() {
        for (IslePane isle : isles) {
            int bridges = controller.getMissingBridges(isle.getPos());
            if (bridges == 0) isle.setColor(SOLVED_COLOR);
            else if (bridges < 0) isle.setColor(ALERT_COLOR);
            else isle.setColor(STD_COLOR);
            if (!showMissingBridges) bridges = controller.getBridges(isle.getPos());
            isle.setText(Integer.toString(bridges));
        }
    }

    private void setIsles(Object[][] islesData) {
        controller.setIsles(islesData);
        for (Object[] data : islesData) {
            IslePane isle = new IslePane((Coordinate) data[0], (int) data[1]);
            isle.setOnMouseClicked(e -> isleListener.handle(e));
            isles.add(isle);
            add(isle, isle.getX(), isle.getY());
        }
    }

    private void setBridges(Object[][] bridgesData) {
        controller.setBridges(bridgesData);
        for (Object[] data : bridgesData) {
            Coordinate start = (Coordinate) data[0];
            Coordinate end = (Coordinate) data[1];
            BridgeLine bridge = new BridgeLine(start, end, false);
            bridge.setStdColor();
            bridges.add(bridge);
            add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            if ((boolean) data[2]) {
                bridge.setOffset(true);
                bridge = new BridgeLine(end, start, true);
                bridge.setStdColor();
                bridges.add(bridge);
                add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            }
        }
        updateIsles();
        checkStatus();
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane isle : isles) {
            int bridges;
            if (showMissingBridges) bridges = controller.getMissingBridges(isle.getPos());
            else bridges = controller.getBridges(isle.getPos());
            isle.setText(Integer.toString(bridges));
        }
    }

    void reset() {
        controller.reset();
        bridges.forEach(bridge -> getChildren().remove(bridge.getLine()));
        bridges.clear();
        isles.forEach(isle -> {
            isle.setColor(STD_COLOR);
            isle.setText(Integer.toString(controller.getBridges(isle.getPos())));
        });
        autoSolver.stop();
    }

    void getNextBridge() {
        if (checker.error() || checker.unsolvable() || checker.solved()) return;
        Coordinate[] next = solver.getNextBridge();
        if (next != null) addBridge(next);
        else if (!checker.solved()) setAlert();
    }

    private void getNextBridgeAuto() {
        if (checker.error() || checker.unsolvable() || checker.solved()) {
            autoSolver.stop();
            return;
        }
        Coordinate[] next = autoSolver.getNextBridge();
        if (next != null) {
            addBridge(next);
        } else if (checker.solved()) {
            autoSolver.stop();
        } else {
            autoSolver.stop();
            setAlert();
        }
    }

    void startAutoSolve() {
        if (!autoSolver.isRunning()) autoSolver.start();
    }

    void stopAutoSolve() {
        if (autoSolver.isRunning()) autoSolver.stop();
    }

    void shutdownAutoSolve() {
        autoSolver.shutdown();
    }
    
    boolean autoSolverIsRunning() {
        return autoSolver.isRunning();
    }

    private void checkStatus() {
        if (checker.error()) {
            statusListener.handle(new StatusEvent("Enthält Fehler."));
        } else if (checker.unsolvable()) {
            statusListener.handle(new StatusEvent("Nicht mehr lösbar."));
        } else if (checker.solved()) {
            bridges.get(bridges.size() - 1).setStdColor();
            statusListener.handle(new StatusEvent("Gelöst!"));
        } else {
            statusListener.handle(new StatusEvent("Noch nicht gelöst."));
        }
    }

    private void setAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nächste Brücke");
        alert.setHeaderText("Keine Brücke gefunden, " + "die sicher ergänzt werden kann.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }

    public void saveGame() {
        controller.saveGame();
    }
}