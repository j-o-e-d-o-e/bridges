package net.joedoe.entities;

import net.joedoe.utils.Coordinate;
import java.util.HashSet;
import java.util.Set;

public class Isle implements Comparable<Isle> {
    private final Coordinate pos;
    private int bridges;
    private int missingBridges;
    private Set<Isle> neighbours = new HashSet<>();

    public Isle(Coordinate pos, int bridges) {
        this.pos = pos;
        this.bridges = bridges;
        missingBridges = bridges;
    }

    public void addBridge() {
        missingBridges--;
    }

    public void removeBridge() {
        missingBridges++;
    }

    public void addNeighbour(Isle neighbour) {
        neighbours.add(neighbour);
    }

    public void removeNeighbour(Isle neighbour) {
        neighbours.remove(neighbour);
    }

    public Set<Isle> getNeighbours() {
        return neighbours;
    }

    public int getBridges() {
        return bridges;
    }

    public void increaseBridges(int bridges) {
        this.bridges += bridges;
    }

    public int getMissingBridges() {
        return missingBridges;
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

    public void clear() {
        missingBridges = bridges;
        neighbours.clear();
    }

    @Override
    public int compareTo(Isle other) {
        return pos.compareTo(other.pos);
    }

    @Override
    public String toString() {
        return "Isle{" + "pos=" + pos + "missingBridges=" + missingBridges + '}';
    }
}