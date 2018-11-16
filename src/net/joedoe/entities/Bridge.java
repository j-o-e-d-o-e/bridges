package net.joedoe.entities;

import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

public class Bridge {
    private Isle startIsle, endIsle;
    private Coordinate start, end;
    private Alignment alignment;

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
        this.alignment = Alignment.getAlignment(startIsle.getY(), endIsle.getY());
    }

    public Isle getStartIsle() {
        return startIsle;
    }

    public Isle getEndIsle() {
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
}