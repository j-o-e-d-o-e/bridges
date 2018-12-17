package net.joedoe.utils;

/**
 * Koordinate auf dem Spielfeld.
 *
 */
public class Coordinate implements Comparable<Coordinate> {
    private int y, x;

    /**
     * Koordinate wird ihr x- und y-Wert übergeben.
     * 
     * @param x
     *            x-Wert der Koordinate
     * @param y
     *            y-Wert der Koordinate
     */
    public Coordinate(int x, int y) {
        this.y = y;
        this.x = x;
    }

    /**
     * Gibt y-Wert der Koordinate zurück.
     * 
     * @return y-Wert
     */
    public int getY() {
        return y;
    }

    /**
     * Gibt x-Wert der Koordinate zurück.
     * 
     * @return x-Wert
     */
    public int getX() {
        return x;
    }

    /**
     * Vergleicht Koordinaten anhand ihrer Nähe zum Koordinaten-Ursprung.
     */
    @Override
    public int compareTo(Coordinate other) {
        int comp = Integer.compare(x, other.x);
        if (comp == 0) return Integer.compare(y, other.y);
        return comp;
    }

    @Override
    public String toString() {
        return "Coordinate{" + "y=" + y + ", x=" + x + '}';
    }
}