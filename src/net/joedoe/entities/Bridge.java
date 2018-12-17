package net.joedoe.entities;

import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

/**
 * Repräsentiert eine Brücke im Modell.
 *
 */
public class Bridge implements IBridge {
    private final Coordinate start, end;
    private final Alignment alignment;
    private boolean doubleBridge;

    /**
     * Neuer Brücke werden die beiden Inseln-Koordinaten, die sie verbindet,
     * übergeben und ein boolean, ob es sich um eine doppelte Brücke handelt.
     * Diejenige der beiden Koordinaten, die näher zum Koordinaten-Urspung liegt,
     * wird zur 'start'-Koordinate. Zudem wird die Ausrichtung der Brücke ermittelt.
     * 
     * @param start
     *            erste Koordinate
     * @param end
     *            zweite Koordinate
     * @param doubleBridge
     *            true, falls doppelte Brücke
     */
    public Bridge(Coordinate start, Coordinate end, boolean doubleBridge) {
        if (start.compareTo(end) > 0) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
        alignment = Alignment.getAlignment(start.getY(), end.getY());
        this.doubleBridge = doubleBridge;
    }

    /**
     * Identifiziert, ob es sich um diejenige Brücke handelt, die die erste und
     * zweite Insel-Koordinate miteinander verbindet.
     * 
     * @param start
     *            erste Koordinate
     * @param endIsle
     *            zweite Koordinate
     * @return true, falls diese Brücke die beiden Inseln-Koordinaten miteinander
     *         verbindet
     */
    public boolean contains(Coordinate start, Coordinate end) {
        if (start.compareTo(end) > 0) return this.start == end && this.end == start;
        else return this.start == start && this.end == end;
    }

    /**
     * Gibt den x-Wert der beiden Insel-Koordinaten zurück, der näher am
     * Koordinaten-Ursprung liegt.
     * 
     * @return x-Wert der ersten Insel
     */
    public int getStartX() {
        return start.getX();
    }

    /**
     * Gibt den y-Wert der beiden Insel-Koordinaten zurück, der näher am
     * Koordinaten-Ursprung liegt.
     * 
     * @return y-Wert der ersten Insel
     */
    public int getStartY() {
        return start.getY();
    }

    /**
     * Gibt Koordinate zurück, die näher am Koordinaten-Ursprung liegt.
     * 
     * @return erste Koordinate
     */
    public Coordinate getStart() {
        return start;
    }

    /**
     * Gibt den x-Wert der beiden Insel-Koordinaten zurück, der weiter vom
     * Koordinaten-Ursprung entfernt liegt.
     * 
     * @return x-Wert der zweiten Insel
     */
    public int getEndX() {
        return end.getX();
    }

    /**
     * Gibt den y-Wert der beiden Insel-Koordinaten zurück, der weiter vom
     * Koordinaten-Ursprung entfernt liegt.
     * 
     * @return y-Wert der zweiten Insel
     */
    public int getEndY() {
        return end.getY();
    }

    /**
     * Gibt Koordinate zurück, die weiter vom Koordinaten-Ursprung entfernt liegt.
     * 
     * @return zweite Koordinate
     */
    public Coordinate getEnd() {
        return end;
    }

    /**
     * Legt fest, ob es eine einfache oder doppelte Brücke ist.
     * 
     * @param doubleBridge
     *            true, falls diese zu einer doppelten Brücke werden soll
     */
    public void setDoubleBridge(boolean doubleBridge) {
        this.doubleBridge = doubleBridge;
    }

    /**
     * Gibt zurück, ob es sich um eine doppelte Brücke handelt.
     * 
     * @return true, falls doppelte Brücke
     */
    public boolean isDoubleBridge() {
        return doubleBridge;
    }

    /**
     * Gibt Ausrichtung der Brücke zurück.
     * 
     * @return Ausrichtung - entweder horizontal oder vertikal
     */
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public String toString() {
        return "Bridge{" + "start=" + start + ", end=" + end + ", doubleBridge=" + doubleBridge + '}';
    }
}