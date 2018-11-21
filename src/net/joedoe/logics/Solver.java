package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private GridController controller;
    private StatusChecker checker;

    public Solver(GridController controller, StatusChecker checker) {
        this.controller = controller;
        this.checker = checker;
    }

    public Coordinate[] getNextBridge() {
        if (checker.error() || checker.unsolvable() || checker.solved()) return null;
        Coordinate[] next;
        next = bridgeForEachNeighbour();
        if (next != null) return next;
        return null;
    }

    private Coordinate[] bridgeForEachNeighbour() {
        for (Isle isle : controller.getIsles()) {
            List<Isle> connectables = getConnectables(isle);
            if (connectables.size() == 2 * isle.getMissingBridgeCount()) {
                Isle endIsle = connectables.get(0);
                return controller.addBridge(endIsle.getX(), endIsle.getY(), Direction.UP);
            }
        }
        return null;

    }

    private List<Isle> getConnectables(Isle isle) {
        List<Isle> connectables = new ArrayList<>();
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            Isle endIsle = controller.getEndIsle(isle, direction);
            if (endIsle != null && !controller.collides(isle.getPos(), endIsle.getPos()))
                connectables.add(endIsle);
        }
        return connectables;
    }
}
