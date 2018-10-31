package net.joedoe.entities;

public class Coordinate implements Comparable<Coordinate> {
    public int row;
    public int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int compareTo(Coordinate o) {
        return 0;
    }
}
