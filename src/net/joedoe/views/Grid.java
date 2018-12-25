package net.joedoe.views;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.logics.AutoSolver;
import net.joedoe.logics.BridgeController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Direction;
import net.joedoe.viewmodel.BridgeLine;
import net.joedoe.viewmodel.GridController;
import net.joedoe.viewmodel.IslePane;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.*;

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
    private boolean showMissingBridges;

    /**
     * Grid wird der StatusListener, der die Status-Zeile im
     * {@link net.joedoe.views.MainFrame} benachrichtigt, �bergeben sowie die Breite
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
        // setGridLinesVisible(true);
        setAlignment(Pos.CENTER);
        setId("grid");
        setBorder(new Border(
                new BorderStroke(STD_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        // Gr��e des Rasters festlegen
        IntStream.range(0, height).mapToObj(i -> new RowConstraints(ONE_TILE))
                .forEach(row -> getRowConstraints().add(row));
        IntStream.range(0, width).mapToObj(i -> new ColumnConstraints(ONE_TILE))
                .forEachOrdered(column -> getColumnConstraints().add(column));

        gridController = new GridController();
        controller = new BridgeController();
        checker = new StatusChecker(controller);
        solver = new Solver(controller);
        autoSolver = new AutoSolver(solver);
        autoSolver.setListener(() -> Platform.runLater(this::getNextBridgeAuto));
        this.statusListener = statusListener;
        setIsles(isles);
        if (bridges != null) setBridges(bridges);
        checkStatus();
    }

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Aufgerufen von
     * {@link net.joedoe.views.IsleListener}.
     *
     * @param isle      Insel (View), die vom Nutzer angeklickt wurde
     * @param direction Richtung, in die der Nutzer mittels Klick-Sektor geklickt hat
     */
    void addBridge(IslePane isle, Direction direction) {
        IBridge bridge = controller.addBridge(isle.getPos(), direction);
        if (bridge == null) return;
        addBridge(bridge);
    }

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich.
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
     * {@link net.joedoe.views.IsleListener}.
     *
     * @param isle      Insel (View), die vom Nutzer angeklickt wurde
     * @param direction Richtung, in die der Nutzer mittels Klick-Sektor geklickt hat
     */
    void removeBridge(IslePane isle, Direction direction) {
        IBridge bridge = controller.removeBridge(isle.getPos(), direction);
        if (bridge == null) return;
        removeBridge(bridge);
    }

    /**
     * Entfernt Br�cken-Linie, falls m�glich.
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
            int bridges = controller.getMissingBridges(isle.getPos());
            if (bridges == 0) isle.setColor(ISLES_SOLVED_COLOR);
            else if (bridges < 0) isle.setColor(ISLES_ALERT_COLOR);
            else isle.setColor(ISLES_STD_COLOR);
            // Br�cken-Anzahl aktualisieren
            if (!showMissingBridges) bridges = controller.getBridges(isle.getPos());
            isle.setText(Integer.toString(bridges));
        }
    }

    /**
     * Checkt den Spielstatus und gibt Status an Status-Zeile in
     * {@link net.joedoe.views.MainFrame} zur�ck.
     */
    private void checkStatus() {
        if (checker.error()) {
            statusListener.handle(new StatusEvent("Enth�lt Fehler."));
        } else if (checker.unsolvable()) {
            statusListener.handle(new StatusEvent("Nicht mehr l�sbar."));
        } else if (checker.solved()) {
            gridController.updateLines();
            statusListener.handle(new StatusEvent("Gel�st!"));
        } else {
            statusListener.handle(new StatusEvent("Noch nicht gel�st."));
        }
    }

    /**
     * L�dt Inseln.
     *
     * @param isles Liste mit zu ladenen Inseln
     */
    private void setIsles(List<IIsle> isles) {
        controller.setIsles(isles);
        gridController.setPanes(isles, new IsleListener(this));
        for (IslePane isle : gridController.getPanes())
            add(isle, isle.getX(), isle.getY());
    }

    /**
     * L�dt Br�cken.
     *
     * @param bridges Liste mit zu ladenen Br�cken
     */
    private void setBridges(List<IBridge> bridges) {
        controller.setBridges(bridges);
        gridController.setLines(bridges);
        for (BridgeLine line : gridController.getLines())
            add(line.getLine(), line.getStartX(), line.getStartY());
        updateIsles();
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
        if (checker.error() || checker.unsolvable() || checker.solved()) {
            if (!checker.solved()) setAlert();
            return;
        }
        IBridge next = solver.getNextBridge();
        if (next == null && !checker.solved()) setAlert();
        else addBridge(next);
    }

    /**
     * Erg�nzt zeitverz�gert neue Br�cken, die sicher hinzugef�gt werden k�nnen.
     */
    private void getNextBridgeAuto() {
        if (checker.error() || checker.unsolvable() || checker.solved()) {
            autoSolver.stop();
            if (!checker.solved()) setAlert();
            return;
        }
        IBridge next = autoSolver.getNextBridge();
        if (next == null) {
            autoSolver.stop();
            if (!checker.solved()) setAlert();
        } else {
            addBridge(next);
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
            isle.setColor(ISLES_STD_COLOR);
            isle.setText(Integer.toString(controller.getBridges(isle.getPos())));
        });
        autoSolver.stop();
        statusListener.handle(new StatusEvent("Noch nicht gel�st."));
    }

    /**
     * Leitet die Nutzer-Anfrage, den aktuellen Spielstand zu speichern, an
     * {@link net.joedoe.logics.GridController} weiter.
     */
    void saveGame() {
        controller.saveGame();
    }
}