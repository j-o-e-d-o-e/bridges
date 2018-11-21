package net.joedoe.entities;

import net.joedoe.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Isle implements Comparable<Isle> {
    private final Coordinate pos;
    private int bridgeCount;
    private List<Bridge> bridges = new ArrayList<>();

    public Isle(int x, int y, int bridgeCount) {
        pos = new Coordinate(x, y);
        this.bridgeCount = bridgeCount;
    }

    public void addBridge(Bridge bridge) {
        bridges.add(bridge);
    }

    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
    }

    public Bridge getBridge(Isle isle) {
        return bridges.stream().filter(bridge -> bridge.getStartIsle() == this
                && bridge.getEndIsle() == isle).findFirst().orElse(null);
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public int getBridgeCount() {
        return bridgeCount;
    }

    public int getMissingBridgeCount() {
        return bridgeCount - bridges.size();
    }

    public int getBridgesSize(){
        return bridges.size();
    }

    public void increaseBridgeCount() {
        this.bridgeCount++;
    }

    public int getBridgesTo(Isle endIsle) {
        return (int) bridges.stream().filter(bridge ->
                bridge.getStartIsle() == endIsle || bridge.getEndIsle() == endIsle).count();
    }

    public Coordinate getPos() {
        return pos;
    }

    public int getY() {
        return pos.getY();
    }

    public int getX() {
        return pos.getX();
    }

    public void clearBridges() {
        bridges.clear();
    }

    public List<Isle> getNeighbours() {
        List<Isle> neighbours = new ArrayList<>();
        for (Bridge bridge : bridges) {
            Isle startIsle = bridge.getStartIsle();
            if (startIsle != this) neighbours.add(startIsle);
            else neighbours.add(bridge.getEndIsle());
        }
        return neighbours;
    }

    @Override
    public int compareTo(Isle other) {
        return pos.compareTo(other.pos);
    }

    @Override
    public String toString() {
        return "Isle{" + "pos=" + pos + "bridgeCount=" + bridgeCount + '}';
    }
}