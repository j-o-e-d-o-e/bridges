package net.joedoe.utils;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;

import java.util.ArrayList;
import java.util.List;

/**
 * Enthält Beispiel-Spieldaten, die beim Start der Applikation geladen werden
 * und u.a. für Tests genutzt werden.
 */
public class Mocks {
    public static final int WIDTH = 7;
    public static final int HEIGHT = 7;
    // public static final int ISLES_COUNT = 12;
    private static List<IIsle> isles;

    public static List<IIsle> getIsles() {
        return createIsles();
    }

    private static List<IIsle> createIsles() {
        List<IIsle> newIsles = new ArrayList<>();
        newIsles.add(new Isle(new Coordinate(0, 0), 3));
        newIsles.add(new Isle(new Coordinate(0, 3), 4));
        newIsles.add(new Isle(new Coordinate(0, 5), 2));
        newIsles.add(new Isle(new Coordinate(1, 1), 2));
        newIsles.add(new Isle(new Coordinate(1, 6), 3));
        newIsles.add(new Isle(new Coordinate(3, 1), 3));
        newIsles.add(new Isle(new Coordinate(3, 3), 3));
        newIsles.add(new Isle(new Coordinate(3, 5), 1));
        newIsles.add(new Isle(new Coordinate(4, 0), 5));
        newIsles.add(new Isle(new Coordinate(4, 6), 5));
        newIsles.add(new Isle(new Coordinate(6, 0), 4));
        newIsles.add(new Isle(new Coordinate(6, 6), 3));
        isles = newIsles;
        return newIsles;
    }

    public static List<IBridge> getBridges() {
        return createBridges();
    }

    private static List<IBridge> createBridges() {
        List<IBridge> bridges = new ArrayList<>();
        bridges.add(new Bridge(isles.get(0).getPos(), isles.get(1).getPos(), true));
        bridges.add(new Bridge(isles.get(0).getPos(), isles.get(8).getPos(), false));
        bridges.add(new Bridge(isles.get(1).getPos(), isles.get(2).getPos(), true));
        bridges.add(new Bridge(isles.get(3).getPos(), isles.get(4).getPos(), false));
        bridges.add(new Bridge(isles.get(3).getPos(), isles.get(5).getPos(), false));
        bridges.add(new Bridge(isles.get(4).getPos(), isles.get(9).getPos(), true));
        bridges.add(new Bridge(isles.get(5).getPos(), isles.get(6).getPos(), true));
        bridges.add(new Bridge(isles.get(6).getPos(), isles.get(7).getPos(), false));
        bridges.add(new Bridge(isles.get(8).getPos(), isles.get(9).getPos(), true));
        bridges.add(new Bridge(isles.get(8).getPos(), isles.get(10).getPos(), true));
        bridges.add(new Bridge(isles.get(9).getPos(), isles.get(11).getPos(), false));
        bridges.add(new Bridge(isles.get(10).getPos(), isles.get(11).getPos(), true));
        return bridges;
    }
}
