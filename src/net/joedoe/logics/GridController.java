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

    public Coordinate[] addBridge(int x, int y, Direction direction) {
        Isle startIsle = getIsle(x, y);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        if (collides(startIsle.getPos(), endIsle.getPos())) return null;
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

    public Coordinate[] removeBridge(int x, int y, Direction direction) {
        Isle startIsle = getIsle(x, y);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = endIsle.getBridgeTo(startIsle);
        if (bridge == null) bridge = startIsle.getBridgeTo(endIsle);
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

    public boolean collides(Coordinate start, Coordinate end) {
        Coordinate finalStart, finalEnd;
        if (start.compareTo(end) > 0) {
            finalStart = end;
            finalEnd = start;
        } else {
            finalStart = start;
            finalEnd = end;
        }
        if (Alignment.getAlignment(finalStart.getY(), finalEnd.getY()) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < finalStart.getY() && b.getEndY() > finalStart.getY()
                    && finalStart.getX() < b.getStartX() && finalEnd.getX() > b.getStartX());
        else
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < finalStart.getX() && b.getEndX() > finalStart.getX()
                    && finalStart.getY() < b.getStartY() && finalEnd.getY() > b.getStartY());
    }

    public Isle getIsle(int x, int y) {
        return isles.stream().filter(isle -> isle.getY() == y && isle.getX() == x).findFirst().orElse(null);
    }

    List<Isle> getIsles() {
        return isles;
    }

    public void setIsles(Object[][] islesData) {
        for (Object[] isle : islesData){
            Coordinate coordinate = (Coordinate) isle[0];
            int bridgeCount = (int) isle[1];
            isles.add(new Isle(coordinate.getX(), coordinate.getY(), bridgeCount));
        }
        Collections.sort(isles);
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public void setBridges(Object[][] bridgesData) {
        for (Object[] data : bridgesData) {
            Coordinate start = (Coordinate) data[0];
            Coordinate end = (Coordinate) data[1];
            Isle startIsle = getIsle(start.getX(), start.getY());
            Isle endIsle = getIsle(end.getX(), end.getY());
            Bridge bridge = new Bridge(startIsle, endIsle);
            startIsle.addBridge(bridge);
            endIsle.addBridge(bridge);
            bridges.add(bridge);
            if((boolean)data[2]){
                bridge = new Bridge(startIsle, endIsle);
                startIsle.addBridge(bridge);
                endIsle.addBridge(bridge);
                bridges.add(bridge);
            }
        }
    }

    public int getBridgeCount(int x, int y) {
        Isle isle = getIsle(x, y);
        return isle.getBridgeCount();
    }

    public int getMissingBridgeCount(int x, int y) {
        Isle isle = getIsle(x, y);
        return isle.getMissingBridgeCount();
    }

    public int getIslesSize() {
        return isles.size();
    }

    public void reset() {
        bridges.clear();
        isles.forEach(Isle::clearBridges);
    }
}