package net.joedoe.utils;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;

public class Converter {

    public static Coordinate[] convertBridgeToData(Bridge bridge) {
        Isle startIsle = bridge.getStartIsle();
        Isle endIsle = bridge.getEndIsle();
        return new Coordinate[]{startIsle.getPos(), endIsle.getPos()};
    }
}