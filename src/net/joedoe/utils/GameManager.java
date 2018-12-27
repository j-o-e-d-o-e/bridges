package net.joedoe.utils;

public class GameManager {
    private static final GameManager gameManager = new GameManager();
    private int points = 0;

    /**
     * Gibt Singleton-Instanz dieser Klasse zurück.
     *
     * @return einzige Instanz dieser Klasse
     */
    public static GameManager getInstance() {
        return gameManager;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void removePoints(int points) {
        this.points -= points;
    }
}
