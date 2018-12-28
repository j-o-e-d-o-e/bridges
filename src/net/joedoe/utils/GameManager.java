package net.joedoe.utils;

public class GameManager {
    private static final GameManager gameManager = new GameManager();
    private int points = 0, tempPoints = 0, level = 1;

    /**
     * Gibt Singleton-Instanz dieser Klasse zur�ck.
     *
     * @return einzige Instanz dieser Klasse
     */
    public static GameManager getInstance() {
        return gameManager;
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

    public void resetPoints(){
        tempPoints = 0;
    }

    public void savePoints(){
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
