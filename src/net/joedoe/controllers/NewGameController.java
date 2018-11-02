package net.joedoe.controllers;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewGameController {
    private static final int MAX = 25;
    private static final int MIN = 2;
    private int rows, columns, isleCount;
    private GridController gridController;
    private List<Integer> indices;
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(NewGameController.class.getName());

    public NewGameController() {
        gridController = new GridController();
        LOGGER.setLevel(Level.OFF);
    }

    public void createGame() {
        LOGGER.info("Create New Game: AUTO");
        rows = (int) (Math.random() * MAX) + MIN;
        columns = (int) (Math.random() * MAX) + MIN;
        isleCount = (int) (Math.random() * (0.2 * rows * columns)) + MIN;
        generateGame();
    }

    public void createGame(int width, int height) {
        LOGGER.info("Create New Game: WIDTH + HEIGHT");
        this.rows = width;
        this.columns = height;
        this.isleCount = (int) (Math.random() * (0.2 * rows * columns)) + MIN;
        generateGame();
    }

    public void createGame(int width, int height, int isles) {
        LOGGER.info("Create New Game: WIDTH + HEIGHT + ISLES");
        this.rows = width;
        this.columns = height;
        this.isleCount = isles;
        generateGame();
    }

    public void generateGame() {
        LOGGER.info("Generate new Game");
        indices = IntStream.range(0, rows * columns).boxed().collect(Collectors.toList());
        while (isleCount > 0) {
            isleCount--;
            generateIsle();

            //TODO
//            generateBridge();
        }
        gridController.setIsles(isles);
//        gridController.setSolution(bridges);
    }

    private void generateIsle() {
        int rand = (int) (Math.random() * indices.size());
        int index = indices.get(rand);
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + rows));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index - rows));
        indices.remove(new Integer(index - 1));
        int y = index % rows;
        int x = index / rows;
        int bridgeCount = (int) (Math.random() * 8) + 1;
        Isle isle = new Isle(y, x, bridgeCount);
        isles.add(isle);
        LOGGER.info("Generated Index: " + index);
        LOGGER.info("Generated Isle: " + isle.toString());
    }

    private void generateBridge() {
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setIsleCount(int isleCount) {
        this.isleCount = isleCount;
    }
}
