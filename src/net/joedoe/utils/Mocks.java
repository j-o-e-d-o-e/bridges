package net.joedoe.utils;

public class Mocks {
    public static final int HEIGHT = 7;
    public static final int WIDTH = 7;

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

    public static final Coordinate[][] BRIDGES = {
            {(Coordinate) ISLES[0][0], (Coordinate) ISLES[1][0]},
            {(Coordinate) ISLES[1][0], (Coordinate) ISLES[0][0]},
            {(Coordinate) ISLES[1][0], (Coordinate) ISLES[2][0]},
            {(Coordinate) ISLES[2][0], (Coordinate) ISLES[1][0]},

            {(Coordinate) ISLES[3][0], (Coordinate) ISLES[4][0]},
            {(Coordinate) ISLES[3][0], (Coordinate) ISLES[5][0]},
            {(Coordinate) ISLES[4][0], (Coordinate) ISLES[9][0]},

            {(Coordinate) ISLES[5][0], (Coordinate) ISLES[6][0]},
            {(Coordinate) ISLES[6][0], (Coordinate) ISLES[5][0]},
            {(Coordinate) ISLES[7][0], (Coordinate) ISLES[6][0]},

            {(Coordinate) ISLES[8][0], (Coordinate) ISLES[0][0]},
            {(Coordinate) ISLES[8][0], (Coordinate) ISLES[9][0]},
            {(Coordinate) ISLES[8][0], (Coordinate) ISLES[10][0]},
            {(Coordinate) ISLES[9][0], (Coordinate) ISLES[4][0]},
            {(Coordinate) ISLES[9][0], (Coordinate) ISLES[8][0]},
            {(Coordinate) ISLES[9][0], (Coordinate) ISLES[11][0]},

            {(Coordinate) ISLES[10][0], (Coordinate) ISLES[8][0]},
            {(Coordinate) ISLES[10][0], (Coordinate) ISLES[11][0]},
            {(Coordinate) ISLES[11][0], (Coordinate) ISLES[10][0]},
    };
}
