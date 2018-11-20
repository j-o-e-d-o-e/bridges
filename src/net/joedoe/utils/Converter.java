package net.joedoe.utils;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;

public class Converter {

    public static Coordinate[] convertBridgeToData(Bridge bridge, boolean reversed) {
        if (!reversed)
            return new Coordinate[]{
                    new Coordinate(bridge.getStartY(), bridge.getStartX()),
                    new Coordinate(bridge.getEndY(), bridge.getEndX())
            };
        else
            return new Coordinate[]{
                    new Coordinate(bridge.getEndY(), bridge.getEndX()),
                    new Coordinate(bridge.getStartY(), bridge.getStartX())
            };
    }

    public static Coordinate[] convertBridgeToData(Bridge bridge) {
        return new Coordinate[]{
                new Coordinate(
                        bridge.getStartIsle().getY(),
                        bridge.getStartIsle().getX()),
                new Coordinate(
                        bridge.getEndIsle().getY(),
                        bridge.getEndIsle().getX())
        };
    }

    public static Isle convertDataToIsle(int[] isle) {
        return new Isle(isle[0], isle[1], isle[2]);
    }

    public static int[] convertIsleToData(Isle isle) {
        return new int[]{isle.getY(), isle.getX(), isle.getBridgeCount()};
    }
}
