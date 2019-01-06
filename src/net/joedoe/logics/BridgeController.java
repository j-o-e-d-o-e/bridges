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
 * Beinhaltet die Logik, damit der Spieler bzw. das Programm Brücken hinzufügen
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
     * Fügt eine neue Brücke hinzu, falls möglich. Aufgerufen von
     * {@link net.joedoe.views.Grid}.
     *
     * @param pos       Koordinate einer Insel
     * @param direction Richtung des Klicksektors
     * @return neue Brücke
     */
    public IBridge addBridge(Coordinate pos, Direction direction) {
        // sucht erste Insel anhand ihrer Insel-Koordinate
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        // sucht zweite Insel ausgehend von erster Insel und Richtung
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        // prüft, ob neue Brücke mit existierenden Brücken kollidiert
        if (detector.collides(startIsle, endIsle)) return null;
        return addBridge(startIsle, endIsle);
    }

    /**
     * Fügt eine neue Brücke hinzu. Falls noch keine Brücke existiert, wird eine
     * neue Brücke erstellt. Falls eine einfache Brücke bereits exisitert, wird
     * diese in eine doppelte Brücke umgewandelt. Falls eine doppelte Brück bereits
     * existiert, wird die Methode beendet.
     *
     * @param startIsle erste Insel, die mit der Brücke verbunden werden soll
     * @param endIsle   zweite Insel, die mit der Brücke verbunden werden soll
     * @return neue Brücke
     */
    Bridge addBridge(Isle startIsle, Isle endIsle) {
        // sucht existierende Brücke, die die beiden Inseln miteinander verbindet
        Bridge bridge = getBridge(startIsle.getPos(), endIsle.getPos());
        // neue Brücke
        if (bridge == null) {
            bridge = new Bridge(startIsle.getPos(), endIsle.getPos(), false);
            startIsle.addNeighbour(endIsle);
            endIsle.addNeighbour(startIsle);
            bridges.add(bridge);
        } // doppelte Brücke
        else if (bridge.isDoubleBridge()) {
            return null;
        } // einfache Brücke in doppelte Brücke umwandeln
        else {
            bridge.setDoubleBridge(true);
        }
        startIsle.addBridge(false);
        endIsle.addBridge(false);
        bridges.forEach(b -> LOGGER.info(b.toString()));
        return bridge;
    }

    /**
     * Entfernt eine existierende Brücke. Falls keine entsprechende Brücke
     * existiert, wird Methode beendet. Falls es sich um eine doppelte Brücke
     * handelt, wird diese in eine einfache Brücke umgewandelt. Falls es sich um
     * eine einfache Brücke handelt, wird diese entfernt.
     *
     * @param pos       Koordinate einer Insel
     * @param direction Richtung des Klicksektors
     * @return zu entfernende Brücke
     */
    public IBridge removeBridge(Coordinate pos, Direction direction) {
        // sucht erste Insel anhand der Insel-Koordinate
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        // sucht zweite Insel ausgehend von erster Insel und Richtung
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = getBridge(startIsle.getPos(), endIsle.getPos());
        // keine Brücke gefunden
        if (bridge == null) {
            return null;
        } // doppelte wird in einfache Brücke umgewandelt
        else if (bridge.isDoubleBridge()) {
            bridge.setDoubleBridge(false);
        } // einfache Brücke wird entfernt
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
     * Gibt die nächstgelegene Insel zurück.
     *
     * @param startIsle Insel, für die die nächstgelegene Insel gesucht wird
     * @param direction Richtung, in der nach der nächstgelegenen Insel gesucht wird
     * @return nächstgelegene Insel
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
     * Identifiziert Insel und gibt diese zurück.
     *
     * @param pos Koordinate, wo die gesuchte Inseln positioniert ist
     * @return Insel, die an der Koordinate 'pos' positioniert ist
     */
    public Isle getIsle(Coordinate pos) {
        return isles.stream().filter(i -> i.getPos() == pos).findFirst().orElse(null);
    }

    /**
     * Identifiziert Brücke und gibt diese zurück.
     *
     * @param startIsle erste Insel, die mit der Brücke verbunden ist
     * @param endIsle   zweite Insel, die mit der Brücke verbunden ist
     * @return Brücke, die 'startIsle' und 'endIsle' miteinander verbindet
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
     * Lädt Daten über Inseln - entweder aus einem geladenen oder einem generierten
     * Spiel.
     *
     * @param isles Liste mit zu ladenden Inseln
     */
    public void setIsles(List<IIsle> isles) {
        for (IIsle isle : isles)
            this.isles.add((Isle) isle);
    }

    /**
     * Gibt alle Inseln zurück.
     *
     * @return Liste aller Inseln
     */
    public List<Isle> getIsles() {
        return isles;
    }

    /**
     * Lädt Daten über Brücken.
     *
     * @param bridges Liste mit zu ladenden Brücken
     */
    public void setBridges(List<IBridge> bridges) {
        for (IBridge bridge : bridges) {
            boolean doubleBridge = bridge.isDoubleBridge();
            // sucht die beiden Inseln, die die Brücke miteinander verbindet
            Isle startIsle = getIsle(bridge.getStart());
            Isle endIsle = getIsle(bridge.getEnd());
            if (startIsle == null || endIsle == null) continue;
            // neue Brücke
            startIsle.addBridge(doubleBridge);
            startIsle.addNeighbour(endIsle);
            endIsle.addBridge(doubleBridge);
            endIsle.addNeighbour(startIsle);
            this.bridges.add((Bridge) bridge);
        }
    }

    /**
     * Gibt alle Brücken zurück.
     *
     * @return Liste aller aktuellen Brücken
     */
    public List<Bridge> getBridges() {
        return bridges;
    }

    /**
     * Gibt die Anzahl der geforderten Brücken einer Insel zurück.
     *
     * @param pos Koordinate, anhand der die Insel identifiziert wird
     * @return Anzahl der geforderten Brücken einer Insel
     */
    public int getBridges(Coordinate pos) {
        Isle isle = getIsle(pos);
        if (isle == null) return 0;
        return isle.getBridges();
    }

    /**
     * Gibt die Anzahl der fehlenden Brücken einer Insel zurück.
     *
     * @param pos Koordinate, anhand der die Insel identifiziert wird
     * @return Anzahl der fehlenden Brücken einer Insel
     */
    public int getMissingBridges(Coordinate pos) {
        Isle isle = getIsle(pos);
        if (isle == null) return 0;
        return isle.getMissingBridges();
    }

    /**
     * Gibt Anzahl aller Inseln zurück.
     *
     * @return Anzahl aller Inseln
     */
    public int getIslesSize() {
        return isles.size();
    }

    /**
     * Setzt den Anfangszustand wieder her. Alle Brücken werden gelöscht und die
     * Inseln in ihren Anfangszustand zurückgesetzt.
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