package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Converter;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridController {
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController() {
        LOGGER.setLevel(Level.OFF);
    }

    @SuppressWarnings("Duplicates")
    public Coordinate[] addBridge(Coordinate pos, Direction direction) {
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        boolean collides;
        if (startIsle.compareTo(endIsle) > 0)
            collides = collidesBridges(endIsle.getPos(), startIsle.getPos());
        else collides = collidesBridges(startIsle.getPos(), endIsle.getPos());
        if (collides) return null;
        Bridge bridge = addBridge(startIsle, endIsle);
        if (bridge == null) return null;
        return Converter.convertBridgeToData(bridge);
    }

    Bridge addBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge;
        if (startIsle.getBridgeTo(endIsle) == null)
            bridge = new Bridge(startIsle, endIsle);
        else if (endIsle.getBridgeTo(startIsle) == null)
            bridge = new Bridge(endIsle, startIsle);
        else return null;
        startIsle.addBridge(bridge);
        endIsle.addBridge(bridge);
        bridges.add(bridge);
        return bridge;
    }

    @SuppressWarnings("Duplicates")
    public Coordinate[] removeBridge(Coordinate pos, Direction direction) {
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = startIsle.getBridgeTo(endIsle);
        if (bridge == null) bridge = endIsle.getBridgeTo(startIsle);
        if (bridge == null) return null;
        startIsle.removeBridge(bridge);
        endIsle.removeBridge(bridge);
        bridges.remove(bridge);
        return Converter.convertBridgeToData(bridge);
    }

    /*Returns the nearest isle to the specified isle in the specified direction
     * @param isle - the isle for which is nearest isle is to be found
     * @param direction - the direction where to look for the nearest isle
     * */
    public Isle getEndIsle(Isle isle, Direction direction) {
        LOGGER.info(isle.toString() + " " + direction);
        if (direction == Direction.UP)
            return isles.stream().sorted(Collections.reverseOrder())
                    .filter(i -> i.getX() == isle.getX() && i.getY() < isle.getY())
                    .findFirst().orElse(null);
        else if (direction == Direction.LEFT)
            return isles.stream().sorted(Collections.reverseOrder())
                    .filter(i -> i.getY() == isle.getY() && i.getX() < isle.getX())
                    .findFirst().orElse(null);
        else if (direction == Direction.DOWN)
            return isles.stream().filter(i -> i.getX() == isle.getX() && i.getY() > isle.getY())
                    .findFirst().orElse(null);
        else
            return isles.stream().filter(i -> i.getY() == isle.getY() && i.getX() > isle.getX())
                    .findFirst().orElse(null);
    }

    @SuppressWarnings("Duplicates")
    public boolean collidesBridges(Coordinate start, Coordinate end) {
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < start.getY() && b.getEndY() > start.getY()
                    && start.getX() < b.getStartX() && end.getX() > b.getStartX());
        else
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < start.getX() && b.getEndX() > start.getX()
                    && start.getY() < b.getStartY() && end.getY() > b.getStartY());
    }

    public Isle getIsle(int x, int y) {
        return isles.stream().filter(isle -> isle.getY() == y && isle.getX() == x).findFirst().orElse(null);
    }

    private Isle getIsle(Coordinate pos) {
        return isles.stream().filter(isle -> isle.getPos() == pos).findFirst().orElse(null);
    }

    public void setBridges(Object[][] bridgesData) {
        for (Object[] data : bridgesData) {
            Isle startIsle = getIsle((Coordinate) data[0]);
            Isle endIsle = getIsle((Coordinate) data[1]);
            Bridge bridge = new Bridge(startIsle, endIsle);
            startIsle.addBridge(bridge);
            endIsle.addBridge(bridge);
            bridges.add(bridge);
            if ((boolean) data[2]) {
                bridge = new Bridge(endIsle, startIsle);
                startIsle.addBridge(bridge);
                endIsle.addBridge(bridge);
                bridges.add(bridge);
            }
        }
    }

    public void setIsles(Object[][] islesData) {
        for (Object[] isle : islesData)
            isles.add(new Isle((Coordinate) isle[0], (int) isle[1]));
        Collections.sort(isles);
    }

    public int getBridgeCount(Coordinate pos) {
        Isle isle = getIsle(pos);
        return isle.getBridgeCount();
    }

    public int getMissingBridgeCount(Coordinate pos) {
        Isle isle = getIsle(pos);
        return isle.getMissingBridgeCount();
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    List<Isle> getIsles() {
        return isles;
    }

    public int getIslesSize() {
        return isles.size();
    }

    public void reset() {
        bridges.clear();
        isles.forEach(Isle::clearBridges);
    }
}