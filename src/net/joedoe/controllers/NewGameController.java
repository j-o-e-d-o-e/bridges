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

    //todo: use more Collections.shuffle() instead of random.nextInt()
    //todo: to make program flow more deterministic

    public List<Isle> generateGame() {
        LOGGER.info("Height: " + height + " Width: " + width + " Isles: " + isleCount);
        int count = isleCount - 1;
        isles.add(generateInitialIsle());
        while (count > 0) {
            Isle startIsle = getRandomIsle();
            LOGGER.info("START ISLE: " + startIsle.toString());
            Direction direction = getRandomDirection(startIsle.getY(), startIsle.getX());
            int distance = getRandomDistance(startIsle, direction);
            Isle endIsle = createIsle(startIsle, direction, distance);
            if (endIsle == null) continue;
            isles.add(endIsle);
            count--;
            addBridge(startIsle, endIsle);
            if (random.nextBoolean()) {
                addBridge(endIsle, startIsle);
            }
        }
        Collections.sort(isles);
        return isles;
//        gridController.setSolution(bridges);
    }

    public Isle generateInitialIsle() {
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        int rand = random.nextInt(indices.size());
        int index = indices.get(rand);
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + width));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        int y = index / height;
        int x = index % height;
        return new Isle(y, x, 0);
    }

    private Isle getRandomIsle() {
//        Collections.shuffle(isles);
        int index = random.nextInt(isles.size());
        return isles.get(index);
    }

    public Direction getRandomDirection(int y, int x) {
        List<Direction> possibleDirections = new ArrayList<>();
        if (y >= 2)
            possibleDirections.add(Direction.UP);
        if (x >= 2)
            possibleDirections.add(Direction.LEFT);
        if (y <= height - 2)
            possibleDirections.add(Direction.DOWN);
        if (x <= width - 2)
            possibleDirections.add(Direction.RIGHT);
        int index = random.nextInt(possibleDirections.size());
        return possibleDirections.get(index);
    }

    private int getRandomDistance(Isle isle, Direction direction) {
        int min = 2;
        int max = 0;
        if (direction == Direction.UP) {
            max = isle.getY();
        }
        if (direction == Direction.LEFT) {
            max = isle.getX();
        }
        if (direction == Direction.DOWN) {
            max = height - isle.getY();
        }
        if (direction == Direction.RIGHT) {
            max = width - isle.getX();
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public Isle createIsle(Isle startIsle, Direction direction, int distance) {
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
        if (!indices.contains(index)) {
            System.out.println("ISLES SIZE: " + isles.size());
            return null;
        }
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + width));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        return new Isle(y, x, 0);
    }

    private void addBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = new Bridge(startIsle, endIsle);
        bridges.add(bridge);
        startIsle.addBridge(bridge);
        startIsle.increaseBridgeCount();
        endIsle.addBridge(bridge);
        endIsle.increaseBridgeCount();
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
