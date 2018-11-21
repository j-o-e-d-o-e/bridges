package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Converter;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.LinkedHashMap;
import java.util.Map;
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
            Map<Direction, Isle> neighbours = getNeighbours(startIsle);
            int neighboursSize = neighbours.size();
            for (Map.Entry<Direction, Isle> neighbour : neighbours.entrySet()) {
                if (bridgesForEach(missingBridges, neighboursSize)) {
                    LOGGER.info("bridges for each: "
                            + "\n" + missingBridges + " + 1 >= " + neighboursSize + " * 2");
                    return createBridge(startIsle, neighbour.getValue());
                }
                //                if (twoBridgesForEach(missingBridges, neighboursSize)) {
//                    LOGGER.info("two bridges for each: "
//                            + "\n" + missingBridges + " == " + neighboursSize + " * 2");
//                    return createBridge(startIsle, neighbour.getValue());
//                }
//                if (oneBridgeForEach(missingBridges, neighboursSize)) {
//                    LOGGER.info("one bridge for each"
//                            + "\n" + missingBridges + " == " + neighboursSize + " * 2 - 1");
//                    return createBridge(startIsle, neighbour.getValue());
//                }
            }
        }
        return null;
    }

    private Map<Direction, Isle> getNeighbours(Isle startIsle) {
        Map<Direction, Isle> neighbours = new LinkedHashMap<>();
        for (Direction direction : Direction.values()) {
            Isle endIsle = controller.getEndIsle(startIsle, direction);
            if (validNeighbour(startIsle, endIsle))
                neighbours.put(direction, endIsle);
        }
        return neighbours;
    }

    private boolean validNeighbour(Isle startIsle, Isle endIsle) {
        return endIsle != null
                && !(endIsle.getMissingBridgeCount() == 0)
                && !controller.collides(startIsle.getPos(), endIsle.getPos())
                && !startIsle.hasTwoBridgesTo(endIsle);
    }

    private boolean bridgesForEach(int missingBridges, int neighboursSize) {
        return missingBridges + 1 >= neighboursSize * 2;
    }

    private boolean twoBridgesForEach(int missingBridges, int neighboursSize) {
        return missingBridges == neighboursSize * 2; // for both: missingBridges + 1 >= neighboursSize * 2;
    }

    private boolean oneBridgeForEach(int missingBridges, int neighboursSize) {
        return missingBridges == neighboursSize * 2 - 1;
    }

    private Coordinate[] createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = controller.addBridge(startIsle, endIsle);
        if (bridge == null) return null;
        LOGGER.info("\nBridge: " + bridge.toString() + "\n\n");
        return Converter.convertBridgeToData(bridge);
    }

}
