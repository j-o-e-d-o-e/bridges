package net.joedoe.utils;

/**
 * Enthält Konstanten zur Darstellung und zur Spiellogik.
 */
public class GameInfo {
    // Darstellung
    public static final int CONTAINER_OFFSET = 10;
    public static int ISLE_SIZE = 60; // needs to be even
    static int ISLE_OFFSET = 16;
    public static int ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
    public static int BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2; // bridge-overlap into isle-tile
    public static int BRIDGE_OFFSET = 4; // distance between two parallel bridges
    // Logik
    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 25;
    public static final int MIN_HEIGHT = 4;
    public static final int MAX_HEIGHT = 25;
    public static final int MIN_ISLES_COUNT = 2;
    public static final int MAX_ISLES_COUNT = 125;
}