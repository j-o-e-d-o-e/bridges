package net.joedoe.entities;

import java.util.Arrays;
import java.util.List;

public class MockData {
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final List<Isle> ISLES = Arrays.asList(
            new Isle(0, 0, 8),
            new Isle(0, 2, 7),
            new Isle(2, 2, 6),
            new Isle(2, 5, 5),
            new Isle(4, 5, 4),
            new Isle(4, 7, 3),
            new Isle(6, 7, 2),
            new Isle(7, 2, 1));
}
