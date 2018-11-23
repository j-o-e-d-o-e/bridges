package net.joedoe.utils;

public class Mocks {
    public static final int WIDTH = 7;
    public static final int HEIGHT = 7;

    public static final Object[][] ISLES = {
            {new Coordinate(0, 0), 3},
            {new Coordinate(0, 3), 4},
            {new Coordinate(0, 5), 2},
            {new Coordinate(1, 1), 2},
            {new Coordinate(1, 6), 3},
            {new Coordinate(3, 1), 3},
            {new Coordinate(3, 3), 3},
            {new Coordinate(3, 5), 1},
            {new Coordinate(4, 0), 5},
            {new Coordinate(4, 6), 5},
            {new Coordinate(6, 0), 4},
            {new Coordinate(6, 6), 3},
    };

    public static final Object[][] BRIDGES = {
            {ISLES[0][0], ISLES[1][0], true},
            {ISLES[0][0], ISLES[8][0], false},
            {ISLES[1][0], ISLES[2][0], true},
            {ISLES[3][0], ISLES[4][0], false},
            {ISLES[3][0], ISLES[5][0], false},
            {ISLES[4][0], ISLES[9][0], true},
            {ISLES[5][0], ISLES[6][0], true},
            {ISLES[6][0], ISLES[7][0], false},
            {ISLES[8][0], ISLES[9][0], true},
            {ISLES[8][0], ISLES[10][0], true},
            {ISLES[9][0], ISLES[11][0], false},
            {ISLES[10][0], ISLES[11][0], true},
    };
}
