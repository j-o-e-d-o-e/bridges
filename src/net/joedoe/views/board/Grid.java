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
 * Das Raster, auf dem Inseln und Brücken platziert werden.
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
     * {@link Board} benachrichtigt, übergeben sowie die Breite
     * und Höhe des Spielfelds und die Listen mit den zu platzierenden Inseln und
     * Brücken.
     *
     * @param statusListener Listener, der die Status-Zeile benachrichtigt
     * @param width          Breite des Spielfelds
     * @param height         Höhe des Spielfelds
     * @param isles          Liste mit den zu platzierenden Inseln
     * @param bridges        Liste mit den zu platzierenden Brücken
     */
    Grid(EventHandler<StatusEvent> statusListener, int width, int height, List<IIsle> isles, List<IBridge> bridges) {
        setId("grid");
        this.width = width;
        this.height = height;
        // Größe des Rasters festlegen
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
     * Fügt eine neue Brücken-Linie hinzu, falls möglich. Aufgerufen von
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
     * Fügt eine neue Brücken-Linie hinzu, falls möglich. Aufgerufen von Programm.
     *
     * @param bridge hinzuzufügende Brücke (Modell)
     */
    private void addBridgeAuto(IBridge bridge) {
        pointListener.handle(new PointEvent(-10));
        addBridge(bridge);
    }

    /**
     * Fügt eine neue Brücken-Linie hinzu, falls möglich. Aufgerufen entweder von Nutzer oder von Programm.
     *
     * @param bridge hinzuzufügende Brücke (Modell)
     */
    private void addBridge(IBridge bridge) {
        BridgeLine line = gridController.addLine(bridge);
        if (line == null) return;
        add(line.getLine(), line.getStartX(), line.getStartY());
        updateIsles();
        checkStatus();
    }

    /**
     * Entfernt Brücken-Linie, falls möglich. Aufgerufen von
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
     * Entfernt Brücken-Linie, falls möglich. Aufgerufen entweder von Nutzer.
     *
     * @param bridge zu entferndende Brücke (Modell)
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
     * Brücken fest und aktualisiert die Angabe an (fehlenden) Brücken.
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
            // Brücken-Anzahl aktualisieren
            if (!showMissingBridges) bridges = controller.getBridges(isle.getPos());
            isle.setText(Integer.toString(bridges));
        }
    }

    /**
     * Checkt den Spielstatus und gibt Status an Status-Zeile in
     * {@link Board} zurück.
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
     * Lädt Inseln.
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
     * Lädt Brücken.
     *
     * @param bridges Liste mit zu ladenen Brücken
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
     * Legt fest, ob die Anzahl der Brücken insgesamt oder nur der fehlenden Brücken
     * angezeigt werden soll.
     *
     * @param showMissingBridges falls true, werden die fehlenden Brücken angezeigt
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
     * Berechnet eine neue Brücke, die sicher hinzugefügt werden kann. Möglich,
     * sofern der aktuelle Spiel-Status keinen Fehler enthält, nicht unlösbar oder
     * bereits gelöst ist.
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
     * Ergänzt zeitverzögert neue Brücken, die sicher hinzugefügt werden können.
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
        alert.setTitle("Nächste Brücke");
        alert.setHeaderText("Keine Brücke gefunden, die sicher ergänzt werden kann.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }


    /**
     * Startet Simulation zur Berechnung neuer Brücken, die sicher hinzugefügt
     * werden können.
     */
    void startAutoSolve() {
        if (!autoSolver.isRunning()) autoSolver.start();
    }

    /**
     * Stoppt Simulation zur Berechnung neuer Brücken, die sicher hinzugefügt werden
     * können.
     */
    void stopAutoSolve() {
        if (autoSolver.isRunning()) autoSolver.stop();
    }

    /**
     * Terminiert Simulation zur Berechnung neuer Brücken, die sicher hinzugefügt
     * werden können.
     */
    void shutdownAutoSolve() {
        autoSolver.shutdown();
    }

    /**
     * Gibt zurück, ob {@link net.joedoe.logics.AutoSolver} aktuell läuft.
     *
     * @return true, falls {@link net.joedoe.logics.AutoSolver} aktuell läuft
     */
    boolean autoSolverIsRunning() {
        return autoSolver.isRunning();
    }

    /**
     * Setzt den Spielstatus zurück. Brücken werden gelöscht und Inseln in ihren
     * Ausgangszustand zurück versetzt.
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