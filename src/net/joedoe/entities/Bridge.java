package net.joedoe.entities;

import net.joedoe.utils.Alignment;

public class Bridge {
    private Coordinate start, end;
    private int startRow, endRow, startColumn, endColumn;
    private Isle startIsle, endIsle;
    private Alignment alignment;

    public Bridge(Isle startIsle, Isle endIsle) {
        this.startIsle = startIsle;
        this.endIsle = endIsle;
        startRow = startIsle.getY();
        startColumn = startIsle.getX();
        endRow = endIsle.getY();
        endColumn = endIsle.getX();
        if (startRow + startColumn < endRow + endColumn) {
            start = new Coordinate(startRow, startColumn);
            end = new Coordinate(endRow, endColumn);
        } else {
            start = new Coordinate(endRow, endColumn);
            end = new Coordinate(startRow, startColumn);
        }
        this.alignment = Alignment.getAlignment(startRow, endRow, startColumn, endColumn);
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

    Isle getStartIsle() {
        return startIsle;
    }

    Isle getEndIsle() {
        return endIsle;
    }

    public int getStartX() {
        return start.getX();
    }

    public int getStartY() {
        return start.getY();
    }

    public int getEndX() {
        return end.getX();
    }

    public int getEndY() {
        return end.getY();
    }


    @Override
    public String toString() {
        return "Bridge{" + "startIsle=" + startIsle + ", endIsle=" + endIsle + '}';
    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }
}