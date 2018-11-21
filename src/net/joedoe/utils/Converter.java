package net.joedoe.utils;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;

public class Converter {

    public static Coordinate[] convertBridgeToData(Bridge bridge) {
        Isle startIsle = bridge.getStartIsle();
        Isle endIsle = bridge.getEndIsle();
        return new Coordinate[]{
                new Coordinate(
                        startIsle.getX(), startIsle.getY()),
                new Coordinate(
                        endIsle.getX(), endIsle.getY()),
        };
    }

    public static Isle convertDataToIsle(int[] isle) {
        return new Isle(isle[1], isle[0], isle[2]);
    }

    public static int[] convertIsleToData(Isle isle) {
        return new int[]{isle.getY(), isle.getX(), isle.getBridgeCount()};
    }
}
