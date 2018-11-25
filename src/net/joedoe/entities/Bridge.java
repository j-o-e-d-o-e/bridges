package net.joedoe.entities;

import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

public class Bridge {
    private Isle startIsle, endIsle;
    private Coordinate start, end;
    private boolean doubleBridge;
    private Alignment alignment;

    public Bridge(Isle startIsle, Isle endIsle) {
        this.startIsle = startIsle;
        this.endIsle = endIsle;
        if (startIsle.compareTo(endIsle) > 0) {
            start = endIsle.getPos();
            end = startIsle.getPos();
        } else {
            start = startIsle.getPos();
            end = endIsle.getPos();
        }
        this.alignment = Alignment.getAlignment(startIsle.getY(), endIsle.getY());
    }

    public Bridge(Isle startIsle, Isle endIsle, boolean doubleBridge){
        this(startIsle, endIsle);
        this.doubleBridge = doubleBridge;
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

    public boolean isDoubleBridge() {
        return doubleBridge;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public String toString() {
        return "Bridge{" + "start=" + startIsle + ", end=" + endIsle + '}';
    }
}