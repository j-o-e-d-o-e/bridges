package net.joedoe.entities;

import net.joedoe.utils.Coordinate;
import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert eine Insel im Modell.
 *
 */
public class Isle implements IIsle, Comparable<Isle> {
    private final Coordinate pos;
    private int bridges;
    private int missingBridges;
    private Set<Isle> neighbours = new HashSet<>();

    /**
     * Neuer Insel werden die Koordinate, an der sie platziert ist, und die Anzahl
     * an zu verbindenden Brücken übergeben.
     * 
     * @param pos
     *            Koordinate, wo die Insel platziert wird
     * @param bridges
     *            Anzahl an Brücken, die zu bauen sind
     */
    public Isle(Coordinate pos, int bridges) {
        this.pos = pos;
        this.bridges = bridges;
        missingBridges = bridges;
    }

    /**
     * Vermindert die Anzahl der fehlenden Brücken um eins bzw. zwei.
     * 
     * @param doubleBridge
     *            falls true, um zwei vermindern
     */
    public void addBridge(boolean doubleBridge) {
        if (doubleBridge) missingBridges -= 2;
        else missingBridges--;
    }

    /**
     * Erhöht die Anzahl der fehlenden Brücken um eins.
     */
    public void removeBridge() {
        missingBridges++;
    }

    /**
     * Erhöht die Anzahl der zu bauenden Brücken um eins (einfache Brücke) oder zwei
     * (doppelte Brücke). Während der Generierung eines neuen Spiels in
     * {@link net.joedoe.logics.Generator} verwendet.
     * 
     * @param doubleBridge
     *            falls true, dann um zwei erhöhen
     */
    public void increaseBridges(boolean doubleBridge) {
        if (doubleBridge) bridges += 2;
        else bridges++;
        missingBridges = bridges;
    }

    /**
     * Fügt eine Nachbar-Inseln hinzu, die mit dieser Insel über eine Brücke
     * verbunden ist.
     * 
     * @param neighbour
     *            benachbarte Insel, die mit dieser Insel verbunden ist
     */
    public void addNeighbour(Isle neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * Entfernt eine Nachbar-Insel, die mit dieser Insel über eine Brücke verbunden
     * war.
     * 
     * @param neighbour
     *            benachbarte Insel, die entfernt werden soll
     */
    public void removeNeighbour(Isle neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * Gibt benachbarte Inseln zurück, die mit dieser Insel verbunden sind.
     */
    public Set<Isle> getNeighbours() {
        return neighbours;
    }

    /**
     * Gibt Anzahl von insgesamt zu bauenden Brücken zurück.
     * 
     * @return Anzahl an Brücken
     */
    public int getBridges() {
        return bridges;
    }

    /**
     * Gibt Anzahl an fehlenden Brücken zurück.
     * 
     * @return Anzahl an noch zu bauenden Brücken
     */
    public int getMissingBridges() {
        return missingBridges;
    }

    /**
     * Gibt y-Wert der Insel-Koordinate zurück.
     * 
     * @return y-Wert der Insel-Koordinate
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Gibt x-Wert der Insel-Koordinate zurück.
     * 
     * @return x-Wert der Insel-Koordinate
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Gibt Koordinate dieser Insel zurück.
     * 
     * @return Koordinate, an der diese Insel platziert ist
     */
    public Coordinate getPos() {
        return pos;
    }

    /**
     * Setzt die Anzahl der fehlenden Brücken auf den Ursprungswert zurück und
     * löscht alle Nachbarn.
     */
    public void clear() {
        missingBridges = bridges;
        neighbours.clear();
    }

    /**
     * Vergleicht Inseln anhand ihrer Koordinate: Kleiner ist eine Insel, wenn sie
     * näher am Koordinaten-Ursprung liegt.
     */
    @Override
    public int compareTo(Isle other) {
        return pos.compareTo(other.pos);
    }

    @Override
    public String toString() {
        return "Isle{" + "pos=" + pos + "missingBridges=" + missingBridges + '}';
    }
}