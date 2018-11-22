package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Converter;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
        for (Isle startIsle : controller.getIsles()) {
            int missingBridges = startIsle.getMissingBridgeCount();
            List<Isle> endIsles = getEndIsles(startIsle);
            int endIslesSize = endIsles.size();
            for (Isle endIsle : endIsles) {
                if (bridgesForEach(missingBridges, endIslesSize)) {
                    LOGGER.info("bridges for each: "
                            + "\n" + missingBridges + " + 1 >= " + endIslesSize + " * 2");
                    return createBridge(startIsle, endIsle);
                }
                int endIslesMissingOne = (int) endIsles.stream().filter(i -> i.getMissingBridgeCount() == 1).count();
                Isle finalEndIsle = endIsles.stream().filter(i -> i.getMissingBridgeCount() > 1).findFirst().orElse(null);
                if (finalEndIsle == null) continue;
                if (bridgesForEachButOne(missingBridges, endIslesSize, endIslesMissingOne)) {
                    LOGGER.info("bridges for each but one: "
                            + "\n" + missingBridges + " + 1 >= (" + endIslesSize + " - 1) * 2"
                            + " && " + endIslesMissingOne + " == 1");
                    return createBridge(startIsle, finalEndIsle);
                }
                if (bridgesForEachButTwo(missingBridges, endIslesSize, endIslesMissingOne)) {
                    LOGGER.info("bridges for each but two: "
                            + "\n" + missingBridges + " == " + endIslesSize
                            + " && " + endIslesMissingOne + " == 2");
                    return createBridge(startIsle, finalEndIsle);
                }
            }
        }
        return null;
    }

    private boolean bridgesForEach(int missingBridges, int endIslesSize) {
        return missingBridges + 1 >= endIslesSize * 2;
    }

    private boolean bridgesForEachButOne(int missingBridges, int endIslesSize, int endIslesMissingOne) {
        return missingBridges == (endIslesSize - 1) * 2 && endIslesMissingOne == 1;
    }

    private boolean bridgesForEachButTwo(int missingBridges, int endIslesSize, int endIslesMissingOne) {
        return missingBridges == endIslesSize && endIslesMissingOne == 2;
    }

    private List<Isle> getEndIsles(Isle startIsle) {
        List<Isle> endIsles = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Isle endIsle = controller.getEndIsle(startIsle, direction);
            if (validEndIsle(startIsle, endIsle))
                endIsles.add(endIsle);
        }
        return endIsles;
    }

    private boolean validEndIsle(Isle startIsle, Isle endIsle) {
        return endIsle != null
                && !controller.collides(startIsle.getPos(), endIsle.getPos())
                && endIsle.getMissingBridgeCount() != 0
                && !(startIsle.getBridgesTo(endIsle) == 2)
                && !(startIsle.getBridgeCount() == 1 && endIsle.getBridgeCount() == 1)

                && !(startIsle.getMissingBridgeCount() == 1 && endIsle.getMissingBridgeCount() == 1
                && startIsle.getBridgesTo(endIsle) == 1
                && startIsle.getBridgesSize() == 1
                && endIsle.getBridgesSize() == 1);
    }

    private Coordinate[] createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = controller.addBridge(startIsle, endIsle);
        if (bridge == null) return null;
        LOGGER.info("\nBridge: " + bridge.toString() + "\n\n");
        return Converter.convertBridgeToData(bridge);
    }

}
