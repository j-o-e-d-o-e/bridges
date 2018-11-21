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
            BridgeLine bridge = new BridgeLine(data[0], data[1]);
            bridges.add(bridge);
            add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
        }
        updateIsles();
        bridges.forEach(BridgeLine::setStdColor);
    }

    private void addIsles(List<int[]> islesData) {
        for (int[] data : islesData) {
            IslePane isle = new IslePane(data[1], data[0], data[2]);
            isle.setOnMouseClicked(e -> isleListener.handle(e));
            isles.add(isle);
            add(isle, isle.getX(), isle.getY());
        }
    }

    void addBridge(IslePane isle, Direction direction) {
        Coordinate[] coordinates = controller.addBridge(isle.getX(), isle.getY(), direction);
        addBridge(coordinates);
    }

    private void addBridge(Coordinate[] data) {
        if (data != null) {
            updateBridges();
            BridgeLine bridge = new BridgeLine(data[0], data[1]);
            bridges.add(bridge);
            add(bridge.getLine(), bridge.getStartX(), bridge.getStartY());
            updateIsles();
            checkStatus();
        }
    }

    void removeBridge(IslePane isle, Direction direction) {
        Coordinate[] data = controller.removeBridge(isle.getX(), isle.getY(), direction);
        if (data != null) {
            updateBridges();
            BridgeLine bridge = getBridge(data[0], data[1]);
            bridges.remove(bridge);
            getChildren().remove(bridge.getLine());
            updateIsles();
            checkStatus();
        }
    }

    private BridgeLine getBridge(Coordinate start, Coordinate end){
        return bridges.stream().filter(
                bridge -> bridge.getStartY() == start.getY()
                        && bridge.getStartX() == start.getX()
                        && bridge.getEndY() == end.getY()
                        && bridge.getEndX() == end.getX()).findFirst().orElse(null);
    }

    private void updateIsles() {
        for (IslePane isle : isles) {
            int bridgeCount = controller.getMissingBridgeCount(isle.getX(), isle.getY());
            if (bridgeCount == 0) isle.setColor(SOLVED_COLOR);
            else if (bridgeCount < 0) isle.setColor(ALERT_COLOR);
            else isle.setColor(STD_COLOR);
            if (!showMissingBridges) bridgeCount = controller.getBridgeCount(isle.getX(), isle.getY());
            isle.setText(Integer.toString(bridgeCount));
        }
    }

    private void updateBridges() {
        if (bridges.size() > 0)
            bridges.get(bridges.size() - 1).setStdColor();
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

    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane isle : isles) {
            int bridgeCount;
            if (showMissingBridges)
                bridgeCount = controller.getMissingBridgeCount(isle.getX(), isle.getY());
            else
                bridgeCount = controller.getBridgeCount(isle.getX(), isle.getY());
            isle.setText(Integer.toString(bridgeCount));
        }
    }

    void reset() {
        for (BridgeLine bridge : bridges) {
            getChildren().remove(bridge.getLine());
        }
        bridges.clear();
        for (IslePane isle : isles) {
            isle.setColor(STD_COLOR);
            isle.setText(Integer.toString(controller.getBridgeCount(isle.getX(), isle.getY())));
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