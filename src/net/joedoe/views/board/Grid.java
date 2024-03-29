package net.joedoe.views.board;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.logics.AutoSolver;
import net.joedoe.logics.BridgeController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.ONE_TILE;
import static net.joedoe.views.board.StatusEvent.Status;

/**
 * Das Raster, auf dem Inseln und Br�cken platziert werden.
 */
class Grid extends GridPane {
    private GridController gridController;
    private BridgeController controller;
    private StatusChecker checker;
    private Solver solver;
    private AutoSolver autoSolver;
    private EventHandler<StatusEvent> statusListener;
    private EventHandler<PointEvent> pointListener;
    private boolean showMissingBridges;

    /**
     * Grid wird der StatusListener, der die Status-Zeile im
     * {@link Board} benachrichtigt, �bergeben sowie die Breite
     * und H�he des Spielfelds und die Listen mit den zu platzierenden Inseln und
     * Br�cken.
     *
     * @param statusListener Listener, der die Status-Zeile benachrichtigt
     * @param width          Breite des Spielfelds
     * @param height         H�he des Spielfelds
     * @param isles          Liste mit den zu platzierenden Inseln
     * @param bridges        Liste mit den zu platzierenden Br�cken
     */
    Grid(EventHandler<StatusEvent> statusListener, int width, int height, List<IIsle> isles, List<IBridge> bridges) {
        setId("grid");
        // Gr��e des Rasters festlegen
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE))
                .forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE))
                .forEachOrdered(column -> getColumnConstraints().add(column));

        gridController = new GridController();
        controller = new BridgeController();
        checker = new StatusChecker(controller);
        solver = new Solver(controller, checker);
        autoSolver = new AutoSolver(solver);
        autoSolver.setListener(() -> Platform.runLater(this::getNextBridgeAuto));
        this.statusListener = statusListener;
        controller.setIsles(isles);
        setIsles(isles);
        if (bridges != null) {
            controller.setBridges(bridges);
            setBridges(bridges);
        }
        checkStatus();
    }

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Aufgerufen von Nutzer.
     *
     * @param isle      Insel (View), die vom Nutzer angeklickt wurde
     * @param direction Richtung, in die der Nutzer mittels Klick-Sektor geklickt hat
     */
    private void addBridge(IslePane isle, Direction direction) {
        IBridge bridge = controller.getBridge(isle.getPos(), direction);
        if (bridge != null && bridge.isDoubleBridge()) {
            removeDoubleBridge(bridge);
            return;
        }
        bridge = controller.addBridge(isle.getPos(), direction);
        if (bridge == null) return;
        pointListener.handle(new PointEvent(10));
        addBridge(bridge);
    }

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Aufgerufen von Programm.
     *
     * @param bridge hinzuzuf�gende Br�cke (Modell)
     */
    private void addBridgeAuto(IBridge bridge) {
        pointListener.handle(new PointEvent(-10));
        addBridge(bridge);
    }

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Aufgerufen entweder von Nutzer oder von Programm.
     *
     * @param bridge hinzuzuf�gende Br�cke (Modell)
     */
    private void addBridge(IBridge bridge) {
        BridgeLine line = gridController.addLine(bridge);
        if (line == null) return;
        add(line.getLine(), line.getStartX(), line.getStartY());
        updateIsles();
        checkStatus();
    }

    /**
     * Entfernt Br�cken-Linie, falls m�glich. Aufgerufen von Nutzer.
     *
     * @param isle      Insel (View), die vom Nutzer angeklickt wurde
     * @param direction Richtung, in die der Nutzer mittels Klick-Sektor geklickt hat
     */
    private void removeBridge(IslePane isle, Direction direction) {
        IBridge bridge = controller.removeBridge(isle.getPos(), direction);
        if (bridge == null) return;
        removeBridge(bridge);
    }

    private void removeDoubleBridge(IBridge bridge) {
        controller.removeDoubleBridge(bridge);
        List<BridgeLine> lines = gridController.removeLines(bridge);
        lines.forEach(l -> getChildren().remove(l.getLine()));
        pointListener.handle(new PointEvent(-20));
        updateIsles();
        checkStatus();
    }

    void undoBridge() {
        Command command = controller.undoBridge();
        if (command == null) {
            setAlert("Undo", "No bridge found to undo.");
            return;
        }
        if (command.isAdd()) removeBridge(command.getBridge());
        else if (command.isDoubleRemove()) {
            List<BridgeLine> lines = gridController.addLines(command.getBridge());
            lines.forEach(l -> add(l.getLine(), l.getStartX(), l.getStartY()));
            pointListener.handle(new PointEvent(20));
        } else {
            pointListener.handle(new PointEvent(10));
            addBridge(command.getBridge());
        }
        gridController.getLines().forEach(BridgeLine::setStdColor);
    }

    private void removeBridge(IBridge bridge) {
        BridgeLine line = gridController.removeLine(bridge);
        if (line == null) return;
        getChildren().remove(line.getLine());
        pointListener.handle(new PointEvent(-10));
        updateIsles();
        checkStatus();
    }

    /**
     * Legt die Farbe der Inseln entsprechend ihrer aktuellen Anzahl an fehlenden
     * Br�cken fest und aktualisiert die Angabe an (fehlenden) Br�cken.
     */
    private void updateIsles() {
        for (IslePane isle : gridController.getPanes()) {
            // Farbe aktualisieren
            isle.getStyleClass().removeAll("isle-alert", "isle-solved");
            int bridges = controller.getMissingBridges(isle.getPos());
            if (bridges == 0)
                isle.getStyleClass().add("isle-solved");
            else if (bridges < 0)
                isle.getStyleClass().add("isle-alert");
            // Br�cken-Anzahl aktualisieren
            if (!showMissingBridges) bridges = controller.getBridges(isle.getPos());
            isle.setText(Integer.toString(bridges));
        }
    }

    /**
     * Checkt den Spielstatus und gibt Status an Status-Zeile in
     * {@link Board} zur�ck.
     */
    private void checkStatus() {
        if (checker.error()) {
            statusListener.handle(new StatusEvent(Status.ERROR));
        } else if (checker.unsolvable()) {
            statusListener.handle(new StatusEvent(Status.UNSOLVABLE));
        } else if (checker.solved()) {
            gridController.updateLines();
            pointListener.handle(new PointEvent(50));
            statusListener.handle(new StatusEvent(Status.SOLVED));
        } else {
            statusListener.handle(new StatusEvent(Status.NOT_SOLVED));
        }
    }

    /**
     * L�dt Inseln.
     *
     * @param isles Liste mit zu ladenen Inseln
     */
    private void setIsles(List<IIsle> isles) {
        gridController.setPanes(isles);
        for (IslePane isle : gridController.getPanes()) {
            add(isle, isle.getX(), isle.getY());
            addDiamonds(isle);
        }
    }

    /**
     * Vier "Diamanten" f�r jede Insel als Klick-Sektoren. Sie sind transparente Quadrate,
     * die auf einer Ecke stehen und zur H�lfte in die n�chste angrenzende Zelle reinragen.
     * Sie bestehen also quasi aus zwei Dreiecken - eins in der Zelle der Inseln, das andere
     * in der n�chsten Zelle. Jeder "Diamant" repr�sentiert eine Richtung. Drauf geklickt,
     * werden Br�cken hinzugef�gt bzw. entfernt.
     *
     * @param isle Insel, f�r die die vier Klick-Sektoren erstellt werden
     */
    //TODO: Zwei Diamonds mit selber Ausrichtung k�nnen sich anscheinend gegenseitig st�ren
    //TODO: F�r mobile: Diamonds und Insel zu einer gr��eren Fl�che, Richtung �ber Swipe
    //TODO: Alternativ kein Listener mehr f�r Inseln od Diamonds, statt dessen die beiden Inseln anhand Koordinaten und Richtung des Swipes ermitteln
    private void addDiamonds(IslePane isle) {
        double len = Math.sqrt((ONE_TILE * ONE_TILE) >> 1);
        for (Direction direction : Direction.values()) {
//            if(direction != Direction.DOWN) continue;
            Rectangle diamond = new Rectangle(0, 0, len, len);
            setHalignment(diamond, HPos.CENTER);
            setValignment(diamond, VPos.CENTER);
            diamond.getTransforms().add(new Rotate(45, len / 2, len / 2));
//            diamond.setOnSwipeDown(e -> System.out.println(e.getSceneX()));
            diamond.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) addBridge(isle, direction);
                else removeBridge(isle, direction);
            });
            diamond.setFill(Color.TRANSPARENT);
            switch (direction) {
                case UP:
//                    diamond.setFill(Color.BLANCHEDALMOND);
                    diamond.setTranslateY(-ONE_TILE >> 1);
                    break;
                case LEFT:
//                    diamond.setFill(Color.DARKGOLDENROD);
                    diamond.setTranslateX(-ONE_TILE >> 1);
                    break;
                case DOWN:
//                    diamond.setFill(Color.DARKMAGENTA);
                    diamond.setTranslateY(ONE_TILE >> 1);
                    break;
                case RIGHT:
//                    diamond.setFill(Color.DARKOLIVEGREEN);
                    diamond.setTranslateX(ONE_TILE >> 1);
            }
            add(diamond, isle.getX(), isle.getY());
        }
    }

    /**
     * L�dt Br�cken.
     *
     * @param bridges Liste mit zu ladenen Br�cken
     */
    private void setBridges(List<IBridge> bridges) {
        gridController.setLines(bridges);
        List<BridgeLine> lines = gridController.getLines();
        for (BridgeLine line : lines)
            add(line.getLine(), line.getStartX(), line.getStartY());
        updateIsles();
    }

    void zoomInOut(int width, int height, boolean showMissingBridges) {
        //TODO: zoomInOut BackGroundImage, too
        getRowConstraints().clear();
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE))
                .forEach(row -> getRowConstraints().add(row));
        getColumnConstraints().clear();
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE))
                .forEachOrdered(column -> getColumnConstraints().add(column));

        getChildren().clear();
        gridController.clear();
        List<IIsle> isles = new ArrayList<>(controller.getIsles());
        setIsles(isles);
        setShowMissingBridges(showMissingBridges);
        List<IBridge> bridges = new ArrayList<>(controller.getBridges());
        setBridges(bridges);
    }

    /**
     * Legt fest, ob die Anzahl der Br�cken insgesamt oder nur der fehlenden Br�cken
     * angezeigt werden soll.
     *
     * @param showMissingBridges falls true, werden die fehlenden Br�cken angezeigt
     */
    void setShowMissingBridges(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        for (IslePane isle : gridController.getPanes()) {
            int bridges;
            if (showMissingBridges) bridges = controller.getMissingBridges(isle.getPos());
            else bridges = controller.getBridges(isle.getPos());
            isle.setText(Integer.toString(bridges));
        }
    }

    /**
     * Berechnet eine neue Br�cke, die sicher hinzugef�gt werden kann. M�glich,
     * sofern der aktuelle Spiel-Status keinen Fehler enth�lt, nicht unl�sbar oder
     * bereits gel�st ist.
     */
    void getNextBridge() {
        boolean solved = checker.solved();
        if (checker.error() || checker.unsolvable() || solved) {
            if (!solved) setAlert("Next bridge", "No bridge found.");
            return;
        }
        IBridge next = solver.getNextBridge();
        if (next == null && !checker.solved()) setAlert("Next bridge", "No bridge found.");
        else addBridgeAuto(next);
    }

    /**
     * Erg�nzt zeitverz�gert neue Br�cken, die sicher hinzugef�gt werden k�nnen.
     */
    private void getNextBridgeAuto() {
        boolean solved = checker.solved();
        if (checker.error() || checker.unsolvable() || solved) {
            autoSolver.stop();
            if (!solved) setAlert("Solve auto", "No bridge found.");
            return;
        }
        IBridge next = autoSolver.getNextBridge();
        if (next == null) {
            autoSolver.stop();
            if (!checker.solved()) setAlert("Solve auto", "No bridge found.");
        } else {
            addBridgeAuto(next);
        }
    }

    private void setAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }


    /**
     * Startet Simulation zur Berechnung neuer Br�cken, die sicher hinzugef�gt
     * werden k�nnen.
     */
    void startAutoSolve() {
        if (!autoSolver.isRunning()) autoSolver.start();
    }

    /**
     * Stoppt Simulation zur Berechnung neuer Br�cken, die sicher hinzugef�gt werden
     * k�nnen.
     */
    void stopAutoSolve() {
        if (autoSolver.isRunning()) autoSolver.stop();
    }

    /**
     * Terminiert Simulation zur Berechnung neuer Br�cken, die sicher hinzugef�gt
     * werden k�nnen.
     */
    void shutdownAutoSolve() {
        autoSolver.shutdown();
    }

    /**
     * Gibt zur�ck, ob {@link net.joedoe.logics.AutoSolver} aktuell l�uft.
     *
     * @return true, falls {@link net.joedoe.logics.AutoSolver} aktuell l�uft
     */
    boolean autoSolverIsRunning() {
        return autoSolver.isRunning();
    }

    /**
     * Setzt den Spielstatus zur�ck. Br�cken werden gel�scht und Inseln in ihren
     * Ausgangszustand zur�ck versetzt.
     */
    void reset() {
        controller.reset();
        gridController.getLines().forEach(line -> getChildren().remove(line.getLine()));
        gridController.reset();
        gridController.getPanes().forEach(isle -> {
            isle.getStyleClass().removeAll("isle-alert", "isle-solved");
            isle.setText(Integer.toString(controller.getBridges(isle.getPos())));
        });
        autoSolver.stop();
        statusListener.handle(new StatusEvent(Status.NOT_SOLVED));
    }

    /**
     * Leitet die Nutzer-Anfrage, den aktuellen Spielstand zu speichern, an
     * {@link net.joedoe.logics.GridController} weiter.
     */
    void savePuzzle() {
        controller.saveGame();
    }

    public List<IIsle> getIsles() {
        return new ArrayList<>(controller.getIsles());
    }

    public List<IBridge> getBridges() {
        return new ArrayList<>(controller.getBridges());
    }

    void setPointListener(EventHandler<PointEvent> listener) {
        pointListener = listener;
    }
}