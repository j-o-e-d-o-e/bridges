package net.joedoe.controllers;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GridController {
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController() {
        addIsles();
        LOGGER.setLevel(Level.OFF);
    }

    private void addIsles() {
        for (int[] values : Mocks.ISLES) {
            isles.add(new Isle(values[0], values[1], values[2]));
        }
    }

    public Bridge addBridge(int y, int x, Direction direction) {
        Isle startIsle = getIsle(y, x);
        if (startIsle == null) return null;
        Isle endIsle = findIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge;
        if (startIsle.hasNoBridge(endIsle))
            bridge = new Bridge(startIsle, endIsle);
        else if (endIsle.hasNoBridge(startIsle))
            bridge = new Bridge(endIsle, startIsle);
        else
            return null;
        if (collides(bridge)) return null;
        startIsle.addBridge(bridge);
        endIsle.addBridge(bridge);
        bridges.add(bridge);
        return bridge;
    }

    public Bridge removeBridge(int y, int x, Direction direction) {
        Isle startIsle = getIsle(y, x);
        if (startIsle == null) return null;
        Isle endIsle = findIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = startIsle.getBridge(endIsle, true);
        if (bridge == null)
            bridge = endIsle.getBridge(startIsle, true);
        if (bridge == null) return null;
        startIsle.removeBridge(bridge);
        endIsle.removeBridge(bridge);
        bridges.remove(bridge);
        return bridge;
    }

    public Isle findIsle(Isle isle, Direction direction) {
        LOGGER.info(isle.toString() + " " + direction);
        if (direction == Direction.UP || direction == Direction.DOWN) {
            List<Isle> candidates = isles.stream().filter(i -> i.getX() == isle.getX()
                    && i != isle).collect(Collectors.toList());
            if (candidates.size() == 0) return null;
            if (direction == Direction.UP)
                return candidates.stream().filter(
                        c -> c.getY() < isle.getY()).sorted().reduce((first, last) -> last).orElse(null);
            else
                return candidates.stream().filter(
                        c -> c.getY() > isle.getY()).sorted().findFirst().orElse(null);
        } else {
            List<Isle> candidates = isles.stream().filter(i -> i.getY() == isle.getY()
                    && i != isle).collect(Collectors.toList());
            if (candidates.size() == 0) return null;
            if (direction == Direction.LEFT)
                return candidates.stream().filter(
                        c -> c.getX() < isle.getX()).sorted().findFirst().orElse(null);
            else
                return candidates.stream().filter(
                        c -> c.getX() > isle.getX()).sorted().reduce((first, last) -> last).orElse(null);
        }
    }

    public boolean collides(Bridge bridge) {
        if (bridge.getAlignment() == Alignment.HORIZONTAL) {
            int y = bridge.getStartY();
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < y && b.getEndY() > y
                    && bridge.getStartX() < b.getStartX() && bridge.getEndX() > b.getStartX());
        } else {
            int x = bridge.getStartX();
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < x && b.getEndX() > x
                    && bridge.getStartY() < b.getStartY() && bridge.getEndY() > b.getStartY());
        }
    }

    public Isle getIsle(int y, int x) {
        return isles.stream().filter(isle -> isle.getY() == y && isle.getX() == x).findFirst().orElse(null);
    }

    public List<Isle> getIsles() {
        return isles;
    }

    public List<Bridge> getBridges(){
        return bridges;
    }

    public boolean gameSolved() {
        return isles.stream().allMatch(isle -> isle.getMissingBridgeCount() == 0);
    }

    public int getMissingBridgeCount(int y, int x) {
        Isle isle = getIsle(y, x);
        if (isle == null) return 0;
        return isle.getMissingBridgeCount();
    }

    public int getBridgeCount(int y, int x) {
        Isle isle = getIsle(y, x);
        if (isle == null) return 0;
        return isle.getBridgeCount();
    }
}