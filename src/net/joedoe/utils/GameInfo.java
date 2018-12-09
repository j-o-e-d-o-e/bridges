package net.joedoe.utils;

import javafx.scene.paint.Color;

public class GameInfo {
    public static final int CONTAINER_OFFSET = 10;
    public static final int ONE_TILE = 30;
    public static final int CIRCLE_RADIUS = 12;
    public static final int BRIDGE_OFFSET = 3;
    public static final int BRIDGE_OVERLAP = ONE_TILE / 2 - CIRCLE_RADIUS;

    public static final Color STD_COLOR = Color.GREY;
    public static final Color SOLVED_COLOR = Color.LIGHTGREEN;
    public static final Color ALERT_COLOR = Color.CORAL;

    public static final int MIN_WIDTH = 4;
    public static final int MAX_WIDTH = 25;
    public static final int MIN_HEIGHT = 2;
    public static final int MAX_HEIGHT = 25;
    public static final int MIN_ISLES_COUNT = 2;
    public static final int MAX_ISLES_COUNT = 125;
}