package net.joedoe.entities;

import net.joedoe.utils.Alignment;

public class Bridge {
    private int startRow, endRow, startColumn, endColumn;
    private Isle startIsle, endIsle;
    private Alignment alignment;

    public Bridge(Isle startIsle, Isle endIsle, Alignment alignment) {
        this.startIsle = startIsle;
        this.endIsle = endIsle;
        this.alignment = alignment;
        startRow = startIsle.getRow();
        endRow = endIsle.getRow();
        startColumn = startIsle.getColumn();
        endColumn = endIsle.getColumn();
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

    @SuppressWarnings("unused")
    public Alignment getAlignment() {
        return alignment;
    }

    public Isle getStartIsle() {
        return startIsle;
    }

    public Isle getEndIsle() {
        return endIsle;
    }
}