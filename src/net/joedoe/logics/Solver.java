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
import java.util.logging.Logger;
import java.util.stream.Collectors;

//import java.util.logging.Level;

public class Solver {
    private GridController controller;
    private StatusChecker checker;

    private final static Logger LOGGER = Logger.getLogger(Solver.class.getName());

    public Solver(GridController controller, StatusChecker checker) {
        this.controller = controller;
        this.checker = checker;
//        LOGGER.setLevel(Level.OFF);
    }

    public Coordinate[] getNextBridge() {
        if (checker.error() || checker.unsolvable() || checker.solved()) return null;
        for (Isle startIsle : getStartIsles()) {
            int missingBridges = startIsle.getMissingBridgeCount();
            List<Isle> neighbours = getNeighbours(startIsle);
            int neighboursSize = neighbours.size();
            Isle endIsle = neighbours.get(0);
            LOGGER.info(startIsle.toString() + " with " + neighboursSize + " neighbours");
            if (bridgesForEach(missingBridges, neighboursSize))
                return createBridge(startIsle, endIsle);
            int neighboursOneBridge = getNeighboursOneBridge(startIsle, neighbours);
            if (neighboursOneBridge == 0) continue;
            endIsle = getEndIsle(startIsle, neighbours);
            if (bridgesForEachButOne(missingBridges, neighboursSize, neighboursOneBridge))
                return createBridge(startIsle, endIsle);
            if (bridgesForEachButTwo(missingBridges, neighboursSize, neighboursOneBridge))
                return createBridge(startIsle, endIsle);
        }
        return null;
    }

    private boolean bridgesForEach(int missingBridges, int neighboursSize) {
        LOGGER.info(missingBridges + " >= " + neighboursSize + " * 2 - 1");
        return missingBridges >= neighboursSize * 2 - 1;
    }

    private boolean bridgesForEachButOne(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        LOGGER.info(missingBridges + " >= " + neighboursSize + " * 2 - 2" + " && " + neighboursOneBridge + " == 1");
        return missingBridges >= neighboursSize * 2 - 2 && neighboursOneBridge >= 1;
    }

    private boolean bridgesForEachButTwo(int missingBridges, int neighboursSize, int neighboursOneBridge) {
        LOGGER.info(missingBridges + " >= " + neighboursSize + " * 2 - 3" + " && " + neighboursOneBridge + " == 2");
        return missingBridges >= neighboursSize * 2 - 3 && neighboursOneBridge >= 2;
    }

    @SuppressWarnings("unused")
    private boolean bridgesForEachButThree(int missingBridges, int neighbours, int neighboursOneBridge) {
        return missingBridges >= neighbours * 2 - 4 && neighboursOneBridge == 3;
    }

    private List<Isle> getStartIsles() {
        return controller.getIsles().stream()
                .filter(isle -> isle.getMissingBridgeCount() > 0)
                .sorted(Collections.reverseOrder(Comparator.comparing(Isle::getMissingBridgeCount)))
                .collect(Collectors.toList());
    }

    private List<Isle> getNeighbours(Isle startIsle) {
        List<Isle> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Isle neighbour = controller.getEndIsle(startIsle, direction);
            if (!invalidNeighbour(startIsle, neighbour))
                neighbours.add(neighbour);
        }
        return neighbours;
    }

    private boolean invalidNeighbour(Isle startIsle, Isle neighbour) {
        return neighbour == null
                || controller.collides(startIsle.getPos(), neighbour.getPos())
                || neighbour.getMissingBridgeCount() == 0
                || startIsle.getBridgeCountTo(neighbour) == 2
                || (startIsle.getBridgeCount() == 1 && neighbour.getBridgeCount() == 1)
                || (startIsle.getBridgeCount() == 2 && neighbour.getBridgeCount() == 2
                && startIsle.getBridgeCountTo(neighbour) == 1
                && controller.getIslesSize() > 2);
    }

    private int getNeighboursOneBridge(Isle startIsle, List<Isle> neighbours) {
        return (int) neighbours.stream()
                .filter(isle -> isle.getMissingBridgeCount() == 1 || startIsle.getBridgeCountTo(isle) == 1)
                .count();
    }

    private Isle getEndIsle(Isle startIsle, List<Isle> neighbours) {
        Isle finalNeighbour;
        finalNeighbour = neighbours.stream()
                .filter(isle -> isle.getMissingBridgeCount() > 1 && startIsle.getBridgeCountTo(isle) == 0)
                .findFirst().orElse(null);
        if (finalNeighbour == null) return neighbours.get(0);
        return finalNeighbour;
    }

    private Coordinate[] createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = controller.addBridge(startIsle, endIsle);
        if (bridge == null) return null;
        LOGGER.info(bridge.toString() + "\n");
        return Converter.convertBridgeToData(bridge);
    }
}
