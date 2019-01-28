package net.joedoe.views.board;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
    private int width, height;
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
        this.width = width;
        this.height = height;
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
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Aufgerufen von
     * {@link IsleListener}.
     *
     * @param isle      Insel (View), die vom Nutzer angeklickt wurde
     * @param direction Richtung, in die der Nutzer mittels Klick-Sektor geklickt hat
     */
    void addBridge(IslePane isle, Direction direction) {
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

    private void removeDoubleBridge(IBridge bridge) {
        controller.removeBridge(bridge);
        List<BridgeLine> lines = gridController.removeLines(bridge);
        lines.forEach(l -> getChildren().remove(l.getLine()));
        pointListener.handle(new PointEvent(-20));
        updateIsles();
        checkStatus();
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
     * Entfernt Br�cken-Linie, falls m�glich. Aufgerufen von
     * {@link IsleListener}.
     *
     * @param isle      Insel (View), die vom Nutzer angeklickt wurde
     * @param direction Richtung, in die der Nutzer mittels Klick-Sektor geklickt hat
     */
    void removeBridge(IslePane isle, Direction direction) {
        IBridge bridge = controller.removeBridge(isle.getPos(), direction);
        if (bridge == null) return;
        removeBridge(bridge);
        pointListener.handle(new PointEvent(-10));
    }

    /**
     * Entfernt Br�cken-Linie, falls m�glich. Aufgerufen entweder von Nutzer.
     *
     * @param bridge zu entferndende Br�cke (Modell)
     */
    private void removeBridge(IBridge bridge) {
        BridgeLine line = gridController.removeLine(bridge);
        if (line == null) return;
        getChildren().remove(line.getLine());
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
        gridController.setPanes(isles, new IsleListener(this));
        ClickListener listener = new ClickListener(this);
        for (IslePane isle : gridController.getPanes()) {
            add(isle, isle.getX(), isle.getY());
            addClickPane(listener, isle);
        }
    }

    private void addClickPane(ClickListener listener, IslePane isle) {
        if (isle.getX() - 1 >= 0) {
            int x = isle.getX() - 1;
            int y = isle.getY();
            ClickPane click = addClickPane(listener, x, y);
            click.setRight(isle);
        }
        if (isle.getX() + 1 < width) {
            int x = isle.getX() + 1;
            int y = isle.getY();
            ClickPane click = addClickPane(listener, x, y);
            click.setLeft(isle);
        }
        if (isle.getY() - 1 >= 0) {
            int x = isle.getX();
            int y = isle.getY() - 1;
            ClickPane click = addClickPane(listener, x, y);
            click.setDown(isle);
        }
        if (isle.getY() + 1 < height) {
            int x = isle.getX();
            int y = isle.getY() + 1;
            ClickPane click = addClickPane(listener, x, y);
            click.setUp(isle);
        }
    }

    private ClickPane addClickPane(ClickListener listener, int x, int y) {
        ClickPane click = (ClickPane) getNode(x, y);
        if (click == null) {
            click = new ClickPane();
            click.setOnMouseClicked(listener);
            add(click, x, y);
        }
        return click;
    }

    private Node getNode(int col, int row) {
        for (Node node : getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
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
//        setStyle("-fx-grid-lines-visible: false;");
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
//        setStyle("-fx-grid-lines-visible: true;");
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
            if (!solved) setAlert();
            return;
        }
        IBridge next = solver.getNextBridge();
        if (next == null && !checker.solved()) setAlert();
        else addBridgeAuto(next);
    }

    /**
     * Erg�nzt zeitverz�gert neue Br�cken, die sicher hinzugef�gt werden k�nnen.
     */
    private void getNextBridgeAuto() {
        boolean solved = checker.solved();
        if (checker.error() || checker.unsolvable() || solved) {
            autoSolver.stop();
            if (!solved) setAlert();
            return;
        }
        IBridge next = autoSolver.getNextBridge();
        if (next == null) {
            autoSolver.stop();
            if (!checker.solved()) setAlert();
        } else {
            addBridgeAuto(next);
        }
    }

    private void setAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("N�chste Br�cke");
        alert.setHeaderText("Keine Br�cke gefunden, die sicher erg�nzt werden kann.");
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