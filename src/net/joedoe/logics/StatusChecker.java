package net.joedoe.logics;

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
        return isles.stream().anyMatch(isle -> isle.getMissingBridges() < 0);
    }

    public boolean unsolvable() {
        List<Isle> isles = controller.getIsles();
        Set<Isle> connectedIsles = new HashSet<>();
        connected(isles.get(0), connectedIsles);
        return isles.stream().filter(isle -> isle.getMissingBridges() > 0).anyMatch(this::isolated)
                || (isles.stream().allMatch(isle -> isle.getMissingBridges() == 0)
                        && connectedIsles.size() < isles.size());
    }

    private boolean isolated(Isle startIsle) {
        List<Isle> connectables = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Isle endIsle = controller.getEndIsle(startIsle, direction);
            if (endIsle != null && !BridgeDetector.collides(startIsle, endIsle, controller.getBridges()))
                connectables.add(endIsle);
        }
        return startIsle.getMissingBridges() > 0
                && connectables.stream().allMatch(connectable -> connectable.getMissingBridges() <= 0);
    }

    public boolean solved() {
        List<Isle> isles = controller.getIsles();
        Set<Isle> connectedIsles = new HashSet<>();
        connected(isles.get(0), connectedIsles);
        return isles.stream().allMatch(isle -> isle.getMissingBridges() == 0) && connectedIsles.size() == isles.size();
    }

    private void connected(Isle isle, Set<Isle> connected) {
        for (Isle neighbour : isle.getNeighbours()) {
            if (connected.contains(neighbour)) continue;
            connected.add(neighbour);
            connected(neighbour, connected);
        }
    }
}
