package net.joedoe.utils;

public class Coordinate implements Comparable<Coordinate> {
    private int y, x;

    public Coordinate(int x, int y) {
        this.y = y;
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    
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