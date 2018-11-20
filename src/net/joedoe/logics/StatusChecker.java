package net.joedoe.logics;

import net.joedoe.entities.Isle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatusChecker {
    private GridController controller;

    public StatusChecker(GridController controller) {
        this.controller = controller;
    }


    public boolean errorOccured() {
        List<Isle> isles = controller.getIsles();
        return isles.stream().anyMatch(isle -> isle.getMissingBridgeCount() < 0);
    }

    public boolean unsolvable() {
        List<Isle> isles = controller.getIsles();
        return isles.stream().allMatch(isle -> isle.getMissingBridgeCount() <= 0);
    }

    public boolean connected() {
        Set<Isle> connectedIsles = new HashSet<>();
        List<Isle> isles = controller.getIsles();
        connected(isles.get(0), connectedIsles);
        return isles.stream().allMatch(isle -> isle.getMissingBridgeCount() == 0) && connectedIsles.size() == isles.size();
    }

    private void connected(Isle isle, Set<Isle> connected) {
        for (Isle neighbour : isle.getNeighbours()) {
            if (connected.contains(neighbour)) continue;
            connected.add(neighbour);
            connected(neighbour, connected);
        }
    }
}
