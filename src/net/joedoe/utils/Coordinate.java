package net.joedoe.utils;

public class Coordinate {
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
    public String toString() {
        return "Coordinate{" + "y=" + y + ", x=" + x + '}';
    }
}