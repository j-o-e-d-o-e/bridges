package net.joedoe.utils;

public class GameManager {
    private static final GameManager gameManager = new GameManager();
    private Mode mode;
    private int points = 0, tempPoints = 0, level = 1;

    /**
     * Gibt Singleton-Instanz dieser Klasse zurück.
     *
     * @return einzige Instanz dieser Klasse
     */
    public static GameManager getInstance() {
        return gameManager;
    }

    public void setLevelMode() {
        mode = Mode.LEVEL;
    }

    public void setTimeMode() {
        mode = Mode.TIME;
    }

    public void setFreeMode() {
        mode = Mode.FREE;
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

    public Mode getMode() {
        return mode;
    }

    void setPoints(int points) {
        this.points = points;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public enum Mode {
        LEVEL, TIME, FREE
    }
}
