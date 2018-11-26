package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Converter;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Solver {
    private GridController controller;
    private StatusChecker checker;

    private final static Logger LOGGER = Logger.getLogger(Solver.class.getName());

    public Solver(GridController controller, StatusChecker checker) {
        this.controller = controller;
        this.checker = checker;
        LOGGER.setLevel(Level.OFF);
    }

    public Coordinate[] getNextBridge() {
        if (checker.error() || checker.unsolvable() || checker.solved()) return null;
        for (Isle startIsle : getStartIsles()) {
            int missingBridges = startIsle.getMissingBridgeCount();
            List<Isle> neighbours = getNeighbours(startIsle);
            if (neighbours.size() == 0) continue; //unlikely due to getStartIsles()
            int neighboursSize = neighbours.size();
            Isle endIsle = neighbours.get(0);
            LOGGER.info(startIsle.toString() + " with " + neighboursSize + " neighbours");
            if (firstRule(missingBridges, neighboursSize)) {
                LOGGER.info("firstRule");
                return createBridge(startIsle, endIsle);
            }
            if (secondRule(missingBridges, neighboursSize)) {
                LOGGER.info("secondRule");
                return createBridge(startIsle, endIsle);
            }
            int neighboursOneBridge = getNeighboursOneBridge(neighbours, startIsle);
            if (neighboursOneBridge == 0) continue;
            endIsle = getEndIsle(neighbours, startIsle);
            if (thirdRule(missingBridges, neighboursSize, neighboursOneBridge)) {
                LOGGER.info("thirdRule");
                return createBridge(startIsle, endIsle);
            }
            if (fourthRule(missingBridges, neighboursSize, neighboursOneBridge)) {
                LOGGER.info("fourthRule");
                return createBridge(startIsle, endIsle);
            }
            if (fifthRule(missingBridges, neighboursSize, neighboursOneBridge)) {
                LOGGER.info("fifthRule");
                return createBridge(startIsle, endIsle);
            }
            if (sixthRule(missingBridges, neighboursSize, neighboursOneBridge)) {
                LOGGER.info("sixthRule");
                return createBridge(startIsle, endIsle);
            }
        }
        return null;
    }

    private boolean firstRule(int missingBridges, int neighboursSize) {
        return missingBridges % 2 == 0 && missingBridges == neighboursSize * 2;
    }

    private boolean secondRule(int missingBridges, int neighboursSize) {
        return missingBridges % 2 != 0 && missingBridges + 1 == neighboursSize * 2;
    }

    private boolean thirdRule(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        return (missingBridges == 6 || missingBridges == 4 || missingBridges == 2)
                && missingBridges + 2 == neighboursSize * 2
                && neighboursOneBridge >= 1;
    }

    private boolean fourthRule(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        return (missingBridges == 5 || missingBridges == 3)
                && missingBridges + 3 == neighboursSize * 2
                && neighboursOneBridge >= 2;
    }

    private boolean fifthRule(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        return missingBridges == 4
                && missingBridges + 4 == neighboursSize * 2
                && neighboursOneBridge >= 3;
    }

    private boolean sixthRule(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        return (missingBridges == 1 || missingBridges == 2)
                && neighboursSize == neighboursOneBridge
                && missingBridges == neighboursSize;
    }

    public List<Isle> getStartIsles() {
        return controller.getIsles().stream()
                .filter(isle -> isle.getMissingBridgeCount() > 0)
                .sorted(Collections.reverseOrder(Comparator.comparing(Isle::getMissingBridgeCount)))
                .collect(Collectors.toList());
    }

    public List<Isle> getNeighbours(Isle startIsle) {
        List<Isle> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Isle neighbour = controller.getEndIsle(startIsle, direction);
            if (!invalidNeighbour(neighbour, startIsle))
                neighbours.add(neighbour);
        }
        return neighbours;
    }

    private boolean invalidNeighbour(Isle neighbour, Isle startIsle) {
        return neighbour == null
                || controller.collidesBridges(neighbour.getPos(), startIsle.getPos())
                || neighbour.getMissingBridgeCount() == 0
                || neighbour.getBridgeCountTo(startIsle) == 2
                || (neighbour.getBridgeCount() == 1 && startIsle.getBridgeCount() == 1)
                || (neighbour.getBridgeCount() == 2 && startIsle.getBridgeCount() == 2
                && neighbour.getBridgeCountTo(startIsle) == 1
                && controller.getIslesSize() > 2);
    }

    public int getNeighboursOneBridge(List<Isle> neighbours, Isle startIsle) {
        return (int) neighbours.stream()
                .filter(isle -> isle.getMissingBridgeCount() == 1 || isle.getBridgeCountTo(startIsle) == 1)
                .count();
    }

    public Isle getEndIsle(List<Isle> neighbours, Isle startIsle) {
        Isle endIsle = neighbours.stream()
                .filter(isle -> getNeighbours(isle).size() == 1).findFirst().orElse(null);
        if (endIsle == null)
            endIsle = neighbours.stream()
                    .filter(isle -> isle.getMissingBridgeCount() > 1 && isle.getBridgeCountTo(startIsle) == 0)
                    .findFirst().orElse(null);
        if (endIsle == null) return neighbours.get(0);
        return endIsle;
    }

    private Coordinate[] createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = controller.addBridge(startIsle, endIsle);
        if (bridge == null) return null; //unlikely due to previous checks
        LOGGER.info(bridge.toString() + "\n");
        return Converter.convertBridgeToData(bridge);
    }
}
