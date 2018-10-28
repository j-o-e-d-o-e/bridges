package net.joedoe.controllers;

import javafx.scene.paint.Color;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GridController {
    private List<Isle> isles = Mocks.ISLES;
    private List<Bridge> bridges = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController() {
        LOGGER.setLevel(Level.OFF);
    }

    public Bridge createBridge(Isle startIsle, Direction direction) {
        if (startIsle.getMissingBridgeCount() > 0) {
            Isle endIsle = searchForIsle(startIsle, direction);
            if (endIsle == null) return null;
            if (startIsle.getBridge(direction) != null) {
                Isle tempIsle = startIsle;
                startIsle = endIsle;
                endIsle = tempIsle;
                direction = getOppositeDirection(direction);
                if (startIsle.getBridge(direction) != null ||
                        startIsle.getMissingBridgeCount() <= 0) return null;
            }
            if (endIsle.getMissingBridgeCount() > 0) {
                Bridge bridge = new Bridge(startIsle, direction, endIsle);
                if (!collides(bridge)) {
                    startIsle.addBridge(bridge);
                    endIsle.decreaseMissingBridges();
                    bridges.forEach(b -> b.setColor(Color.GREY));
                    bridges.add(bridge);
                    return bridge;
                }
            }
        }
        return null;
    }

    public Bridge removeBridge(Isle startIsle, Direction direction) {
        Bridge bridge = startIsle.getBridge(direction);
        if (bridge == null) {
            startIsle = searchForIsle(startIsle, direction);
            if (startIsle == null) return null;
            bridge = startIsle.getBridge(getOppositeDirection(direction));
            if (bridge == null) return null;

        }
        startIsle.removeBridge(bridge);
        Isle endIsle = bridge.getEndIsle();
        endIsle.increaseMissingBridges();
        bridges.remove(bridge);
        bridges.forEach(b -> b.setColor(Color.GREY));
        return bridge;
    }

    private Direction getOppositeDirection(Direction direction) {
        Direction oppositeDirection = null;
        if (direction == Direction.UP)
            oppositeDirection = Direction.DOWN;
        if (direction == Direction.LEFT)
            oppositeDirection = Direction.RIGHT;
        if (direction == Direction.DOWN)
            oppositeDirection = Direction.UP;
        if (direction == Direction.RIGHT)
            oppositeDirection = Direction.LEFT;
        return oppositeDirection;
    }


    private Isle searchForIsle(Isle startIsle, Direction direction) {
        LOGGER.info(startIsle.toString() + " " + direction);
        if (direction == Direction.UP) {
            for (int row = startIsle.getRow() - 1; row >= 0; row--) {
                Isle isle = findIsle(row, startIsle.getColumn());
                if (isle != null) return isle;
            }
        }
        if (direction == Direction.LEFT) {
            for (int column = startIsle.getColumn() - 1; column >= 0; column--) {
                Isle isle = findIsle(startIsle.getRow(), column);
                if (isle != null) return isle;
            }
        }
        if (direction == Direction.DOWN) {
            for (int row = startIsle.getRow() + 1; row <= Mocks.ROWS; row++) {
                Isle isle = findIsle(row, startIsle.getColumn());
                if (isle != null) return isle;
            }
        }
        if (direction == Direction.RIGHT) {
            for (int column = startIsle.getColumn() + 1; column <= Mocks.COLS; column++) {
                Isle isle = findIsle(startIsle.getRow(), column);
                if (isle != null) return isle;
            }
        }
        return null;
    }

    private Isle findIsle(int row, int column) {
        for (Isle isle : isles)
            if (isle.getRow() == row && isle.getColumn() == column)
                return isle;
        return null;
    }

    private boolean collides(Bridge newBridge) {
        if (newBridge.getDirection() == Direction.UP || newBridge.getDirection() == Direction.DOWN) {
            int column = newBridge.getStartColumn();
            List<Bridge> horizontalBridges = bridges.stream().filter(
                    bridge -> ((bridge.getDirection() == Direction.LEFT
                            && column < bridge.getStartColumn() + 1 && column > bridge.getEndColumn() - 1)
                            || (bridge.getDirection() == Direction.RIGHT
                            && column > bridge.getStartColumn() - 1 && column < bridge.getEndColumn() + 1)))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (newBridge.getDirection() == Direction.UP)
                for (int row = newBridge.getStartRow() + 1; row >= newBridge.getEndRow() - 1; row--) {
                    for (Bridge bridge : horizontalBridges) {
                        if (row == bridge.getStartRow()) {
                            return true;
                        }
                    }
                }
            if (newBridge.getDirection() == Direction.DOWN)
                for (int row = newBridge.getStartRow() - 1; row <= newBridge.getEndRow() + 1; row++) {
                    for (Bridge bridge : horizontalBridges) {
                        if (row == bridge.getStartRow()) {
                            return true;
                        }
                    }
                }
        }
        if (newBridge.getDirection() == Direction.LEFT || newBridge.getDirection() == Direction.RIGHT) {
            int row = newBridge.getStartRow();
            List<Bridge> verticalBridges = bridges.stream().filter(
                    bridge -> ((bridge.getDirection() == Direction.UP
                            && row < bridge.getStartRow() + 1 && row > bridge.getEndRow() - 1)
                            || (bridge.getDirection() == Direction.DOWN
                            && row > bridge.getStartRow() - 1 && row < bridge.getEndRow() + 1)))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (newBridge.getDirection() == Direction.LEFT)
                for (int column = newBridge.getStartColumn() + 1; column >= newBridge.getEndColumn() - 1; column--) {
                    for (Bridge bridge : verticalBridges) {
                        if (column == bridge.getStartColumn()) {
                            return true;
                        }
                    }
                }
            if (newBridge.getDirection() == Direction.RIGHT)
                for (int column = newBridge.getStartColumn() - 1; column <= newBridge.getEndColumn() + 1; column++) {
                    for (Bridge bridge : verticalBridges) {
                        if (column == bridge.getStartColumn()) {
                            return true;
                        }
                    }
                }
        }
        return false;
    }

    public List<Isle> getIsles() {
        return isles;
    }

    public boolean gameSolved() {
        return isles.stream().allMatch(isle -> isle.getMissingBridgeCount() == 0);
    }
}