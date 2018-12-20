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
 * Pr�ft den Spielstatus.
 */
public class StatusChecker {
    private BridgeController controller;
    private BridgeDetector detector;

    private final static Logger LOGGER = Logger.getLogger(StatusChecker.class.getName());

    /**
     * Wird {@link net.joedoe.logics.BridgeController} �bergeben, um auf die
     * aktuellen Br�cken zugreifen zu k�nnen.
     *
     * @param controller enth�lt Liste mit aktuellen Br�cken
     */
    public StatusChecker(BridgeController controller) {
        this.controller = controller;
        detector = new BridgeDetector(controller.getBridges());
        LOGGER.setLevel(Level.OFF);
    }

    /**
     * Pr�ft, ob der aktuelle Spielstatus Fehler enth�lt. Spiel enth�lt einen
     * Fehler, falls eine Br�cke mehr Br�cken aufweist als gefordert.
     *
     * @return true, falls eine Insel mehr Br�cken als gefordert hat
     */
    public boolean error() {
        List<Isle> isles = controller.getIsles();
        return isles.stream().anyMatch(isle -> isle.getMissingBridges() < 0);
    }

    /**
     * Pr�ft, ob das Spiel im aktuellen Zustand nicht mehr l�sbar ist, d.h. falls
     * jede weitere Br�cke garantiert zu einem Regelversto� f�hrt.
     *
     * @return true, falls unl�sbar
     */
    public boolean unsolvable() {
        return controller.getIsles().stream().filter(i -> i.getMissingBridges() > 0).anyMatch(this::isolated);
    }

    /**
     * Pr�ft, ob von einer Insel keine Br�cken mehr zu ihren Nachbarn gebaut werden
     * k�nnen.
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
    // // Pr�ft, ob einzelne Insel oder Insel-Gruppe isoliert ist
    // return isles.stream().filter(i -> i.getMissingBridges() >
    // 0).anyMatch(this::isolated) || disconnected(isles);
    // }
    // private boolean isolated(Isle startIsle) {
    // // ermittelt die Br�cken-Anzahl aller m�glichen Nachbarn von 'startIsle'
    // int bridges = 0;
    // for (Direction direction : Direction.values()) {
    // Isle endIsle = controller.getEndIsle(startIsle, direction);
    // // ung�ltige Nachbar aussortieren
    // if (endIsle == null || endIsle.getMissingBridges() == 0 ||
    // detector.collides(startIsle, endIsle)) continue;
    // Bridge bridge = controller.getBridge(startIsle.getPos(), endIsle.getPos());
    // if (bridge == null) {
    // // Br�cke exisitert nicht, m�glicher Nachbar fehlt aber nur eine Br�cke
    // if (endIsle.getMissingBridges() == 1) bridges++;
    // else bridges += 2;
    // } // Br�cke existiert, ist aber keine doppelte
    // else if (!bridge.isDoubleBridge()) bridges++;
    // }
    // LOGGER.info(startIsle + " with " + bridges + " possible bridges");
    // // Pr�ft, ob fehlende Br�cken mehr sind als hinzuf�gbare Br�cken
    // return startIsle.getMissingBridges() > bridges;
    // }
    // private boolean disconnected(List<Isle> isles) {
    // // Liste aller Inseln ohne fehlende Br�cken
    // List<Isle> islesZeroBridges = isles.stream().filter(i ->
    // i.getMissingBridges() == 0)
    // .collect(Collectors.toList());
    // // Speichert Inseln, die bereits �berpr�ft wurden
    // Set<Isle> cached = new HashSet<>();
    // // Pr�ft, ob Insel-Gruppen isoliert sind
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
     * Pr�ft, ob das Spiel im aktuellen Zustand gel�st ist. Spiel ist gel�st, falls
     * alle Inseln so viele Br�cken wie gefordert haben und direkt oder indirekt
     * miteinander verbunden sind
     *
     * @return true, falls alle Inseln so viele Br�cken wie gefordert haben und
     * direkt oder indirekt miteinander verbunden sind
     */
    public boolean solved() {
        List<Isle> isles = controller.getIsles();
        // Pr�ft, ob alle Inseln keine fehlenden Br�cken mehr haben
        boolean noBridges = isles.stream().allMatch(i -> i.getMissingBridges() == 0);
        if (noBridges) {
            // Pr�ft, ob die Anzahl der verbundenen Inseln gleich der Gesamt-Anzahl ist
            Set<Isle> connectedIsles = new HashSet<>();
            connected(isles.get(0), connectedIsles);
            return connectedIsles.size() == isles.size();
        }
        return false;
    }

    /**
     * Pr�ft, ob alle Inseln indirekt miteinander verbunden sind. Iteriert rekursiv
     * �ber die Insel-Nachbarn und deren Nachbarn.
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
