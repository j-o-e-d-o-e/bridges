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
        Bridge bridge;
        if (startIsle.getBridge(endIsle) == null)
            bridge = new Bridge(startIsle, endIsle);
        else if (endIsle.getBridge(startIsle) == null)
            bridge = new Bridge(endIsle, startIsle);
        else return null;
        startIsle.addBridge(bridge);
        endIsle.addBridge(bridge);
        bridges.add(bridge);
        return Converter.convertBridgeToData(bridge);
    }

    public Coordinate[] removeBridge(int x, int y, Direction direction) {
        Isle startIsle = getIsle(x, y);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = startIsle.getBridge(endIsle);
        if (bridge == null) bridge = endIsle.getBridge(startIsle);
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
            Isle startIsle = getIsle(coordinates[0].getX(), coordinates[0].getY());
            Isle endIsle = getIsle(coordinates[1].getX(), coordinates[1].getY());
            Bridge bridge = new Bridge(startIsle, endIsle);
            startIsle.addBridge(bridge);
            endIsle.addBridge(bridge);
            bridges.add(bridge);
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

    public void reset() {
        bridges.clear();
        isles.forEach(Isle::clearBridges);
    }
}