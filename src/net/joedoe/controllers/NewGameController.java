package net.joedoe.controllers;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NewGameController {
    private static final int MAX = 10; // 25
    private static final int MIN = 2;
    private int height, width;
    private int isleCount;
    //    private GridController gridController;
    private List<Integer> indices;
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();
    private Random random;

    private final static Logger LOGGER = Logger.getLogger(NewGameController.class.getName());

    public NewGameController() {
        this.random = new Random();
//        gridController = new GridController();
//        LOGGER.setLevel(Level.OFF);
    }

    public List<Isle> createGame() {
        height = random.nextInt((MAX - MIN) + 1) + MIN;
        width = random.nextInt((MAX - MIN) + 1) + MIN;
        int maxIsles = (int) (0.2 * height * width);
        isleCount = random.nextInt((maxIsles - MIN) + 1) + MIN;
        return generateGame();
    }

    public List<Isle> createGame(int height, int width) {
        this.height = height - 1;
        this.width = width - 1;
        int maxIsles = (int) (0.2 * height * width);
        isleCount = random.nextInt((maxIsles - MIN) + 1) + MIN;
        return generateGame();
    }

    public List<Isle> createGame(int height, int width, int isleCount) {
        this.height = height - 1;
        this.width = width - 1;
        this.isleCount = isleCount;
        return generateGame();
    }

    @SuppressWarnings("WeakerAccess")
    public List<Isle> generateGame() {
        LOGGER.info("Height: " + height + " Width: " + width + " Isles: " + isleCount);
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        int rand = random.nextInt(indices.size());
        addIsle(indices.get(rand));
        while (isleCount > 0) {
            Collections.shuffle(isles);
            for (Isle startIsle : isles) {
                LOGGER.info("START ISLE: " + startIsle.toString());
                Isle endIsle = getEndIsle(startIsle);
                addIsle(endIsle.getY() + endIsle.getX());
                if (endIsle != null) {
                    addBridge(startIsle, endIsle, random.nextBoolean());
                    break;
                }
            }
        }
        Collections.sort(isles);
        return isles;
//        gridController.setSolution(bridges);
    }

    private Isle getEndIsle(Isle startIsle) {
        for (Direction direction : getDirections(startIsle)) {
            for (int distance : getDistances(startIsle, direction)) {
                int y = 0;
                int x = 0;
                if (direction == Direction.UP) {
                    y = startIsle.getY() - distance;
                    x = startIsle.getX();
                }
                if (direction == Direction.LEFT) {
                    y = startIsle.getY();
                    x = startIsle.getX() - distance;
                }
                if (direction == Direction.DOWN) {
                    y = startIsle.getY() + distance;
                    x = startIsle.getX();
                }
                if (direction == Direction.RIGHT) {
                    y = startIsle.getY();
                    x = startIsle.getX() + distance;
                }
                int index = y + x;
                if (indices.contains(index)) {
                    return new Isle(index / height, index % height, 0);
                }
            }
        }
        return null;
    }

    private List<Direction> getDirections(Isle startIsle) {
        List<Direction> directions = new ArrayList<>();
        if (startIsle.getY() > 1)
            directions.add(Direction.UP);
        if (startIsle.getX() > 1)
            directions.add(Direction.LEFT);
        if (startIsle.getY() < height - 1)
            directions.add(Direction.DOWN);
        if (startIsle.getX() < width - 1)
            directions.add(Direction.RIGHT);
        Collections.shuffle(directions);
        return directions;
    }

    private List<Integer> getDistances(Isle startIsle, Direction direction) {
        int max = 0;
        if (direction == Direction.UP)
            max = startIsle.getY();
        if (direction == Direction.LEFT)
            max = startIsle.getX();
        if (direction == Direction.DOWN)
            max = height - startIsle.getY();
        if (direction == Direction.RIGHT)
            max = width - startIsle.getX();
        List<Integer> distances = IntStream.range(2, max).boxed().collect(Collectors.toList());
        Collections.shuffle(distances);
        return distances;
    }

    private void addIsle(int index) {
        LOGGER.info("ISLES SIZE: " + isles.size());
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + width));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        Isle isle = new Isle(index / height, index % height, 0);
        isles.add(isle);
        isleCount--;
    }

    private void addBridge(Isle startIsle, Isle endIsle, boolean doubleBridge) {
        Bridge bridge = new Bridge(startIsle, endIsle);
        bridges.add(bridge);
        startIsle.addBridge(bridge);
        startIsle.increaseBridgeCount();
        endIsle.addBridge(bridge);
        endIsle.increaseBridgeCount();
        if (doubleBridge)
            addBridge(endIsle, startIsle, false);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setIsleCount(int isleCount) {
        this.isleCount = isleCount;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Isle> getIsles() {
        return isles;
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public int getIsleCount() {
        return isleCount;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
}
