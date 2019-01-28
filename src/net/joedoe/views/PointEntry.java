package net.joedoe.views;

public class PointEntry {
    private int points;
    private String name;

    public PointEntry(int points, String name) {
        this.points = points;
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
