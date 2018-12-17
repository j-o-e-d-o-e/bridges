package net.joedoe.utils;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;

import java.util.List;

/**
 * Singleton-Klasse, die geladene bzw. gespeicherte Spiel-Daten enth�lt.
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
     * Gibt Singleton-Instanz dieser Klasse zur�ck.
     *
     * @return einzige Instanz dieser Klasse
     */
    public static GameData getInstance() {
        return gameData;
    }

    /**
     * Gibt Breite des Spielfelds zur�ck.
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
     * Gibt H�he des Spielfelds zur�ck.
     *
     * @return H�he des Spielfelds
     */
    public int getHeight() {
        return height;
    }

    /**
     * Legt H�he des Spielfelds fest.
     *
     * @param height H�he des Spielfelds
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gibt Anzahl an Inseln auf dem Spielfeld zur�ck.
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
     * Gibt geladene und zu speichernde Inseln zur�ck.
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
     * Gibt geladene Br�cken zur�ck.
     *
     * @return Liste von Br�cken
     */
    public List<IBridge> getBridges() {
        return bridges;
    }

    /**
     * Legt geladene Br�cken fest.
     *
     * @param bridges Liste von Br�cken
     */
    public void setBridges(List<IBridge> bridges) {
        this.bridges = bridges;
    }

    /**
     * Gibt zu speichernde Br�cken zur�ck.
     *
     * @return Liste von Br�cken jeweils mit zwei Koordinaten und einem boolean, ob
     * es sich um eine doppelte Br�cke handelt
     */
    Object[][] getBridgesData() {
        return bridgesData;
    }

    /**
     * Legt zu speichernde Br�cken fest.
     *
     * @param bridges Liste von Br�cken jeweils mit zwei Koordinaten und einem boolean,
     *                ob es sich um eine doppelte Br�cke handelt
     */
    void setBridgesData(Object[][] bridgesData) {
        this.bridgesData = bridgesData;
    }
}