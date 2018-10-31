package net.joedoe.utils;

public enum Alignment {
    HORIZONTAL, VERTICAL;


    public static Alignment calculcateAlignment(int x1, int x2, int y1, int y2) {
        if (x1 == x2)
            return Alignment.HORIZONTAL;
        else if (y1 == y2)
            return Alignment.VERTICAL;
        else return null;
    }
}
