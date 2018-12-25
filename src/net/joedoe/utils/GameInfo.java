package net.joedoe.utils;

import javafx.scene.paint.Color;

/**
 * Enth�lt Konstanten zur Darstellung und zur Spiellogik.
 */
public class GameInfo {
    // Darstellung
    public static final int CONTAINER_OFFSET = 10;
    public static final int ONE_TILE = 30;
    private static final int CIRCLE_RADIUS = 12;
    public static final int BRIDGE_OFFSET = 3;
    public static final int BRIDGE_OVERLAP = ONE_TILE / 2 - CIRCLE_RADIUS;
    public static final Color STD_COLOR = Color.GREY;

    public static final Color ISLES_STD_COLOR = Color.GHOSTWHITE;
    public static final Color ISLES_SOLVED_COLOR = Color.web("#009900");
    public static final Color ISLES_ALERT_COLOR = Color.web("#ff4500");

    public static final Color BRIDGES_STD_COLOR = Color.DARKKHAKI;
    // Logik
    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 25;
    public static final int MIN_HEIGHT = 4;
    public static final int MAX_HEIGHT = 25;
    public static final int MIN_ISLES_COUNT = 2;
    public static final int MAX_ISLES_COUNT = 125;
}