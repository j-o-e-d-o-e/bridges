package net.joedoe.entities;

public class Coordinate {
    private int y, x;

    Coordinate(int y, int x) {
        this.y = y;
        this.x = x;
    }

    int getY() {
        return y;
    }

    int getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Coordinate{" + "y=" + y + ", x=" + x + '}';
    }
}