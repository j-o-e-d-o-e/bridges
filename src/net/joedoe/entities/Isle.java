package net.joedoe.entities;

import net.joedoe.utils.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Isle implements Comparable<Isle> {
    private final Coordinate position;
    private int bridgeCount;
    private List<Bridge> bridges = new ArrayList<>();
    private Set<Isle> neighbours = new HashSet<>();

    public Isle(int x, int y, int bridgeCount) {
        position = new Coordinate(x, y);
        this.bridgeCount = bridgeCount;
    }

    public void addBridge(Bridge bridge) {
        bridges.add(bridge);
        if (bridge.getStartIsle() != this) neighbours.add(bridge.getStartIsle());
        else if (bridge.getEndIsle() != this) neighbours.add(bridge.getEndIsle());
    }

    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
        for (Isle isle : bridge.getIsles()) {
            if (isle == this) continue;
            boolean remove = bridges.stream().noneMatch(b ->
                    b.getStartIsle() == isle || b.getEndIsle() == isle);
            if (remove) neighbours.remove(isle);
        }
    }

    public Bridge getBridge(Isle isle, boolean outgoing) {
        if (outgoing)
            return bridges.stream().filter(bridge -> bridge.getStartIsle() == this
                    && bridge.getEndIsle() == isle).findFirst().orElse(null);
        else
            return bridges.stream().filter(bridge -> bridge.getStartIsle() == isle
                    && bridge.getEndIsle() == this).findFirst().orElse(null);
    }

    public int getBridgeCount() {
        return bridgeCount;
    }

    public int getMissingBridgeCount() {
        return bridgeCount - bridges.size();
    }

    public void increaseBridgeCount() {
        this.bridgeCount++;
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public Set<Isle> getNeighbours() {
        return neighbours;
    }

    public Coordinate getPos() {
        return position;
    }

    public int getY() {
        return position.getY();
    }

    public int getX() {
        return position.getX();
    }

    @Override
    public int compareTo(Isle other) {
        return position.compareTo(other.position);
    }

    @Override
    public String toString() {
        return "Isle{" + "position=" + position + "bridgeCount=" + bridgeCount + '}';
    }

    public void clearBridges() {
        bridges.clear();
    }

    public void clearNeighbours() {
        neighbours.clear();
    }
}