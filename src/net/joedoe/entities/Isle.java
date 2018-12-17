package net.joedoe.entities;

import net.joedoe.utils.Coordinate;
import java.util.HashSet;
import java.util.Set;

/**
 * Repr�sentiert eine Insel im Modell.
 *
 */
public class Isle implements IIsle, Comparable<Isle> {
    private final Coordinate pos;
    private int bridges;
    private int missingBridges;
    private Set<Isle> neighbours = new HashSet<>();

    /**
     * Neuer Insel werden die Koordinate, an der sie platziert ist, und die Anzahl
     * an zu verbindenden Br�cken �bergeben.
     * 
     * @param pos
     *            Koordinate, wo die Insel platziert wird
     * @param bridges
     *            Anzahl an Br�cken, die zu bauen sind
     */
    public Isle(Coordinate pos, int bridges) {
        this.pos = pos;
        this.bridges = bridges;
        missingBridges = bridges;
    }

    /**
     * Vermindert die Anzahl der fehlenden Br�cken um eins bzw. zwei.
     * 
     * @param doubleBridge
     *            falls true, um zwei vermindern
     */
    public void addBridge(boolean doubleBridge) {
        if (doubleBridge) missingBridges -= 2;
        else missingBridges--;
    }

    /**
     * Erh�ht die Anzahl der fehlenden Br�cken um eins.
     */
    public void removeBridge() {
        missingBridges++;
    }

    /**
     * Erh�ht die Anzahl der zu bauenden Br�cken um eins (einfache Br�cke) oder zwei
     * (doppelte Br�cke). W�hrend der Generierung eines neuen Spiels in
     * {@link net.joedoe.logics.Generator} verwendet.
     * 
     * @param doubleBridge
     *            falls true, dann um zwei erh�hen
     */
    public void increaseBridges(boolean doubleBridge) {
        if (doubleBridge) bridges += 2;
        else bridges++;
        missingBridges = bridges;
    }

    /**
     * F�gt eine Nachbar-Inseln hinzu, die mit dieser Insel �ber eine Br�cke
     * verbunden ist.
     * 
     * @param neighbour
     *            benachbarte Insel, die mit dieser Insel verbunden ist
     */
    public void addNeighbour(Isle neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * Entfernt eine Nachbar-Insel, die mit dieser Insel �ber eine Br�cke verbunden
     * war.
     * 
     * @param neighbour
     *            benachbarte Insel, die entfernt werden soll
     */
    public void removeNeighbour(Isle neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * Gibt benachbarte Inseln zur�ck, die mit dieser Insel verbunden sind.
     */
    public Set<Isle> getNeighbours() {
        return neighbours;
    }

    /**
     * Gibt Anzahl von insgesamt zu bauenden Br�cken zur�ck.
     * 
     * @return Anzahl an Br�cken
     */
    public int getBridges() {
        return bridges;
    }

    /**
     * Gibt Anzahl an fehlenden Br�cken zur�ck.
     * 
     * @return Anzahl an noch zu bauenden Br�cken
     */
    public int getMissingBridges() {
        return missingBridges;
    }

    /**
     * Gibt y-Wert der Insel-Koordinate zur�ck.
     * 
     * @return y-Wert der Insel-Koordinate
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Gibt x-Wert der Insel-Koordinate zur�ck.
     * 
     * @return x-Wert der Insel-Koordinate
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Gibt Koordinate dieser Insel zur�ck.
     * 
     * @return Koordinate, an der diese Insel platziert ist
     */
    public Coordinate getPos() {
        return pos;
    }

    /**
     * Setzt die Anzahl der fehlenden Br�cken auf den Ursprungswert zur�ck und
     * l�scht alle Nachbarn.
     */
    public void clear() {
        missingBridges = bridges;
        neighbours.clear();
    }

    /**
     * Vergleicht Inseln anhand ihrer Koordinate: Kleiner ist eine Insel, wenn sie
     * n�her am Koordinaten-Ursprung liegt.
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