package net.joedoe.utils;

/**
 * Gibt an, ob eine Brücke horizontal oder vertikal ausgerichtet ist.
 *
 */
public enum Alignment {
    HORIZONTAL, VERTICAL;

    /**
     * Ermittelt, ob eine Brücke horizontal oder vertikal ausgerichtet ist.
     * 
     * @param startY
     *            y-Wert der ersten Koordinate einer Brücke
     * @param endY
     *            y-Wert der zweiten Koordinate einer Brücke
     * @return Ausrichtung, entweder horizontal oder vertikal
     */
    public static Alignment getAlignment(int startY, int endY) {
        if (startY == endY) return Alignment.HORIZONTAL;
        else return Alignment.VERTICAL;
    }
}
