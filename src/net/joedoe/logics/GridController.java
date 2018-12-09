package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
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

    public Coordinate[] addBridge(Coordinate pos, Direction direction) {
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        if (BridgeDetector.collides(startIsle, endIsle, bridges)) return null;
        return addBridge(startIsle, endIsle);
    }

    Coordinate[] addBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = getBridge(startIsle, endIsle);
        if (bridge == null) {
            bridge = new Bridge(startIsle, endIsle, false);
            startIsle.addNeighbour(endIsle);
            endIsle.addNeighbour(startIsle);
            bridges.add(bridge);
        } else if (bridge.isDoubleBridge()) {
            return null;
        } else {
            bridge.setDoubleBridge(true);
        }
        startIsle.addBridge();
        endIsle.addBridge();
        bridges.forEach(b -> LOGGER.info(b.toString()));
        startIsle = bridge.getStartIsle();
        endIsle = bridge.getEndIsle();
        return new Coordinate[] { startIsle.getPos(), endIsle.getPos() };
    }

    public Coordinate[] removeBridge(Coordinate pos, Direction direction) {
        Isle startIsle = getIsle(pos);
        if (startIsle == null) return null;
        Isle endIsle = getEndIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = getBridge(startIsle, endIsle);
        if (bridge == null) {
            return null;
        } else if (bridge.isDoubleBridge()) {
            bridge.setDoubleBridge(false);
        } else {
            bridges.remove(bridge);
            startIsle.removeNeighbour(endIsle);
            endIsle.removeNeighbour(startIsle);
        }
        startIsle.removeBridge();
        endIsle.removeBridge();
        startIsle = bridge.getStartIsle();
        endIsle = bridge.getEndIsle();
        return new Coordinate[] { startIsle.getPos(), endIsle.getPos() };
    }

    /*
     * Returns the nearest isle to the specified isle in the specified direction
     * 
     * @param isle - the isle for which is nearest isle is to be found
     * 
     * @param direction - the direction where to look for the nearest isle
     */
    public Isle getEndIsle(Isle isle, Direction direction) {
        LOGGER.info(isle.toString() + " " + direction);
        if (direction == Direction.UP) {
            return isles.stream().sorted(Collections.reverseOrder())
                    .filter(i -> i.getX() == isle.getX() && i.getY() < isle.getY()).findFirst().orElse(null);
        } else if (direction == Direction.LEFT) {
            return isles.stream().sorted(Collections.reverseOrder())
                    .filter(i -> i.getY() == isle.getY() && i.getX() < isle.getX()).findFirst().orElse(null);
        } else if (direction == Direction.DOWN) {
            return isles.stream().filter(i -> i.getX() == isle.getX() && i.getY() > isle.getY()).findFirst()
                    .orElse(null);
        } else {
            return isles.stream().filter(i -> i.getY() == isle.getY() && i.getX() > isle.getX()).findFirst()
                    .orElse(null);
        }
    }

    public Isle getIsle(Coordinate pos) {
        return isles.stream().filter(isle -> isle.getPos() == pos).findFirst().orElse(null);
    }

    Bridge getBridge(Isle startIsle, Isle endIsle) {
        return bridges.stream().filter(b -> b.contains(startIsle, endIsle)).findFirst().orElse(null);
    }

    public void setIsles(Object[][] islesData) {
        for (Object[] isle : islesData)
            isles.add(new Isle((Coordinate) isle[0], (int) isle[1]));
    }

    public void setBridges(Object[][] bridgesData) {
        for (Object[] data : bridgesData) {
            Coordinate start = (Coordinate) data[0];
            Coordinate end = (Coordinate) data[1];
            boolean doubleBridge = (boolean) data[2];
            Isle startIsle = getIsle(start);
            Isle endIsle = getIsle(end);
            Bridge bridge = new Bridge(startIsle, endIsle, doubleBridge);
            startIsle.addBridge();
            startIsle.addNeighbour(endIsle);
            endIsle.addBridge();
            endIsle.addNeighbour(startIsle);
            bridges.add(bridge);
            if (doubleBridge) {
                startIsle.addBridge();
                endIsle.addBridge();
            }
        }
    }

    public int getBridges(Coordinate pos) {
        Isle isle = getIsle(pos);
        return isle.getBridges();
    }

    public int getMissingBridges(Coordinate pos) {
        Isle isle = getIsle(pos);
        return isle.getMissingBridges();
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public List<Isle> getIsles() {
        return isles;
    }

    public int getIslesSize() {
        return isles.size();
    }

    public void reset() {
        bridges.clear();
        isles.forEach(Isle::clear);
    }

    public void saveGame() {
        Converter.convertIslesToData(isles);
        Converter.convertBridgesToData(bridges, isles);
    }
}