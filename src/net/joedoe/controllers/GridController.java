package net.joedoe.controllers;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
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
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private List<Bridge> solution;

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController() {
//        LOGGER.setLevel(Level.OFF);
    }

    public Coordinate[] addBridge(int y, int x, Direction direction) {
        Isle startIsle = getIsle(y, x);
        if (startIsle == null) return null;
        Isle endIsle = findIsle(startIsle, direction);
        LOGGER.info(endIsle.toString());
        if (endIsle == null) return null;
        Bridge bridge;
        boolean reversed = false;
        if (startIsle.hasNoBridge(endIsle))
            bridge = new Bridge(startIsle, endIsle);
        else if (endIsle.hasNoBridge(startIsle)) {
            bridge = new Bridge(endIsle, startIsle);
            reversed = true;
        } else
            return null;
        if (collides(bridge)) return null;
        startIsle.addBridge(bridge);
        endIsle.addBridge(bridge);
        bridges.add(bridge);
        if (!reversed)
            return new Coordinate[]{
                    new Coordinate(startIsle.getY(), startIsle.getX()),
                    new Coordinate(endIsle.getY(), endIsle.getX())
            };
        else
            return new Coordinate[]{
                    new Coordinate(endIsle.getY(), endIsle.getX()),
                    new Coordinate(startIsle.getY(), startIsle.getX())
            };
    }

    public Coordinate[] removeBridge(int y, int x, Direction direction) {
        Isle startIsle = getIsle(y, x);
        if (startIsle == null) return null;
        Isle endIsle = findIsle(startIsle, direction);
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
        if (!reversed)
            return new Coordinate[]{
                    new Coordinate(startIsle.getY(), startIsle.getX()),
                    new Coordinate(endIsle.getY(), endIsle.getX())
            };
        else
            return new Coordinate[]{
                    new Coordinate(endIsle.getY(), endIsle.getX()),
                    new Coordinate(startIsle.getY(), startIsle.getX())
            };
    }

    /*Returns the nearest isle to the specified isle in the specified direction
     * @param isle - the isle for which is nearest isle is to be found
     * @param direction - the direction where to look for the nearest isle
     * */
    public Isle findIsle(Isle isle, Direction direction) {
        LOGGER.info(isle.toString() + " " + direction);
        if (direction == Direction.UP) {
            List<Isle> reversed = new ArrayList<>(isles);
            Collections.reverse(reversed);
            return reversed.stream().filter(i -> i.getX() == isle.getX()
                    && i.getY() < isle.getY()).findFirst().orElse(null);
        } else if (direction == Direction.LEFT) {
            List<Isle> reversed = new ArrayList<>(isles);
            Collections.reverse(reversed);
            return reversed.stream().filter(i -> i.getY() == isle.getY()
                    && i.getX() < isle.getX()).findFirst().orElse(null);
        } else if (direction == Direction.DOWN)
            return isles.stream().filter(i -> i.getX() == isle.getX()
                    && i.getY() > isle.getY()).findFirst().orElse(null);
        else
            return isles.stream().filter(i -> i.getY() == isle.getY()
                    && i.getX() > isle.getX()).findFirst().orElse(null);
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

    public List<Bridge> getBridges() {
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

    public void setIsles(List<int[]> islesData) {
        for (int[] isle : islesData)
            isles.add(new Isle(
                    isle[0],
                    isle[1],
                    isle[2]
            ));
        Collections.sort(isles);
    }

    public void setBridges(List<int[]> bridgesData) {
        for (int[] bridge : bridgesData)
            bridges.add(new Bridge(
                    getIsle(bridge[0], bridge[1]),
                    getIsle(bridge[2], bridge[3])
            ));
    }
}