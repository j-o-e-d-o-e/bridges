package net.joedoe.entities;

import java.util.Arrays;

import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

public class Bridge {
    private final Coordinate start, end;
    private final Alignment alignment;
    private final Isle[] isles = new Isle[2];
    private boolean doubleBridge;

    public Bridge(Isle startIsle, Isle endIsle, boolean doubleBridge) {
        if (startIsle.compareTo(endIsle) > 0) {
            start = endIsle.getPos();
            end = startIsle.getPos();
        } else {
            start = startIsle.getPos();
            end = endIsle.getPos();
        }
        alignment = Alignment.getAlignment(startIsle.getY(), endIsle.getY());
        isles[0] = startIsle;
        isles[1] = endIsle;
        this.doubleBridge = doubleBridge;
    }

    public Isle getStartIsle() {
        return isles[0];
    }

    public Isle getEndIsle() {
        return isles[1];
    }
    
    public boolean contains(Isle startIsle, Isle endIsle) {
        return Arrays.asList(isles).contains(startIsle) && Arrays.asList(isles).contains(endIsle);
    }

    public boolean isDoubleBridge() {
        return doubleBridge;
    }

    public void setDoubleBridge(boolean doubleBridge) {
        this.doubleBridge = doubleBridge;
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
        return "Bridge{" + "start=" + isles[0] + ",\nend=" + isles[1] + ", doubleBridge=" + doubleBridge + '}';
    }
}