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
    private List<Isle> isles = Mocks.ISLES;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<Bridge> bridges = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController() {
        LOGGER.setLevel(Level.OFF);
    }

    public Bridge addBridge(int row, int column, Direction direction) {
        Isle startIsle = isles.stream().filter(isle -> isle.getRow() == row
                && isle.getColumn() == column).findFirst().orElse(null);
        if (startIsle == null) return null;
        Isle endIsle = findIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge;
        Alignment alignment = direction.convertToAlignment();
        if (startIsle.getBridge(alignment) == null)
            bridge = new Bridge(startIsle, endIsle, alignment);
        else if (endIsle.getBridge(alignment) == null)
            bridge = new Bridge(endIsle, startIsle, alignment);
        else
            return null;
        startIsle.addBridge(bridge);
        endIsle.addBridge(bridge);
        bridges.add(bridge);
        return bridge;
    }

    public Isle findIsle(Isle startIsle, Direction direction) {
        LOGGER.info(startIsle.toString() + " " + direction);
        if (direction == Direction.UP || direction == Direction.DOWN) {
            List<Isle> candidates = isles.stream().filter(isle -> isle.getColumn() == startIsle.getColumn()
                    && isle != startIsle).collect(Collectors.toList());
            if (candidates.size() == 0) return null;
            if (direction == Direction.UP) {
                candidates = candidates.stream().filter(
                        c -> c.getRow() < startIsle.getRow()).sorted().collect(Collectors.toList());
                if (candidates.size() == 0) return null;
                return candidates.get(candidates.size() - 1);
            } else {
                return candidates.stream().filter(
                        c -> c.getRow() > startIsle.getRow()).sorted().findFirst().orElse(null);
            }
        } else {
            List<Isle> candidates = isles.stream().filter(isle -> isle.getRow() == startIsle.getRow()
                    && isle != startIsle).collect(Collectors.toList());
            if (candidates.size() == 0) return null;
            if (direction == Direction.LEFT) {
                return candidates.stream().filter(
                        c -> c.getColumn() < startIsle.getColumn()).sorted().findFirst().orElse(null);
            } else {
                candidates = candidates.stream().filter(
                        c -> c.getColumn() > startIsle.getColumn()).sorted().collect(Collectors.toList());
                if (candidates.size() == 0) return null;
                return candidates.get(candidates.size() - 1);
            }
        }
    }

    public Bridge removeBridge(int row, int column, Direction direction) {
        Isle startIsle = isles.stream().filter(isle -> isle.getRow() == row
                && isle.getColumn() == column).findFirst().orElse(null);
        if (startIsle != null) {
            Bridge bridge = startIsle.getBridge(direction.convertToAlignment());
            if (bridge == null) {
                startIsle = findIsle(startIsle, direction);
                if (startIsle == null) return null;
                bridge = startIsle.getBridge(direction.convertToAlignment());
                if (bridge == null) return null;

            }
            startIsle.removeBridge(bridge);
            Isle endIsle = bridge.getEndIsle();
            endIsle.removeBridge(bridge);
            bridges.remove(bridge);
            return bridge;
        }
        return null;
    }


//    private boolean collides(Bridge newBridge) {
//        if (newBridge.getDirection() == Direction.UP || newBridge.getDirection() == Direction.DOWN) {
//            int column = newBridge.getStartColumn();
//            List<Bridge> horizontalBridges = bridges.stream().filter(
//                    bridge -> ((bridge.getDirection() == Direction.LEFT
//                            && column < bridge.getStartColumn() + 1 && column > bridge.getEndColumn() - 1)
//                            || (bridge.getDirection() == Direction.RIGHT
//                            && column > bridge.getStartColumn() - 1 && column < bridge.getEndColumn() + 1)))
//                    .collect(Collectors.toCollection(ArrayList::new));
//            if (newBridge.getDirection() == Direction.UP)
//                for (int row = newBridge.getStartRow() + 1; row >= newBridge.getEndRow() - 1; row--) {
//                    for (Bridge bridge : horizontalBridges) {
//                        if (row == bridge.getStartRow()) {
//                            return true;
//                        }
//                    }
//                }
//            if (newBridge.getDirection() == Direction.DOWN)
//                for (int row = newBridge.getStartRow() - 1; row <= newBridge.getEndRow() + 1; row++) {
//                    for (Bridge bridge : horizontalBridges) {
//                        if (row == bridge.getStartRow()) {
//                            return true;
//                        }
//                    }
//                }
//        }
//        if (newBridge.getDirection() == Direction.LEFT || newBridge.getDirection() == Direction.RIGHT) {
//            int row = newBridge.getStartRow();
//            List<Bridge> verticalBridges = bridges.stream().filter(
//                    bridge -> ((bridge.getDirection() == Direction.UP
//                            && row < bridge.getStartRow() + 1 && row > bridge.getEndRow() - 1)
//                            || (bridge.getDirection() == Direction.DOWN
//                            && row > bridge.getStartRow() - 1 && row < bridge.getEndRow() + 1)))
//                    .collect(Collectors.toCollection(ArrayList::new));
//            if (newBridge.getDirection() == Direction.LEFT)
//                for (int column = newBridge.getStartColumn() + 1; column >= newBridge.getEndColumn() - 1; column--) {
//                    for (Bridge bridge : verticalBridges) {
//                        if (column == bridge.getStartColumn()) {
//                            return true;
//                        }
//                    }
//                }
//            if (newBridge.getDirection() == Direction.RIGHT)
//                for (int column = newBridge.getStartColumn() - 1; column <= newBridge.getEndColumn() + 1; column++) {
//                    for (Bridge bridge : verticalBridges) {
//                        if (column == bridge.getStartColumn()) {
//                            return true;
//                        }
//                    }
//                }
//        }
//        return false;
//    }

    public List<Isle> getIsles() {
        return isles;
    }

    public boolean gameSolved() {
        return isles.stream().allMatch(isle -> isle.getMissingBridgeCount() == 0);
    }

    public int getMissingBridgeCount(int row, int column) {
        Isle isle = isles.stream().filter(i -> i.getRow() == row && i.getColumn() == column).findFirst().orElse(null);
        if (isle != null) return isle.getMissingBridgeCount();
        return 0;
    }

    public int getBridgeCount(int row, int column) {
        Isle isle = isles.stream().filter(i -> i.getRow() == row && i.getColumn() == column).findFirst().orElse(null);
        if (isle != null) return isle.getBridgeCount();
        return 0;
    }
}