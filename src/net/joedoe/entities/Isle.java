package net.joedoe.entities;

import net.joedoe.utils.Alignment;

import java.util.ArrayList;
import java.util.List;

public class Isle implements Comparable<Isle> {
    private int bridgeCount;
    private int row, column;
    private List<Bridge> bridges = new ArrayList<>();

    Isle(int row, int column, int bridgeCount) {
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

    public Bridge getBridge(Alignment alignment) {
        for (Bridge bridge : bridges)
            if (bridge.getStartIsle() == this && bridge.getAlignment() == alignment)
                return bridge;
        return null;
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
}