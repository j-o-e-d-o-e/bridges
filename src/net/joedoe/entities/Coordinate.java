package net.joedoe.entities;

public class Coordinate {
    private int y, x;

    Coordinate(int y, int x) {
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