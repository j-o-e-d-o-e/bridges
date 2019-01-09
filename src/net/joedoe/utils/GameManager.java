package net.joedoe.utils;

import java.time.Duration;
import java.time.LocalTime;

public class GameManager {
    private static final GameManager gameManager = new GameManager();
    private boolean levelMode, timeMode, freeMode;
    private int points = 0, tempPoints = 0, level = 1;

    /**
     * Gibt Singleton-Instanz dieser Klasse zurück.
     *
     * @return einzige Instanz dieser Klasse
     */
    public static GameManager getInstance() {
        return gameManager;
    }

    public boolean isLevelMode() {
        return levelMode;
    }

    public void setLevelMode(boolean levelMode) {
        this.levelMode = levelMode;
    }

    public boolean isTimeMode() {
        return timeMode;
    }

    public void setTimeMode(boolean timeMode) {
        this.timeMode = timeMode;
    }

    public boolean isFreeMode() {
        return freeMode;
    }

    public void setFreeMode(boolean freeMode) {
        this.freeMode = freeMode;
    }

    public int getPoints() {
        return points + tempPoints;
    }

    public void addPoints(int points) {
        tempPoints += points;
    }

    public void removePoints(int points) {
        tempPoints -= points;
    }

    public void resetPoints() {
        tempPoints = 0;
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
}
