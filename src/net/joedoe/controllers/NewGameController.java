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
        height = (int) (Math.random() * MAX) + MIN;
        width = (int) (Math.random() * MAX) + MIN;
        isleCount = (int) (Math.random() * (0.2 * height * width)) + MIN;
        return generateGame();
    }

    public List<Isle> createGame(int height, int width) {
        this.height = height;
        this.width = width;
        this.isleCount = (int) (Math.random() * (0.2 * height * width)) + MIN;
        return generateGame();
    }

    public List<Isle> createGame(int height, int width, int isleCount) {
        this.height = height;
        this.width = width;
        this.isleCount = isleCount;
        return generateGame();
    }

    public List<Isle> generateGame() {
        LOGGER.info("Height: " + height + " Width: " + width + " Isles: " + isleCount);
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        int count = isleCount - 1;
        generateIsle();
        while (count > 0) {
            Isle startIsle = getRandomIsle();
            LOGGER.info("START ISLE: " + startIsle.toString());
            Direction direction = getRandomDirection(startIsle.getY(), startIsle.getX());
            int distance = getRandomDistance(startIsle, direction);
            Isle endIsle = createIsle(startIsle, direction, distance);
            if (endIsle == null) continue;
            isles.add(endIsle);
            count--;
            if (random.nextBoolean()) {
                Bridge bridge = new Bridge(startIsle, endIsle);
                bridges.add(bridge);
                startIsle.addBridge(bridge);
                endIsle.addBridge(bridge);
                bridge = new Bridge(endIsle, startIsle);
                bridges.add(bridge);
                startIsle.addBridge(bridge);
                endIsle.addBridge(bridge);
                startIsle.increaseBridgeCount();
                startIsle.increaseBridgeCount();
                endIsle.increaseBridgeCount();
                endIsle.increaseBridgeCount();
            } else {
                Bridge bridge = new Bridge(startIsle, endIsle);
                bridges.add(bridge);
                startIsle.addBridge(bridge);
                endIsle.addBridge(bridge);
                startIsle.increaseBridgeCount();
                endIsle.increaseBridgeCount();
            }
        }

        Collections.sort(isles);
        return isles;
//        gridController.setSolution(bridges);
    }

    private Isle getRandomIsle() {
        int index = (int) (Math.random() * isles.size());
        return isles.get(index);
    }

    public Direction getRandomDirection(int y, int x) {
        List<Direction> possibleDirections = new ArrayList<>();
        if (y > 1)
            possibleDirections.add(Direction.UP);
        if (x > 1)
            possibleDirections.add(Direction.LEFT);
        if (y < height - 1)
            possibleDirections.add(Direction.DOWN);
        if (x < width - 1)
            possibleDirections.add(Direction.RIGHT);
        int index = (int) (Math.random() * possibleDirections.size());
        return possibleDirections.get(index);
    }

    private int getRandomDistance(Isle isle, Direction direction) {
        if (direction == Direction.UP)
            return (int) (Math.random() * (isle.getY() - 2));
        else if (direction == Direction.LEFT)
            return (int) (Math.random() * (isle.getX() - 2));
        else if (direction == Direction.DOWN)
            return (int) (Math.random() * height) + isle.getY() + 2;
        else
            return (int) (Math.random() * width) + isle.getX() + 2;
    }

    private Isle createIsle(Isle startIsle, Direction direction, int distance) {
        int y = 0;
        int x = 0;
        if (direction == Direction.UP)
            y = startIsle.getY() - distance;
        if (direction == Direction.LEFT)
            x = startIsle.getX() - distance;
        if (direction == Direction.DOWN)
            y = startIsle.getY() + distance;
        else
            x = startIsle.getX() + distance;
        int index = y + x;
        if (!indices.contains(index)) return null;
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + height));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index - height));
        indices.remove(new Integer(index - 1));
        return new Isle(y, x, 0);

    }

    private void generateIsle() {
        int rand = (int) (Math.random() * indices.size());
        int index = indices.get(rand);
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + height));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index - height));
        indices.remove(new Integer(index - 1));
        int y = index % height;
        int x = index / height;
        Isle isle = new Isle(y, x, 0);
        isles.add(isle);
//        LOGGER.info("Generated Index: " + index);
//        LOGGER.info("Generated Isle: " + isle.toString());
    }

    @SuppressWarnings("unused")
    private void generateBridge() {
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
}
