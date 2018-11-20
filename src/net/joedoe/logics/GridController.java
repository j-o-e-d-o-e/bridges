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

    public Coordinate[] addBridge(int y, int x, Direction direction) {
        Isle startIsle = getIsle(y, x);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge;
        boolean reversed = false;
        if (startIsle.getBridge(endIsle, true) == null)
            bridge = new Bridge(startIsle, endIsle);
        else if (endIsle.getBridge(startIsle, true) == null) {
            bridge = new Bridge(endIsle, startIsle);
            reversed = true;
        } else
            return null;
        if (collides(bridge)) return null;
        startIsle.addBridge(bridge);
        endIsle.addBridge(bridge);
        bridges.add(bridge);
        return Converter.convertBridgeToData(bridge, reversed);
    }

    public Coordinate[] removeBridge(int y, int x, Direction direction) {
        Isle startIsle = getIsle(y, x);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = startIsle.getBridge(endIsle, true);
        boolean reversed = false;
        if (bridge == null) {
            bridge = endIsle.getBridge(startIsle, true);
            reversed = true;
        }
        if (bridge == null) return null;
        startIsle.removeBridge(bridge);
        endIsle.removeBridge(bridge);
        bridges.remove(bridge);
        return Converter.convertBridgeToData(bridge, reversed);
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

    public boolean collides(Bridge bridge) {
        LOGGER.info(bridge.toString());
        if (bridge.getAlignment() == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < bridge.getStartY() && b.getEndY() > bridge.getStartY()
                    && bridge.getStartX() < b.getStartX() && bridge.getEndX() > b.getStartX());
        else
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < bridge.getStartX() && b.getEndX() > bridge.getStartX()
                    && bridge.getStartY() < b.getStartY() && bridge.getEndY() > b.getStartY());
    }

    public Isle getIsle(int y, int x) {
        return isles.stream().filter(isle -> isle.getY() == y && isle.getX() == x).findFirst().orElse(null);
    }

    List<Isle> getIsles() {
        return isles;
    }

    public void setIsles(List<int[]> islesData) {
        for (int[] isle : islesData)
            isles.add(Converter.convertDataToIsle(isle));
        Collections.sort(isles);
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    // for testing only
    public void setBridges(List<Coordinate[]> bridgesData) {
        for (Coordinate[] coordinates : bridgesData) {
            Isle startIsle = getIsle(coordinates[0].getY(), coordinates[0].getX());
            Isle endIsle = getIsle(coordinates[1].getY(), coordinates[1].getX());
            Bridge bridge = new Bridge(startIsle, endIsle);
            startIsle.addBridge(bridge);
            endIsle.addBridge(bridge);
            bridges.add(bridge);
        }
    }

    public int getBridgeCount(int y, int x) {
        Isle isle = getIsle(y, x);
        return isle.getBridgeCount();
    }

    public int getMissingBridgeCount(int y, int x) {
        Isle isle = getIsle(y, x);
        return isle.getMissingBridgeCount();
    }

    public void reset() {
        bridges.clear();
        isles.forEach(Isle::clearBridges);
    }
}