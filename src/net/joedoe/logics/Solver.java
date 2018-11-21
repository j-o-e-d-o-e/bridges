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
                if (bridgesForEachButOne(startIsle, endIsle, endIslesSize)) {
                    LOGGER.info("bridges for each but one: "
                            + "\n" + missingBridges + " + 1 >= (" + endIslesSize + " - 1) * 2");
                    Isle finalEndIsle = endIsles.stream().filter(i -> i != endIsle).findFirst().orElse(null);
                    if (finalEndIsle != null) return createBridge(startIsle, finalEndIsle);
                }
                if (bridgesForEachButTwo(startIsle, endIsles)) {
                    LOGGER.info("bridges for each but two: "
                            + "\n" + missingBridges + " == " + endIslesSize);
                    Isle finalEndIsle = endIsles.stream()
                            .filter(isle -> isle.getMissingBridgeCount() != 1).findFirst().orElse(null);
                    if (finalEndIsle != null) return createBridge(startIsle, finalEndIsle);
                }
            }
        }
        return null;
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
                && endIsle.getMissingBridgeCount() != 0
                && !(startIsle.getBridgeCount() == 1 && endIsle.getBridgeCount() == 1)
                && !controller.collides(startIsle.getPos(), endIsle.getPos())
                && !(startIsle.getBridgesTo(endIsle) == 2)
                && !(startIsle.getMissingBridgeCount() == 1 && endIsle.getMissingBridgeCount() == 1
                && startIsle.getBridgesTo(endIsle) == 1
                && startIsle.getBridgesSize() == 1
                && endIsle.getBridgesSize() == 1);
    }

    private boolean bridgesForEach(int missingBridges, int endIslesSize) {
        return missingBridges + 1 >= endIslesSize * 2;
    }

    private boolean bridgesForEachButOne(Isle startIsle, Isle endIsle, int endIslesSize) {
        return endIsle.getMissingBridgeCount() == 1
                && startIsle.getMissingBridgeCount() == (endIslesSize - 1) * 2;
    }

    private boolean bridgesForEachButTwo(Isle startIsle, List<Isle> endIsles) {
        return endIsles.stream().filter(i -> i.getMissingBridgeCount() == 1).count() == 2
                && startIsle.getMissingBridgeCount() == endIsles.size();
    }


    private Coordinate[] createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = controller.addBridge(startIsle, endIsle);
        if (bridge == null) return null;
        LOGGER.info("\nBridge: " + bridge.toString() + "\n\n");
        return Converter.convertBridgeToData(bridge);
    }

}
