package net.joedoe.entities;

import java.util.ArrayList;
import java.util.List;

public class Isle implements Comparable<Isle> {
    private Coordinate coordinate;
    private int row, column;
    private int bridgeCount;
    private List<Bridge> bridges = new ArrayList<>();

    Isle(int row, int column, int bridgeCount) {
        coordinate = new Coordinate(row, column);
        this.row = row;
        this.column = column;
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

    public int getBridgeCount() {
        return bridgeCount;
    }

    public int getMissingBridgeCount() {
        return bridgeCount - bridges.size();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Isle{" + "row=" + row + ", column=" + column + '}';
    }

    @Override
    public int compareTo(Isle isle) {
        if (isle.getRow() == row)
            if (isle.getColumn() > column)
                return 1;
            else
                return -1;
        else if (isle.getColumn() == column)
            if (isle.getRow() < row)
                return 1;
            else
                return -1;
        return 0;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}