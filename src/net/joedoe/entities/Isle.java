package net.joedoe.entities;

import net.joedoe.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Isle implements Comparable<Isle> {
    private Coordinate position;
    private int bridgeCount;
    private List<Bridge> bridges = new ArrayList<>();

    public Isle(int y, int x, int bridgeCount) {
        position = new Coordinate(y, x);
        this.bridgeCount = bridgeCount;
    }

    public void addBridge(Bridge bridge) {
        bridges.add(bridge);
    }

    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
    }

    public Bridge getBridge(Isle isle, boolean outgoing) {
        if (outgoing)
            return bridges.stream().filter(bridge -> bridge.getStartIsle() == this
                    && bridge.getEndIsle() == isle).findFirst().orElse(null);
        else
            return bridges.stream().filter(bridge -> bridge.getStartIsle() == isle
                    && bridge.getEndIsle() == this).findFirst().orElse(null);
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public boolean hasNoBridge(Isle isle) {
        return bridges.stream().noneMatch(bridge -> bridge.getStartIsle() == this && bridge.getEndIsle() == isle);
    }

    public void increaseBridgeCount() {
        this.bridgeCount++;
    }

    public int getBridgeCount() {
        return bridgeCount;
    }

    public int getMissingBridgeCount() {
        return bridgeCount - bridges.size();
    }

    public int getY() {
        return position.getY();
    }

    public int getX() {
        return position.getX();
    }

    @Override
    public int compareTo(Isle isle) {
        if (isle.getY() + isle.getX() < getY() + getX())
            return 1;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "Isle{" + "position=" + position + "bridgeCount=" + bridgeCount + '}';
    }

    public void clearBridges() {
        bridges.clear();
    }
}