package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private GridController controller;
    private List<Bridge> solution = new ArrayList<>();

    public Solver(GridController controller) {
        this.controller = controller;
    }

    public Coordinate[] getNextBridge() {
        List<Bridge> bridges = controller.getBridges();
        Bridge next = solution.stream().filter(bridge -> bridges.stream().allMatch(
                b -> bridge.getStartIsle() != b.getStartIsle()
                        && bridge.getEndIsle() != b.getEndIsle())).findFirst().orElse(null);
        if (next == null) return null;
        bridges.add(next);
        Isle startIsle = next.getStartIsle();
        startIsle.addBridge(next);
        Isle endIsle = next.getEndIsle();
        endIsle.addBridge(next);
        if (startIsle.compareTo(endIsle) > 0)
            return new Coordinate[]{
                    new Coordinate(next.getStartY(), next.getStartX()),
                    new Coordinate(next.getEndY(), next.getEndX())
            };
        return new Coordinate[]{
                new Coordinate(next.getEndY(), next.getEndX()),
                new Coordinate(next.getStartY(), next.getStartX())
        };
    }

    public void setSolution(List<Coordinate[]> bridgesData) {
        for (Coordinate[] data : bridgesData)
            solution.add(new Bridge(
                    controller.getIsle(data[0].getY(), data[0].getX()),
                    controller.getIsle(data[1].getY(), data[1].getX())
            ));
    }
}
