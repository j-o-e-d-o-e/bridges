package net.joedoe.utils;

/**
 * Enthält Konstanten zur Darstellung und zur Spiellogik.
 */
public class GameInfo {
    // Darstellung
    public static final int CONTAINER_OFFSET = 10;
    public static final int ISLE_SIZE_MAX = 90;
    public static final int ISLE_SIZE_MIN = 30;
    public static int ISLE_SIZE = 60;
    public static final int ONE_TILE = ISLE_SIZE + CONTAINER_OFFSET * 2;
    // into isle-tile
    public static final int BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2;
    // distance between two parallel bridges
    public static final int BRIDGE_OFFSET_MAX = 5;
    public static final int BRIDGE_OFFSET_MIN = 3;
    public static final int BRIDGE_OFFSET = 4;

    // Logik
    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 25;
    public static final int MIN_HEIGHT = 4;
    public static final int MAX_HEIGHT = 25;
    public static final int MIN_ISLES_COUNT = 2;
    public static final int MAX_ISLES_COUNT = 125;
}