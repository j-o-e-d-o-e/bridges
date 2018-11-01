package net.joedoe.entities;

import net.joedoe.utils.Alignment;

public class Bridge {
    private Coordinate start, end;
    private Isle startIsle, endIsle;
    private Alignment alignment;

    //TODO:
    private int startRow, endRow, startColumn,endColumn;

    public Bridge(Isle startIsle, Isle endIsle) {
        this.startIsle = startIsle;
        this.endIsle = endIsle;
        if (startIsle.getY() + startIsle.getX() < endIsle.getY() + endIsle.getX()) {
            start = new Coordinate(startIsle.getY(), startIsle.getX());
            end = new Coordinate(endIsle.getY(), endIsle.getX());
        } else {
            start = new Coordinate(endIsle.getY(), endIsle.getX());
            end = new Coordinate(startIsle.getY(), startIsle.getX());
        }
        this.alignment = Alignment.getAlignment(startIsle.getY(), endIsle.getY(), startIsle.getX(), endIsle.getX());

        //TODO:
        startRow = startIsle.getY();
        startColumn = startIsle.getX();
        endRow = endIsle.getY();
        endColumn = endIsle.getX();
    }

    Isle getStartIsle() {
        return startIsle;
    }

    Isle getEndIsle() {
        return endIsle;
    }

    public int getStartY() {
        return start.getY();
    }

    public int getStartX() {
        return start.getX();
    }

    public int getEndY() {
        return end.getY();
    }

    public int getEndX() {
        return end.getX();
    }

    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public String toString() {
        return "Bridge{" + "startIsle=" + startIsle + ", endIsle=" + endIsle + '}';
    }


    //TODO:

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
}