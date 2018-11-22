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
        List<Isle> startIsles = controller.getIsles().stream()
                .filter(isle -> isle.getMissingBridgeCount() > 0)
                .sorted(Collections.reverseOrder(Comparator.comparing(Isle::getBridgeCount)))
                .collect(Collectors.toList());
        for (Isle startIsle : startIsles) {
            int missingBridges = startIsle.getMissingBridgeCount();
            List<Isle> neighbours = getNeighbours(startIsle);
            for (Isle neighbour : neighbours) {
                if (bridgesForEach(missingBridges, neighbours.size())) {
                    LOGGER.info("bridges for each: "
                            + "\n" + missingBridges + " + 1 >= " + neighbours.size() + " * 2");
                    return createBridge(startIsle, neighbour);
                }
                int neighboursMissingOne = (int) neighbours.stream()
                        .filter(i -> i.getMissingBridgeCount() == 1 || startIsle.getBridgeCountTo(i) == 1).count();
                Isle finalNeighbour = neighbours.stream().filter(i -> i.getMissingBridgeCount() > 1).findFirst().orElse(null);
                if (neighboursMissingOne == 0 || finalNeighbour == null) continue;
                if (bridgesForEachButOne(missingBridges, neighbours.size(), neighboursMissingOne)) {
                    LOGGER.info("bridges for each but one: "
                            + "\n" + missingBridges + " + 1 >= (" + neighbours.size() + " - 1) * 2"
                            + " && " + neighboursMissingOne + " == 1");
                    return createBridge(startIsle, finalNeighbour);
                }
//                if (bridgesForEachButTwo(missingBridges, neighbours.size(), neighboursMissingOne)) {
//                    LOGGER.info("bridges for each but two: "
//                            + "\n" + missingBridges + " == " + neighbours.size()
//                            + " && " + neighboursMissingOne + " == 2");
//                    return createBridge(startIsle, finalNeighbour);
//                }
            }
        }
        return null;
    }

    private boolean bridgesForEach(int missingBridges, int neighbours) {
        return missingBridges >= neighbours * 2 - 1;
    }

    private boolean bridgesForEachButOne(int missingBridges, int neighbours, int neighboursMissingOne) {
        return neighboursMissingOne == 1 && missingBridges >= (neighbours - 1) * 2;
    }

    private boolean bridgesForEachButTwo(int missingBridges, int neighbours, int neighboursMissingOne) {
        return neighboursMissingOne == 2 && missingBridges >= (neighbours - 2) * 2;
    }

    private boolean bridgesForEachButThree(int missingBridges, int neighbours, int neighboursMissingOne) {
        return neighboursMissingOne == 3 && missingBridges >= (neighbours - 2) * 2;
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

    private Coordinate[] createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = controller.addBridge(startIsle, endIsle);
        if (bridge == null) return null;
        LOGGER.info("\nBridge: " + bridge.toString() + "\n\n");
        return Converter.convertBridgeToData(bridge);
    }

}
