package net.joedoe.utils;

/**
 * Enthält Konstanten zur Darstellung und zur Spiellogik.
 */
public class GameInfo {
    // Darstellung
    public static final int CONTAINER_OFFSET = 10;
    public static int ISLE_SIZE = 60; // needs to be even
    private static int ISLE_OFFSET = 16;
    public static int ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
    public static int BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2; // bridge-overlap into isle-tile
    public static int BRIDGE_OFFSET = 4; // distance between two parallel bridges

    private static final int ISLE_SIZE_MAX = 100;
    private static final int ISLE_SIZE_MIN = 10;

    // Logik
    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 25;
    public static final int MIN_HEIGHT = 4;
    public static final int MAX_HEIGHT = 25;
    public static final int MIN_ISLES_COUNT = 2;
    public static final int MAX_ISLES_COUNT = 125;

    public static void zoomIn() {
        if (ISLE_SIZE + 10 >= ISLE_SIZE_MAX) return;
        else ISLE_SIZE += 10;
        setIslesOffset();
        ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
        BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2;
        setBridgeOffset();
    }

    public static void zoomOut() {
        if (ISLE_SIZE - 10 <= ISLE_SIZE_MIN) return;
        else ISLE_SIZE -= 10;
        setIslesOffset();
        ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
        BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2;
        setBridgeOffset();
    }

    private static void setIslesOffset() {
        if (ISLE_SIZE >= 90) ISLE_OFFSET = 20;
        else if (ISLE_SIZE >= 60) ISLE_OFFSET = 16;
        else if (ISLE_SIZE >= 30) ISLE_OFFSET = 12;
        else ISLE_OFFSET = 8;

    }

    private static void setBridgeOffset() {
        if (ONE_TILE >= 80) BRIDGE_OFFSET = 5;
        if (ONE_TILE >= 60) BRIDGE_OFFSET = 4;
        if (ONE_TILE >= 40) BRIDGE_OFFSET = 3;
        else BRIDGE_OFFSET = 2;
    }
}