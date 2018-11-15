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
    private static final int MAX = 25;
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

    public List<Isle> generateGame() {
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        int index = random.nextInt(indices.size());
        Isle initialIsle = createIsle(index / width, index % width);
        isles.add(initialIsle);
        int initialIsleCount = isleCount;
        isleCount--;
        LOGGER.info("Height: " + height
                + " Width: " + width
                + " Initial Isle: " + initialIsle.toString()
                + "\nCurrent Isle Count: " + isleCount
                + " Indices Size: " + indices.size()
                + "\n");
        while (isleCount > 0) {
            Collections.shuffle(isles);
            for (Isle startIsle : isles) {
                Isle endIsle = getEndIsle(startIsle);
                if (endIsle != null) {
                    isles.add(endIsle);
                    isleCount--;
                    bridges.add(createBridge(startIsle, endIsle));
                    if (random.nextBoolean())
                        bridges.add(createBridge(endIsle, startIsle));
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
                //neighbouring check, isle and bridge collision detection
                if (indices.contains(y * height + x) && !collides(startIsle.getY(), startIsle.getX(), y, x)) {
                    Isle endIsle = createIsle(y, x);
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

    private boolean collides(int startY, int startX, int endY, int endX) {
        return collidesIsles(startY, startX, endY, endX) || collidesBridges(startY, startX, endY, endX);
    }

    public boolean collidesIsles(int startY, int startX, int endY, int endX) {
        return isles.stream().anyMatch(i -> i.getY() > startY && i.getY() < endY && i.getX() == startX
                || i.getX() > startX && i.getX() < endX && i.getY() == startY);
    }

    public boolean collidesBridges(int startY, int startX, int endY, int endX) {
        if (Alignment.getAlignment(startY, endY) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < startY && b.getEndY() > startY
                    && startX < b.getStartX() && endX > b.getStartX());
        else
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < startX && b.getEndX() > startX
                    && startY < b.getStartY() && endY > b.getStartY());
    }

    public Isle createIsle(int y, int x) {
        Isle isle = new Isle(y, x, 0);
        int index = y * height + x;
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index + width));
        return isle;
    }

    public Bridge createBridge(Isle startIsle, Isle endIsle) {
        Bridge bridge = new Bridge(startIsle, endIsle);
        startIsle.addBridge(bridge);
        startIsle.increaseBridgeCount();
        endIsle.addBridge(bridge);
        endIsle.increaseBridgeCount();
        bridges.add(bridge);
        //for testing
        int indicesSizeOld = indices.size();
        int startY = bridge.getStartY() * height;
        int endY = bridge.getEndY() * height;
        if (bridge.getAlignment() == Alignment.HORIZONTAL)
            indices.removeAll(IntStream.range(startY + bridge.getStartX(), endY + bridge.getEndX())
                    .boxed().collect(Collectors.toList()));
        else
            indices.removeAll(IntStream.range(startY, endY).filter(i -> i % height == bridge.getStartX())
                    .boxed().collect(Collectors.toList()));
        LOGGER.info("Indices size before/after: " + indicesSizeOld + "/" + indices.size());
        return bridge;
    }

    @SuppressWarnings("unused")
    public void setHeight() {
        height = random.nextInt((MAX - MIN) + 1) + MIN;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @SuppressWarnings("unused")
    public void setWidth() {
        width = random.nextInt((MAX - MIN) + 1) + MIN;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
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

    public List<Isle> getIsles() {
        return isles;
    }

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
