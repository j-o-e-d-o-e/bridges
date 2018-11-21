package net.joedoe.entities;

import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import java.util.Arrays;
import java.util.List;

public class Bridge {
    private Isle startIsle, endIsle;
    private Coordinate start, end;
    private Alignment alignment;

    public Bridge(Isle startIsle, Isle endIsle) {
        this.startIsle = startIsle;
        this.endIsle = endIsle;
        if (startIsle.compareTo(endIsle) < 0) {
            start = new Coordinate(startIsle.getX(), startIsle.getY());
            end = new Coordinate(endIsle.getX(), endIsle.getY());
        } else {
            start = new Coordinate(endIsle.getX(), endIsle.getY());
            end = new Coordinate(startIsle.getX(), startIsle.getY());
        }
        this.alignment = Alignment.getAlignment(startIsle.getY(), endIsle.getY());
    }

    public Isle getStartIsle() {
        return startIsle;
    }

    public Isle getEndIsle() {
        return endIsle;
    }

    @SuppressWarnings("WeakerAccess")
    public List<Isle> getIsles() {
        return Arrays.asList(startIsle, endIsle);
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