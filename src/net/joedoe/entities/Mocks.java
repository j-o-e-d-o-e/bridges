package net.joedoe.entities;

import java.util.Arrays;
import java.util.List;

public class Mocks {
    public static final int ROWS = 9;
    public static final int COLS = 9;
    public static final List<Isle> ISLES = Arrays.asList(
            new Isle(0, 0, 8),
//            new Isle(0, 2, 7),
            new Isle(2, 2, 6),
            new Isle(2, 5, 5),
            new Isle(5, 5, 4),
            new Isle(4, 7, 3),
            new Isle(8, 7, 2),
            new Isle(8, 2, 1));
}
