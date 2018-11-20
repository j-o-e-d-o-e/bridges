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
        return null;
    }

    public List<Isle> getConnectables(Isle isle) {
        List<Isle> connectables = new ArrayList<>();
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            Isle endIsle = controller.getEndIsle(isle, direction);
            if (endIsle != null && !controller.collides(new Bridge(isle, endIsle)))
                connectables.add(endIsle);
        }
        return connectables;
    }
}
