package net.joedoe.controllers;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
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
    private List<Integer> indices = new ArrayList<>();
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();
    private Random random = new Random();

    private final static Logger LOGGER = Logger.getLogger(NewGameController.class.getName());

    public NewGameController() {
//        LOGGER.setLevel(Level.OFF);
    }

    @SuppressWarnings("WeakerAccess")
    public List<Isle> generateGame() {
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        int rand = random.nextInt(indices.size());
        int index = indices.get(rand);
        Isle initialIsle = addIsle(index / width, index % width);
        LOGGER.info("Height: " + height
                + " Width: " + width
                + " Initial Isle: " + initialIsle.toString()
                + "\nCurrent Isle Count: " + isleCount
                + " Indices Size: " + indices.size()
                + "\n");
        int initialIsleCount = isleCount;
        while (isleCount > 0) {
            Collections.shuffle(isles);
            for (Isle startIsle : isles) {
                Isle endIsle = getEndIsle(startIsle);
                if (endIsle != null) {
                    addBridge(startIsle, endIsle, random.nextBoolean());
                    break;
                }
                if (startIsle == isles.get(isles.size() - 1)) {
                    isleCount = initialIsleCount;
                    isles.clear();
                    bridges.clear();
                    return generateGame();
                }
            }
        }
        Collections.sort(isles);
        return isles;
//      TODO: return solution with bridges;
    }

    public Isle getEndIsle(Isle startIsle) {
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
                //check neighbouring and collision condition
                if (indices.contains(y + x) && !collidesBridges(startIsle.getY(), startIsle.getX(), y)) {
//                if (indices.contains(y + x)) {
                    Isle endIsle = addIsle(y, x);
                    LOGGER.info("Start Isle: " + startIsle.toString()
                            + " Direction: " + direction.toString()
                            + " Distance: " + distance
                            + " End Isle: " + endIsle.toString()
                            + "\nCurrent Isle Count: " + isleCount
                            + " Indices Size: " + indices.size()
                            + "\n");
                    return endIsle;
                }
            }
        }
        return null;
    }

    public List<Direction> getDirections(Isle startIsle) {
        List<Direction> directions = new ArrayList<>();
        if (startIsle.getY() > 1)
            directions.add(Direction.UP);
        if (startIsle.getX() > 1)
            directions.add(Direction.LEFT);
        if (startIsle.getY() < height - 2)
            directions.add(Direction.DOWN);
        if (startIsle.getX() < width - 2)
            directions.add(Direction.RIGHT);
        Collections.shuffle(directions);
        return directions;
    }

    public List<Integer> getDistances(Isle startIsle, Direction direction) {
        int max = 0;
        if (direction == Direction.UP)
            max = startIsle.getY();
        if (direction == Direction.LEFT)
            max = startIsle.getX();
        if (direction == Direction.DOWN)
            max = height - startIsle.getY() - 1;
        if (direction == Direction.RIGHT)
            max = width - startIsle.getX() - 1;
        List<Integer> distances = IntStream.range(2, max + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(distances);
        return distances;
    }

    public boolean collidesBridges(int startY, int startX, int endY) {
        if (Alignment.getAlignment(startY, endY) == Alignment.HORIZONTAL) {
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < startY && b.getEndY() > startY
                    && startX < b.getStartX() && startX > b.getStartX());
        } else {
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < startX && b.getEndX() > startX
                    && startY < b.getStartY() && endY > b.getStartY());
        }
    }

    @SuppressWarnings("unused")
    public boolean collidesIsles(int startY, int startX, int endY) {
        return true;
    }

    public Isle addIsle(int y, int x) {
        Isle isle = new Isle(y, x, 0);
        isles.add(isle);
        isleCount--;
        int index = y * height + x;
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index + width));
        return isle;
    }

    public void addBridge(Isle startIsle, Isle endIsle, boolean doubleBridge) {
        Bridge bridge = new Bridge(startIsle, endIsle);
        startIsle.addBridge(bridge);
        startIsle.increaseBridgeCount();
        endIsle.addBridge(bridge);
        endIsle.increaseBridgeCount();
        bridges.add(bridge);
        if (doubleBridge)
            addBridge(endIsle, startIsle, false);
    }

    public void setHeight() {
        height = random.nextInt((MAX - MIN) + 1) + MIN;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void setWidth() {
        width = random.nextInt((MAX - MIN) + 1) + MIN;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setIsleCount() {
        int maxIsles = (int) (0.2 * height * width);
        isleCount = random.nextInt((maxIsles - MIN) + 1) + MIN;
    }

    public void setIsleCount(int isleCount) {
        this.isleCount = isleCount;
    }

    public int getIsleCount() {
        return isleCount;
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

    @SuppressWarnings("unused")
    public List<Bridge> getBridges() {
        return bridges;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<Integer> getIndices() {
        return indices;
    }
}
