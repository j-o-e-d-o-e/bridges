package net.joedoe.utils;

/**
 * Gibt an, ob eine Br�cke horizontal oder vertikal ausgerichtet ist.
 *
 */
public enum Alignment {
    HORIZONTAL, VERTICAL;

    /**
     * Ermittelt, ob eine Br�cke horizontal oder vertikal ausgerichtet ist.
     * 
     * @param startY
     *            y-Wert der ersten Koordinate einer Br�cke
     * @param endY
     *            y-Wert der zweiten Koordinate einer Br�cke
     * @return Ausrichtung, entweder horizontal oder vertikal
     */
    public static Alignment getAlignment(int startY, int endY) {
        if (startY == endY) return Alignment.HORIZONTAL;
        else return Alignment.VERTICAL;
    }
}
