package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
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

    private final static Logger LOGGER = Logger.getLogger(Solver.class.getName());

    public Solver(GridController controller) {
        this.controller = controller;
        LOGGER.setLevel(Level.OFF);
    }

    public Coordinate[] getNextBridge() {
        List<Isle> startIsles = getStartIsles();
        for (Isle startIsle : startIsles) {
            int missingBridges = startIsle.getMissingBridges();
            List<Isle> connectables = getConnectables(startIsle);
            int connectablesSize = connectables.size();
            if (connectablesSize == 0) continue;
            LOGGER.info(startIsle.toString() + " with " + connectablesSize + " connectables");
            Isle endIsle = getEndIsle(startIsle, connectables);

            if (firstRule(missingBridges, connectablesSize)) {
                LOGGER.info("firstRule: " + missingBridges + "/" + connectablesSize);
                return addBridge(startIsle, endIsle);
            }
            if (secondRule(missingBridges, connectablesSize)) {
                LOGGER.info("secondRule: " + missingBridges + "/" + connectablesSize);
                return addBridge(startIsle, endIsle);
            }

            int connectablesOneBridge = getConnectablesOneBridge(startIsle, connectables);
            if (connectablesOneBridge == 0) continue;
            LOGGER.info(startIsle.toString() + " with " + connectablesSize + " (" + connectablesOneBridge
                    + ") connectables (one bridge)");

            if (thirdRule(missingBridges, connectablesSize, connectablesOneBridge)) {
                LOGGER.info("thirdRule: " + missingBridges + "/" + connectablesSize + "/" + connectablesOneBridge);
                return addBridge(startIsle, endIsle);
            }
            if (fourthRule(missingBridges, connectablesSize, connectablesOneBridge)) {
                LOGGER.info("fourthRule: " + missingBridges + "/" + connectablesSize + "/" + connectablesOneBridge);
                return addBridge(startIsle, endIsle);
            }
        }
        return null;
    }

    private boolean firstRule(int missingBridges, int connactablesSize) {
        return missingBridges % 2 == 0 && missingBridges == connactablesSize * 2;
    }

    private boolean secondRule(int missingBridges, int connectablesSize) {
        return missingBridges % 2 != 0 && missingBridges + 1 == connectablesSize * 2;
    }

    private boolean thirdRule(int missingBridges, int connectablesSize, int connectablesOneBridge) {
        return (missingBridges == 6 || missingBridges == 4 || missingBridges == 2)
                && missingBridges + 2 == connectablesSize * 2 && connectablesOneBridge >= 1;
    }

    private boolean fourthRule(int missingBridges, int connectablesSize, int connectablesOneBridge) {
        return (missingBridges == 5 || missingBridges == 3) && missingBridges + 3 == connectablesSize * 2
                && connectablesOneBridge >= 2;
    }

    public List<Isle> getStartIsles() {
        return controller.getIsles().stream().filter(isle -> isle.getMissingBridges() > 0)
                .sorted(Collections.reverseOrder(Comparator.comparing(Isle::getMissingBridges)))
                .collect(Collectors.toList());
    }

    public List<Isle> getConnectables(Isle startIsle) {
        List<Isle> connectables = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Isle connectable = controller.getEndIsle(startIsle, direction);
            if (connectable != null && !invalidConnectable(startIsle, connectable)) connectables.add(connectable);
        }
        return connectables;
    }

    private boolean invalidConnectable(Isle startIsle, Isle connectable) {
        List<Bridge> bridges = controller.getBridges();
        Bridge bridge = controller.getBridge(startIsle, connectable);
        int islesSize = controller.getIslesSize();
        return (bridge == null && BridgeDetector.collides(startIsle, connectable, bridges))
                || (bridge != null && bridge.isDoubleBridge()) || connectable.getMissingBridges() == 0
                || (startIsle.getBridges() == 1 && connectable.getBridges() == 1 && islesSize != 2)
                || (startIsle.getBridges() == 2 && connectable.getBridges() == 2 && islesSize != 2 && bridge != null);
    }

    public int getConnectablesOneBridge(Isle startIsle, List<Isle> connectables) {
        return (int) connectables.stream()
                .filter(c -> c.getMissingBridges() == 1 || controller.getBridge(startIsle, c) != null).count();
    }

    public Isle getEndIsle(Isle startIsle, List<Isle> connectables) {
        Isle endIsle = connectables.stream()
                .filter(c -> getConnectables(c).size() == 1
                        || (c.getMissingBridges() > 1 && controller.getBridge(startIsle, c) == null))
                .findFirst().orElse(null);
        if (endIsle == null) return connectables.get(0);
        return endIsle;
    }

    private Coordinate[] addBridge(Isle startIsle, Isle endIsle) {
        Coordinate[] pos = controller.addBridge(startIsle, endIsle);
        LOGGER.info("Bridge added: " + pos[0].toString() + "/" + pos[1].toString() + "\n");
        return pos;
    }
}
