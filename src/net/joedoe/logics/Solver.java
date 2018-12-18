package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Berechnet einen neuen Spielzug.
 */
public class Solver {
    private BridgeController controller;
    private BridgeDetector detector;

    private final static Logger LOGGER = Logger.getLogger(Solver.class.getName());

    /**
     * Wird {@link net.joedoe.logics.BridgeController} �bergeben, um auf die
     * aktuellen Br�cken zugreifen zu k�nnen.
     *
     * @param controller enth�lt Liste mit aktuellen Br�cken
     */
    public Solver(BridgeController controller) {
        this.controller = controller;
        detector = new BridgeDetector(controller.getBridges());
        LOGGER.setLevel(Level.OFF);
    }

    /**
     * Berechnet eine neue Br�cke, die sicher hinzugef�gt werden kann.
     *
     * @return neue Br�cke
     */
    public IBridge getNextBridge() {
        for (Isle startIsle : getStartIsles()) {
            int missingBridges = startIsle.getMissingBridges();
            // Inseln, die mit 'startIsle' verbunden werden k�nnen
            List<Isle> connectables = getConnectables(startIsle);
            int connectablesSize = connectables.size();
            if (connectablesSize == 0) continue;
            LOGGER.info(startIsle.toString() + " with " + connectablesSize + " connectables");
            // Insel, die durch eine neue Br�cke mit 'startIsle' verbunden werden k�nnte
            Isle endIsle = getEndIsle(startIsle, connectables);
            if (firstRule(missingBridges, connectablesSize)) {
                LOGGER.info("firstRule: " + missingBridges + "/" + connectablesSize);
                return addBridge(startIsle, endIsle);
            }
            if (secondRule(missingBridges, connectablesSize)) {
                LOGGER.info("secondRule: " + missingBridges + "/" + connectablesSize);
                return addBridge(startIsle, endIsle);
            }
            // Insel, zu denen maximal eine Br�cke gebaut werden kann
            int connectablesOneBridge = getConnectablesOneBridge(startIsle, connectables);
            if (connectablesOneBridge == 0) continue;
            LOGGER.info(startIsle.toString() + " with " + connectablesSize + " (" + connectablesOneBridge
                    + ") connectables (one bridge)");

            if (thirdRule(missingBridges, connectablesSize, connectablesOneBridge)) {
                LOGGER.info("thirdRule: " + missingBridges + "/" + connectablesSize + "/" + connectablesOneBridge);
                return addBridge(startIsle, endIsle);
            }
            if (fourthRule(missingBridges, connectablesSize, connectablesOneBridge)) {
                LOGGER.info("fourthRule: " + missingBridges + "/" + connectablesSize + "/" + connectablesOneBridge);
                return addBridge(startIsle, endIsle);
            }
            if (fifthRule(missingBridges, connectablesSize, connectablesOneBridge)) {
                LOGGER.info("fifthRule: " + missingBridges + "/" + connectablesSize + "/" + connectablesOneBridge);
                return addBridge(startIsle, endIsle);
            }
        }
        return null;
    }

    private boolean firstRule(int missingBridges, int connactablesSize) {
        return missingBridges % 2 == 0 && missingBridges == connactablesSize * 2;
    }

    private boolean secondRule(int missingBridges, int connectablesSize) {
        return missingBridges % 2 != 0 && missingBridges + 1 == connectablesSize * 2;
    }

    private boolean thirdRule(int missingBridges, int connectablesSize, int connectablesOneBridge) {
        return (missingBridges == 6 || missingBridges == 4 || missingBridges == 2)
                && missingBridges + 2 == connectablesSize * 2 && connectablesOneBridge >= 1;
    }

    private boolean fourthRule(int missingBridges, int connectablesSize, int connectablesOneBridge) {
        return (missingBridges == 5 || missingBridges == 3) && missingBridges + 3 == connectablesSize * 2
                && connectablesOneBridge >= 2;
    }

    private boolean fifthRule(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        return missingBridges == 4 && missingBridges + 4 == neighboursSize * 2 && neighboursOneBridge >= 3;
    }

    /**
     * Liste aller Inseln, denen mindestens eine Br�cke fehlt, absteigend geordnet
     * nach Anzahl ihrer fehlenden Br�cken.
     *
     * @return Liste von Inseln
     */
    public List<Isle> getStartIsles() {
        return controller.getIsles().stream().filter(isle -> isle.getMissingBridges() > 0)
                .sorted(Collections.reverseOrder(Comparator.comparing(Isle::getMissingBridges)))
                .collect(Collectors.toList());
    }

    /**
     * Gibt Liste von Nachbar-Inseln zur�ck, mit denen die 'startIsle' �ber eine
     * Br�cke verbunden werden k�nnte.
     *
     * @param startIsle Insel, f�r die m�gliche Nachbar-Inseln gesucht werden
     * @return Liste aller m�glichen Nachbar-Inseln
     */
    public List<Isle> getConnectables(Isle startIsle) {
        List<Isle> connectables = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Isle connectable = controller.getEndIsle(startIsle, direction);
            // Pr�ft, ob m�gliche Nachbar-Insel zul�ssig ist
            if (connectable != null && !invalidConnectable(startIsle, connectable)) connectables.add(connectable);
        }
        return connectables;
    }

    /**
     * Pr�ft, ob eine m�gliche Nachbar-Insel zul�ssig ist. Kriterien f�r
     * Unzul�ssigkeit: Br�ckenkollision, doppelte Br�cke bereits vorhanden, m�gliche
     * Nachbar-Insel hat keine fehlenden Br�cken mehr, die beiden Inseln d�rfen
     * aufgrund von Isolationsverbot nicht miteinander verbunden werden.
     *
     * @param startIsle   Insel, f�r die m�gliche Nachbar-Insel zum Br�ckenbau gesucht
     *                    werden
     * @param connectable m�gliche Nachbar-Insel, die mit 'startIsle' verbunden werden
     *                    k�nnte
     * @return true, falls die m�gliche Nachbar-Insel eines der oben genannten
     * Kriterien erf�llt
     */
    private boolean invalidConnectable(Isle startIsle, Isle connectable) {
        Bridge bridge = controller.getBridge(startIsle.getPos(), connectable.getPos());
        int islesSize = controller.getIslesSize();
        return (bridge == null && detector.collides(startIsle, connectable))
                || (bridge != null && bridge.isDoubleBridge()) || connectable.getMissingBridges() == 0
                || (startIsle.getBridges() == 1 && connectable.getBridges() == 1 && islesSize != 2)
                || (startIsle.getBridges() == 2 && connectable.getBridges() == 2 && islesSize != 2 && bridge != null);
    }

    /**
     * Gibt Anzahl an benachbarten Inseln von 'startIsle' zur�ck, zu denen nur noch
     * eine Br�cke gebaut werden kann.
     *
     * @param startIsle    Insel, von der eine Br�cke gebaut werden soll
     * @param connectables benachbarte Inseln von 'startIsle, zu denen eine Br�cke gebaut
     *                     werden k�nnte
     * @return Anzahl an Inseln, zu denen maximal eine Br�cke gebaut werden k�nnte
     */
    public int getConnectablesOneBridge(Isle startIsle, List<Isle> connectables) {
        return (int) connectables.stream()
                .filter(c -> c.getMissingBridges() == 1 || controller.getBridge(startIsle.getPos(), c.getPos()) != null)
                .count();
    }

    /**
     * Gibt die zweite Insel zur�ck, die �ber eine Br�cke mit 'startIsle' verbunden
     * werden kann
     *
     * @param startIsle    erste Insel einer neuen Br�cke
     * @param connectables Liste mit m�glichen Nachbar-Inseln, um eine neue Br�cke ausgehend
     *                     von 'startIsle' zu bauen
     * @return zweite Insel einer neuen Br�cke ausgehend von 'startIsle'
     */
    public Isle getEndIsle(Isle startIsle, List<Isle> connectables) {
        // w�hle Insel, der noch mehr als eine Br�cke fehlt und zu der noch keine Br�cke besteht
        Isle endIsle = connectables.stream()
                .filter(c -> c.getMissingBridges() > 1 && controller.getBridge(startIsle.getPos(), c.getPos()) == null)
                .findFirst().orElse(null);
        // ansonsten w�hle erste Insel in der Liste
        if (endIsle == null) return connectables.get(0);
        return endIsle;
    }

    private IBridge addBridge(Isle startIsle, Isle endIsle) {
        IBridge bridge = controller.addBridge(startIsle, endIsle);
        LOGGER.info("Bridge added: " + bridge.toString() + "\n");
        return bridge;
    }
}
