package net.joedoe.utils;

import java.util.Arrays;
import java.util.List;

public class Mocks {
    public static final int ROWS = 7;
    public static final int COLS = 7;
    public static final List<int[]> ISLES = Arrays.asList(
            new int[]{0, 0, 3},
            new int[]{0, 4, 5},
            new int[]{0, 6, 4},
            new int[]{1, 1, 2},
            new int[]{1, 3, 3},
            new int[]{3, 0, 4},
            new int[]{3, 3, 3},
            new int[]{5, 0, 2},
            new int[]{5, 3, 1},
            new int[]{6, 1, 3},
            new int[]{6, 4, 5},
            new int[]{6, 6, 3}
    );
    public static final List<Coordinate[]> BRIDGES = Arrays.asList(
            new Coordinate[]{
                    new Coordinate(0, 0),
                    new Coordinate(0, 4)},
            new Coordinate[]{
                    new Coordinate(0, 0),
                    new Coordinate(3, 0)},
            new Coordinate[]{
                    new Coordinate(3, 0),
                    new Coordinate(0, 0)},

            new Coordinate[]{
                    new Coordinate(3, 0),
                    new Coordinate(5, 0)},
            new Coordinate[]{
                    new Coordinate(5, 0),
                    new Coordinate(3, 0)},

            new Coordinate[]{
                    new Coordinate(0, 4),
                    new Coordinate(0, 6)},
            new Coordinate[]{
                    new Coordinate(0, 6),
                    new Coordinate(0, 4)},
            new Coordinate[]{
                    new Coordinate(0, 4),
                    new Coordinate(6, 4)},
            new Coordinate[]{
                    new Coordinate(6, 4),
                    new Coordinate(0, 4)},

            new Coordinate[]{
                    new Coordinate(0, 6),
                    new Coordinate(6, 6)},
            new Coordinate[]{
                    new Coordinate(6, 6),
                    new Coordinate(0, 6)},

            new Coordinate[]{
                    new Coordinate(6, 6),
                    new Coordinate(6, 4)},

            new Coordinate[]{
                    new Coordinate(6, 4),
                    new Coordinate(6, 1)},
            new Coordinate[]{
                    new Coordinate(6, 1),
                    new Coordinate(6, 4)},

            new Coordinate[]{
                    new Coordinate(6, 1),
                    new Coordinate(1, 1)},

            new Coordinate[]{
                    new Coordinate(1, 1),
                    new Coordinate(1, 3)},

            new Coordinate[]{
                    new Coordinate(1, 3),
                    new Coordinate(3, 3)},
            new Coordinate[]{
                    new Coordinate(3, 3),
                    new Coordinate(1, 3)},

            new Coordinate[]{
                    new Coordinate(5, 3),
                    new Coordinate(3, 3)}
    );
}
