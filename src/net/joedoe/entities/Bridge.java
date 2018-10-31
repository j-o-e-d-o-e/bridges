package net.joedoe.entities;

import net.joedoe.utils.Alignment;

public class Bridge {
    private int startRow, endRow, startColumn, endColumn;
    private Isle startIsle, endIsle;
    private Alignment alignment;

    public Bridge(Isle startIsle, Isle endIsle) {
        this.startIsle = startIsle;
        this.endIsle = endIsle;
        startRow = startIsle.getRow();
        endRow = endIsle.getRow();
        startColumn = startIsle.getColumn();
        endColumn = endIsle.getColumn();
        this.alignment = Alignment.calculcateAlignment(startRow, endRow, startColumn, endColumn);
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public Isle getStartIsle() {
        return startIsle;
    }

    public Isle getEndIsle() {
        return endIsle;
    }

    @Override
    public String toString() {
        return "Bridge{" + "startIsle=" + startIsle + ", endIsle=" + endIsle + '}';
    }
}