package net.joedoe.utils;

public class GameData {
    private static final GameData gameData = new GameData();
    private int width;
    private int height;
    private int islesCount;
    private Object[][] isles;
    private Object[][] bridges;

    public static GameData getInstance() {
        return gameData;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getIslesCount() {
        return islesCount;
    }

    public void setIslesCount(int islesCount) {
        this.islesCount = islesCount;
    }

    public Object[][] getIsles() {
        return isles;
    }

    public void setIsles(Object[][] isles) {
        this.isles = isles;
    }

    public Object[][] getBridges() {
        return bridges;
    }

    public void setBridges(Object[][] bridges) {
        this.bridges = bridges;
    }
}
