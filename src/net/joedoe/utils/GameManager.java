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

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public int getPoints() {
        return points + tempPoints;
    }

    public int getTempPoints() {
        return tempPoints;
    }

    public void addPoints(int points) {
        tempPoints += points;
    }

    public void removePoints(int points) {
        tempPoints -= points;
    }

    public void resetTempPoints() {
        tempPoints = 0;
    }

    public void resetPoints() {
        points = 0;
    }

    public void savePoints() {
        points += tempPoints;
        tempPoints = 0;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        level++;
    }

    public Mode getMode() {
        return mode;
    }

    void setPoints(int points) {
        this.points = points;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
