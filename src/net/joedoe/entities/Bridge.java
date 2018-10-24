package net.joedoe.entities;

@SuppressWarnings("unused")
public class Bridge {
    private int row, column;

    public Bridge(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
