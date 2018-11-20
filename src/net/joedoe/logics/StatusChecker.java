package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatusChecker {
    private GridController controller;

    public StatusChecker(GridController controller) {
        this.controller = controller;
    }

    public boolean error() {
        List<Isle> isles = controller.getIsles();
        return isles.stream().anyMatch(isle -> isle.getMissingBridgeCount() < 0);
    }

    public boolean unsolvable() {
        return controller.getIsles().stream().filter(isle -> isle.getMissingBridgeCount() > 0)
                .anyMatch(this::isolated);
    }

    private boolean isolated(Isle isle) {
        List<Isle> connectables = new ArrayList<>();
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            Isle endIsle = controller.getEndIsle(isle, direction);
            if (endIsle != null && !controller.collides(new Bridge(isle, endIsle)))
                connectables.add(endIsle);
        }
        return isle.getMissingBridgeCount() > 0 && connectables.stream().allMatch(
                connectable -> connectable.getMissingBridgeCount() <= 0);
    }

    public boolean solved() {
        List<Isle> isles = controller.getIsles();
        Set<Isle> connectedIsles = new HashSet<>();
        connected(isles.get(0), connectedIsles);
        return isles.stream().allMatch(isle -> isle.getMissingBridgeCount() == 0)
                && connectedIsles.size() == isles.size();
    }

    private void connected(Isle isle, Set<Isle> connected) {
        for (Isle neighbour : isle.getNeighbours()) {
            if (connected.contains(neighbour)) continue;
            connected.add(neighbour);
            connected(neighbour, connected);
        }
    }
}
