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
        if (x + y > other.x + other.y) return 1;
        else return -1;
    }

    @Override
    public String toString() {
        return "Coordinate{" + "y=" + y + ", x=" + x + '}';
    }
}