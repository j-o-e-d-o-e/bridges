package net.joedoe.utils;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;

import java.util.List;

/**
 * Singleton-Klasse, die geladene bzw. gespeicherte Spiel-Daten enthält.
 */
public class GameData {
    private static final GameData gameData = new GameData();
    private int width;
    private int height;
    private int islesCount;
    private List<IIsle> isles;
    private List<IBridge> bridges;
    private Object[][] bridgesData;

    /**
     * Gibt Singleton-Instanz dieser Klasse zurück.
     *
     * @return einzige Instanz dieser Klasse
     */
    public static GameData getInstance() {
        return gameData;
    }

    /**
     * Gibt Breite des Spielfelds zurück.
     *
     * @return Breite des Spielfelds
     */
    public int getWidth() {
        return width;
    }

    /**
     * Legt Breite des Spielfelds fest.
     *
     * @param width Breite des Spielfelds
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gibt Höhe des Spielfelds zurück.
     *
     * @return Höhe des Spielfelds
     */
    public int getHeight() {
        return height;
    }

    /**
     * Legt Höhe des Spielfelds fest.
     *
     * @param height Höhe des Spielfelds
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gibt Anzahl an Inseln auf dem Spielfeld zurück.
     *
     * @return Insel-Anzahl
     */
    int getIslesCount() {
        return islesCount;
    }

    /**
     * Legt Anzahl der Inseln auf dem Spielfeld fest.
     *
     * @param islesCount Insel-Anzahl
     */
    void setIslesCount(int islesCount) {
        this.islesCount = islesCount;
    }

    /**
     * Gibt geladene und zu speichernde Inseln zurück.
     *
     * @return Liste von Inseln
     */
    public List<IIsle> getIsles() {
        return isles;
    }

    /**
     * Legt geladene und zu speichernde Inseln fest.
     *
     * @param isles Liste von Inseln
     */
    public void setIsles(List<IIsle> isles) {
        this.isles = isles;
    }

    /**
     * Gibt geladene Brücken zurück.
     *
     * @return Liste von Brücken
     */
    public List<IBridge> getBridges() {
        return bridges;
    }

    /**
     * Legt geladene Brücken fest.
     *
     * @param bridges Liste von Brücken
     */
    public void setBridges(List<IBridge> bridges) {
        this.bridges = bridges;
    }

    /**
     * Gibt zu speichernde Brücken zurück.
     *
     * @return Liste von Brücken jeweils mit zwei Koordinaten und einem boolean, ob
     * es sich um eine doppelte Brücke handelt
     */
    Object[][] getBridgesData() {
        return bridgesData;
    }

    /**
     * Legt zu speichernde Brücken fest.
     *
     * @param bridges Liste von Brücken jeweils mit zwei Koordinaten und einem boolean,
     *                ob es sich um eine doppelte Brücke handelt
     */
    void setBridgesData(Object[][] bridgesData) {
        this.bridgesData = bridgesData;
    }
}