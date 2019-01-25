package net.joedoe.utils;

import static net.joedoe.utils.GameManager.Mode.LEVEL;

public class GameManager {
    private static final GameManager gameManager = new GameManager();
    private Mode mode = LEVEL;
    private int points = 0, tempPoints = 0, level = 1;

    public enum Mode {
        LEVEL, TIME, FREE
    }

    public static GameManager getInstance() {
        return gameManager;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void addPoints(int points) {
        tempPoints += points;
    }

    public int getAllPoints() {
        return points + tempPoints;
    }

    public int getTempPoints() {
        return tempPoints;
    }

    void setPoints(int points) {
        this.points = points;
    }

    public void savePoints() {
        points += tempPoints;
        tempPoints = 0;
    }

    public void resetAllPoints() {
        points = 0;
    }

    public void resetTempPoints() {
        tempPoints = 0;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        level++;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
