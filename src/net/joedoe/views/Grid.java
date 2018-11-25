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
import net.joedoe.utils.GameInfo;
import net.joedoe.utils.Mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


    Grid() {
        this(Mocks.HEIGHT, Mocks.WIDTH, Mocks.ISLES, Mocks.BRIDGES);
    }

    Grid(int height, int width, Object[][] isles, Object[][] bridges) {
//        setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(GameInfo.STD_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE)).forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE)).forEachOrdered(column -> getColumnConstraints().add(column));
        isleListener = new IsleListener(this);
        controller = new GridController();
        checker = new StatusChecker(controller);
        solver = new Solver(controller, checker);
        autoSolver = new AutoSolver(solver);
        autoSolver.addListener(() -> Platform.runLater(this::getNextBridgeAuto));
        setIsles(isles);
        setBridges(bridges);
    }

    void addBridge(IslePane isle, Direction direction) {
        Coordinate[] coordinates = controller.addBridge(isle.getPos(), direction);
        addBridge(coordinates);
    }

    private void addBridge(Coordinate[] pos) {
        if (pos != null) {
            updateBridges();
            BridgeLine opposite = getBridge(pos[1], pos[0]);
            BridgeLine bridge;
            if (opposite == null)
                bridge = new BridgeLine(pos[0], pos[1], false);
            else {
                bridge = new BridgeLine(pos[0], pos[1], true);
                opposite.setOffset(true);
            }
            bridges.add(bridge);
            add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            updateIsles();
            checkStatus();
        }
    }

    void removeBridge(IslePane isle, Direction direction) {
        Coordinate[] pos = controller.removeBridge(isle.getPos(), direction);
        if (pos != null) {
            updateBridges();
            BridgeLine bridge = getBridge(pos[0], pos[1]);
            bridges.remove(bridge);
            getChildren().remove(bridge.getLine());
            BridgeLine opposite = getBridge(pos[1], pos[0]);
            if (opposite != null) opposite.setOffset(false);
            updateIsles();
            checkStatus();
        }
    }

    private BridgeLine getBridge(Coordinate start, Coordinate end) {
        return bridges.stream().filter(
                bridge -> bridge.getStart() == start
                        && bridge.getEnd() == end)
                .findFirst().orElse(null);
    }

    private void updateBridges(){
        if (bridges.size() > 0) bridges.get(bridges.size() - 1).setStdColor();
    }

    private void updateIsles() {
        for (IslePane isle : isles) {
            int bridges = controller.getMissingBridgeCount(isle.getPos());
            if (bridges == 0) isle.setColor(SOLVED_COLOR);
            else if (bridges < 0) isle.setColor(ALERT_COLOR);
            else isle.setColor(STD_COLOR);
            if (!showMissingBridges) bridges = controller.getBridgeCount(isle.getPos());
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
            if ((boolean) data[2]) {
                List<BridgeLine> bridges = Arrays.asList(
                        new BridgeLine(start, end, true),
                        new BridgeLine(end, start, true));
                this.bridges.addAll(bridges);
                bridges.forEach(bridge -> add(bridge.getLine(), bridge.getStartX(), bridge.getStartY()));
            } else {
                BridgeLine bridge = new BridgeLine(start, end, false);
                bridges.add(bridge);
                add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            }
        }
        bridges.forEach(BridgeLine::setStdColor);
        updateIsles();
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane isle : isles) {
            int bridgeCount;
            if (showMissingBridges)
                bridgeCount = controller.getMissingBridgeCount(isle.getPos());
            else
                bridgeCount = controller.getBridgeCount(isle.getPos());
            isle.setText(Integer.toString(bridgeCount));
        }
    }

    void reset() {
        controller.reset();
        bridges.forEach(bridge -> getChildren().remove(bridge.getLine()));
        bridges.clear();
        isles.forEach(isle -> {
            isle.setColor(STD_COLOR);
            isle.setText(Integer.toString(controller.getBridgeCount(isle.getPos())));
        });
    }

    void getNextBridge() {
        Coordinate[] next = solver.getNextBridge();
        if (next == null && !checker.solved())
            setAlert();
        else
            addBridge(next);
    }

    private void getNextBridgeAuto() {
        Coordinate[] next = autoSolver.getNextBridge();
        if (next == null && !checker.solved()) {
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

    private void checkStatus() {
        if (checker.error())
            statusListener.handle(new StatusEvent("Enthält Fehler."));
        else if (checker.unsolvable())
            statusListener.handle(new StatusEvent("Nicht mehr lösbar."));
        else if (checker.solved()) {
            bridges.get(bridges.size() - 1).setStdColor();
            statusListener.handle(new StatusEvent("Gelöst!"));
        } else
            statusListener.handle(new StatusEvent("Noch nicht gelöst."));
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