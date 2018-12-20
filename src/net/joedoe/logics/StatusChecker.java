package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Prüft den Spielstatus.
 */
public class StatusChecker {
    private BridgeController controller;
    private BridgeDetector detector;

    private final static Logger LOGGER = Logger.getLogger(StatusChecker.class.getName());

    /**
     * Wird {@link net.joedoe.logics.BridgeController} übergeben, um auf die
     * aktuellen Brücken zugreifen zu können.
     *
     * @param controller enthält Liste mit aktuellen Brücken
     */
    public StatusChecker(BridgeController controller) {
        this.controller = controller;
        detector = new BridgeDetector(controller.getBridges());
        LOGGER.setLevel(Level.OFF);
    }

    /**
     * Prüft, ob der aktuelle Spielstatus Fehler enthält. Spiel enthält einen
     * Fehler, falls eine Brücke mehr Brücken aufweist als gefordert.
     *
     * @return true, falls eine Insel mehr Brücken als gefordert hat
     */
    public boolean error() {
        List<Isle> isles = controller.getIsles();
        return isles.stream().anyMatch(isle -> isle.getMissingBridges() < 0);
    }

    /**
     * Prüft, ob das Spiel im aktuellen Zustand nicht mehr lösbar ist, d.h. falls
     * jede weitere Brücke garantiert zu einem Regelverstoß führt.
     *
     * @return true, falls unlösbar
     */
    public boolean unsolvable() {
        return controller.getIsles().stream().filter(i -> i.getMissingBridges() > 0).anyMatch(this::isolated);
    }

    /**
     * Prüft, ob von einer Insel keine Brücken mehr zu ihren Nachbarn gebaut werden
     * können.
     *
     * @param startIsle die betrachtete Insel
     * @return true, falls betrachtete Insel isoliert ist
     */
    private boolean isolated(Isle startIsle) {
        for (Direction direction : Direction.values()) {
            Isle endIsle = controller.getEndIsle(startIsle, direction);
            if (endIsle == null || endIsle.getMissingBridges() <= 0 || detector.collides(startIsle, endIsle)) continue;
            Bridge bridge = controller.getBridge(startIsle.getPos(), endIsle.getPos());
            if (bridge == null || !bridge.isDoubleBridge()) return false;
        }
        return true;
    }

    // Alternative unsolvable()
    // public boolean unsolvable() {
    // List<Isle> isles = controller.getIsles();
    // // Prüft, ob einzelne Insel oder Insel-Gruppe isoliert ist
    // return isles.stream().filter(i -> i.getMissingBridges() >
    // 0).anyMatch(this::isolated) || disconnected(isles);
    // }
    // private boolean isolated(Isle startIsle) {
    // // ermittelt die Brücken-Anzahl aller möglichen Nachbarn von 'startIsle'
    // int bridges = 0;
    // for (Direction direction : Direction.values()) {
    // Isle endIsle = controller.getEndIsle(startIsle, direction);
    // // ungültige Nachbar aussortieren
    // if (endIsle == null || endIsle.getMissingBridges() == 0 ||
    // detector.collides(startIsle, endIsle)) continue;
    // Bridge bridge = controller.getBridge(startIsle.getPos(), endIsle.getPos());
    // if (bridge == null) {
    // // Brücke exisitert nicht, möglicher Nachbar fehlt aber nur eine Brücke
    // if (endIsle.getMissingBridges() == 1) bridges++;
    // else bridges += 2;
    // } // Brücke existiert, ist aber keine doppelte
    // else if (!bridge.isDoubleBridge()) bridges++;
    // }
    // LOGGER.info(startIsle + " with " + bridges + " possible bridges");
    // // Prüft, ob fehlende Brücken mehr sind als hinzufügbare Brücken
    // return startIsle.getMissingBridges() > bridges;
    // }
    // private boolean disconnected(List<Isle> isles) {
    // // Liste aller Inseln ohne fehlende Brücken
    // List<Isle> islesZeroBridges = isles.stream().filter(i ->
    // i.getMissingBridges() == 0)
    // .collect(Collectors.toList());
    // // Speichert Inseln, die bereits überprüft wurden
    // Set<Isle> cached = new HashSet<>();
    // // Prüft, ob Insel-Gruppen isoliert sind
    // for (Isle isle : islesZeroBridges) {
    // if (cached.contains(isle)) continue;
    // Set<Isle> connectedIsles = new HashSet<>();
    // connected(isle, connectedIsles);
    // cached.addAll(connectedIsles);
    // boolean disconnected = connectedIsles.stream().allMatch(i ->
    // i.getMissingBridges() == 0)
    // && connectedIsles.size() < isles.size();
    // if (disconnected) return true;
    // }
    // return false;
    // }

    /**
     * Prüft, ob das Spiel im aktuellen Zustand gelöst ist. Spiel ist gelöst, falls
     * alle Inseln so viele Brücken wie gefordert haben und direkt oder indirekt
     * miteinander verbunden sind
     *
     * @return true, falls alle Inseln so viele Brücken wie gefordert haben und
     * direkt oder indirekt miteinander verbunden sind
     */
    public boolean solved() {
        List<Isle> isles = controller.getIsles();
        // Prüft, ob alle Inseln keine fehlenden Brücken mehr haben
        boolean noBridges = isles.stream().allMatch(i -> i.getMissingBridges() == 0);
        if (noBridges) {
            // Prüft, ob die Anzahl der verbundenen Inseln gleich der Gesamt-Anzahl ist
            Set<Isle> connectedIsles = new HashSet<>();
            connected(isles.get(0), connectedIsles);
            return connectedIsles.size() == isles.size();
        }
        return false;
    }

    /**
     * Prüft, ob alle Inseln indirekt miteinander verbunden sind. Iteriert rekursiv
     * über die Insel-Nachbarn und deren Nachbarn.
     *
     * @param isle      Start-Insel, deren Nachbarn gesucht werden
     * @param connected Liste von Inseln, mit denen die Start-Insel direkt oder indirekt
     *                  verbunden ist
     */
    private void connected(Isle isle, Set<Isle> connected) {
        for (Isle neighbour : isle.getNeighbours()) {
            if (connected.contains(neighbour)) continue;
            connected.add(neighbour);
            connected(neighbour, connected);
        }
    }
}
