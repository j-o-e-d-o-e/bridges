package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Converter;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Beinhaltet die Logik, damit der Spieler bzw. das Programm Br�cken hinzuf�gen
 * bzw. entfernen kann.
 */
public class BridgeController {
    private BridgeDetector detector;
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(BridgeController.class.getName());

    public BridgeController() {
        detector = new BridgeDetector(bridges);
        LOGGER.setLevel(Level.OFF);
    }

    /**
     * F�gt eine neue Br�cke hinzu, falls m�glich. Aufgerufen von
     * {@link net.joedoe.views.Grid}.
     *
     * @param pos       Koordinate einer Insel
     * @param direction Richtung des Klicksektors
     * @return neue Br�cke
     */
    public IBridge addBridge(Coordinate pos, Direction direction) {
        // sucht erste Insel anhand ihrer Insel-Koordinate
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        // sucht zweite Insel ausgehend von erster Insel und Richtung
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        // pr�ft, ob neue Br�cke mit existierenden Br�cken kollidiert
        if (detector.collides(startIsle, endIsle)) return null;
        return addBridge(startIsle, endIsle);
    }

    /**
     * F�gt eine neue Br�cke hinzu. Falls noch keine Br�cke existiert, wird eine
     * neue Br�cke erstellt. Falls eine einfache Br�cke bereits exisitert, wird
     * diese in eine doppelte Br�cke umgewandelt. Falls eine doppelte Br�ck bereits
     * existiert, wird die Methode beendet.
     *
     * @param startIsle erste Insel, die mit der Br�cke verbunden werden soll
     * @param endIsle   zweite Insel, die mit der Br�cke verbunden werden soll
     * @return neue Br�cke
     */
    Bridge addBridge(Isle startIsle, Isle endIsle) {
        // sucht existierende Br�cke, die die beiden Inseln miteinander verbindet
        Bridge bridge = getBridge(startIsle.getPos(), endIsle.getPos());
        // neue Br�cke
        if (bridge == null) {
            bridge = new Bridge(startIsle.getPos(), endIsle.getPos(), false);
            startIsle.addNeighbour(endIsle);
            endIsle.addNeighbour(startIsle);
            bridges.add(bridge);
        } // doppelte Br�cke
        else if (bridge.isDoubleBridge()) {
            return null;
        } // einfache Br�cke in doppelte Br�cke umwandeln
        else {
            bridge.setDoubleBridge(true);
        }
        startIsle.addBridge(false);
        endIsle.addBridge(false);
        bridges.forEach(b -> LOGGER.info(b.toString()));
        return bridge;
    }

    /**
     * Entfernt eine existierende Br�cke. Falls keine entsprechende Br�cke
     * existiert, wird Methode beendet. Falls es sich um eine doppelte Br�cke
     * handelt, wird diese in eine einfache Br�cke umgewandelt. Falls es sich um
     * eine einfache Br�cke handelt, wird diese entfernt.
     *
     * @param pos       Koordinate einer Insel
     * @param direction Richtung des Klicksektors
     * @return zu entfernende Br�cke
     */
    public IBridge removeBridge(Coordinate pos, Direction direction) {
        // sucht erste Insel anhand der Insel-Koordinate
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        // sucht zweite Insel ausgehend von erster Insel und Richtung
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = getBridge(startIsle.getPos(), endIsle.getPos());
        // keine Br�cke gefunden
        if (bridge == null) {
            return null;
        } // doppelte wird in einfache Br�cke umgewandelt
        else if (bridge.isDoubleBridge()) {
            bridge.setDoubleBridge(false);
        } // einfache Br�cke wird entfernt
        else {
            bridges.remove(bridge);
            startIsle.removeNeighbour(endIsle);
            endIsle.removeNeighbour(startIsle);
        }
        startIsle.removeBridge(false);
        endIsle.removeBridge(false);
        return bridge;
    }

    public void removeBridge(IBridge bridge) {
        Isle startIsle = getIsle(bridge.getStart());
        Isle endIsle = getIsle(bridge.getEnd());
        startIsle.removeBridge(true);
        startIsle.removeNeighbour(endIsle);
        endIsle.removeBridge(true);
        endIsle.removeNeighbour(startIsle);
        Bridge bridge1 = (Bridge) bridge;
        this.bridges.remove(bridge1);
    }

    /**
     * Gibt die n�chstgelegene Insel zur�ck.
     *
     * @param startIsle Insel, f�r die die n�chstgelegene Insel gesucht wird
     * @param direction Richtung, in der nach der n�chstgelegenen Insel gesucht wird
     * @return n�chstgelegene Insel
     */
    public Isle getEndIsle(Isle startIsle, Direction direction) {
        LOGGER.info(startIsle.toString() + " " + direction);
        if (direction == Direction.UP) {
            return isles.stream().sorted(Collections.reverseOrder())
                    .filter(i -> i.getX() == startIsle.getX() && i.getY() < startIsle.getY()).findFirst().orElse(null);
        } else if (direction == Direction.LEFT) {
            return isles.stream().sorted(Collections.reverseOrder())
                    .filter(i -> i.getY() == startIsle.getY() && i.getX() < startIsle.getX()).findFirst().orElse(null);
        } else if (direction == Direction.DOWN) {
            return isles.stream().filter(i -> i.getX() == startIsle.getX() && i.getY() > startIsle.getY()).findFirst()
                    .orElse(null);
        } else {
            return isles.stream().filter(i -> i.getY() == startIsle.getY() && i.getX() > startIsle.getX()).findFirst()
                    .orElse(null);
        }
    }

    /**
     * Identifiziert Insel und gibt diese zur�ck.
     *
     * @param pos Koordinate, wo die gesuchte Inseln positioniert ist
     * @return Insel, die an der Koordinate 'pos' positioniert ist
     */
    public Isle getIsle(Coordinate pos) {
        return isles.stream().filter(i -> i.getPos() == pos).findFirst().orElse(null);
    }

    /**
     * Identifiziert Br�cke und gibt diese zur�ck.
     *
     * @param startIsle erste Insel, die mit der Br�cke verbunden ist
     * @param endIsle   zweite Insel, die mit der Br�cke verbunden ist
     * @return Br�cke, die 'startIsle' und 'endIsle' miteinander verbindet
     */
    Bridge getBridge(Coordinate start, Coordinate end) {
        return bridges.stream().filter(b -> b.contains(start, end)).findFirst().orElse(null);
    }

    public Bridge getBridge(Coordinate pos, Direction direction) {
        Isle startIsle = getIsle(pos);
        Isle endIsle = getEndIsle(startIsle, direction);
        return getBridge(startIsle.getPos(), endIsle.getPos());
    }

    /**
     * L�dt Daten �ber Inseln - entweder aus einem geladenen oder einem generierten
     * Spiel.
     *
     * @param isles Liste mit zu ladenden Inseln
     */
    public void setIsles(List<IIsle> isles) {
        for (IIsle isle : isles)
            this.isles.add((Isle) isle);
    }

    /**
     * Gibt alle Inseln zur�ck.
     *
     * @return Liste aller Inseln
     */
    public List<Isle> getIsles() {
        return isles;
    }

    /**
     * L�dt Daten �ber Br�cken.
     *
     * @param bridges Liste mit zu ladenden Br�cken
     */
    public void setBridges(List<IBridge> bridges) {
        for (IBridge bridge : bridges) {
            boolean doubleBridge = bridge.isDoubleBridge();
            // sucht die beiden Inseln, die die Br�cke miteinander verbindet
            Isle startIsle = getIsle(bridge.getStart());
            Isle endIsle = getIsle(bridge.getEnd());
            if (startIsle == null || endIsle == null) continue;
            // neue Br�cke
            startIsle.addBridge(doubleBridge);
            startIsle.addNeighbour(endIsle);
            endIsle.addBridge(doubleBridge);
            endIsle.addNeighbour(startIsle);
            this.bridges.add((Bridge) bridge);
        }
    }

    /**
     * Gibt alle Br�cken zur�ck.
     *
     * @return Liste aller aktuellen Br�cken
     */
    public List<Bridge> getBridges() {
        return bridges;
    }

    /**
     * Gibt die Anzahl der geforderten Br�cken einer Insel zur�ck.
     *
     * @param pos Koordinate, anhand der die Insel identifiziert wird
     * @return Anzahl der geforderten Br�cken einer Insel
     */
    public int getBridges(Coordinate pos) {
        Isle isle = getIsle(pos);
        if (isle == null) return 0;
        return isle.getBridges();
    }

    /**
     * Gibt die Anzahl der fehlenden Br�cken einer Insel zur�ck.
     *
     * @param pos Koordinate, anhand der die Insel identifiziert wird
     * @return Anzahl der fehlenden Br�cken einer Insel
     */
    public int getMissingBridges(Coordinate pos) {
        Isle isle = getIsle(pos);
        if (isle == null) return 0;
        return isle.getMissingBridges();
    }

    /**
     * Gibt Anzahl aller Inseln zur�ck.
     *
     * @return Anzahl aller Inseln
     */
    public int getIslesSize() {
        return isles.size();
    }

    /**
     * Setzt den Anfangszustand wieder her. Alle Br�cken werden gel�scht und die
     * Inseln in ihren Anfangszustand zur�ckgesetzt.
     */
    public void reset() {
        bridges.clear();
        isles.forEach(Isle::clear);
    }

    /**
     * Speichert die aktuellen Daten.
     */
    public void saveGame() {
        Converter.convertIslesToData(new ArrayList<>(isles));
        Converter.convertBridgesToData(this);
    }
}