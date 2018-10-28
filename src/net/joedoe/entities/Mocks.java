package net.joedoe.entities;

import java.util.Arrays;
import java.util.List;

public class Mocks {
    public static final int ROWS = 7;
    public static final int COLS = 7;
    public static final List<Isle> ISLES = Arrays.asList(
            new Isle(0, 0, 3),
            new Isle(0, 4, 5),
            new Isle(0, 6, 4),
            new Isle(1, 1, 2),
            new Isle(1, 3, 3),
            new Isle(3, 0, 4),
            new Isle(3, 3, 3),
            new Isle(5, 0, 2),
            new Isle(5, 3, 1),
            new Isle(6, 1, 3),
            new Isle(6, 4, 5),
            new Isle(6, 6, 3));
}
