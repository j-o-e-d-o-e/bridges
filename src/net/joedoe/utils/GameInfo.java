package net.joedoe.utils;

import javafx.scene.paint.Color;

/**
 * Enthält Konstanten zur Darstellung und zur Spiellogik.
 */
public class GameInfo {
    // Darstellung
    public static final int CONTAINER_OFFSET = 10;
    public static final int ONE_TILE = 30; // 30
    private static final int CIRCLE_RADIUS = 12;
    public static final int BRIDGE_OFFSET = 3;
    public static final int BRIDGE_OVERLAP = ONE_TILE / 2 - CIRCLE_RADIUS;
    public static final Color STD_COLOR = Color.GREY;

    // Logik
    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 25;
    public static final int MIN_HEIGHT = 4;
    public static final int MAX_HEIGHT = 25;
    public static final int MIN_ISLES_COUNT = 2;
    public static final int MAX_ISLES_COUNT = 125;
}