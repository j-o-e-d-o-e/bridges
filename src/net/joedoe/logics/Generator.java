package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Converter;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    private static final int MAX = 25;
    private static final int MIN = 2;
    private int height, width, isleCount;
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Random random = new Random();

    private final static Logger LOGGER = Logger.getLogger(Generator.class.getName());

    public Generator() {
        LOGGER.setLevel(Level.OFF);
    }

    public void generateGame() {
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        int index = random.nextInt(indices.size());
        Isle initialIsle = createIsle(index % width, index / width);
        isles.add(initialIsle);
        LOGGER.info("Height: " + height
                + " Width: " + width
                + " Isles: " + isleCount
                + "\nRandom Index: " + index
                + " Initial Isle: " + initialIsle.toString()
                + "\nIndices Size: " + indices.size()
                + "\n");
        while (isleCount - isles.size() > 0) {
            Collections.shuffle(isles);
            for (Isle startIsle : isles) {
                if (startIsle.getBridgeCount() == 8) continue;
                Isle endIsle = getEndIsle(startIsle);
                if (endIsle != null) {
                    isles.add(endIsle);
                    bridges.add(createBridge(startIsle, endIsle));
                    if (random.nextBoolean() && startIsle.getBridgeCount() < 8)
                        bridges.add(createBridge(endIsle, startIsle));
                    break;
                }
                if (startIsle == isles.get(isles.size() - 1)) {
                    isles.clear();
                    bridges.clear();
                    generateGame();
                    return;
                }
            }
        }
    }

    /*Returns an isle to which a bridge can be build from the specified isle
     * @param isle - the isle from which a bridge shall be build
     * */
    public Isle getEndIsle(Isle startIsle) {
        for (Direction direction : getDirections(startIsle))
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
                //collision detection for new isle & for new bridge
                if (indices.contains(y * width + x) && !collides(startIsle.getPos(), new Coordinate(x, y))) {
                    Isle endIsle = createIsle(x, y);
                    LOGGER.info("Start Isle: " + startIsle.toString()
                            + " Direction: " + direction.toString()
                            + " Distance: " + distance
                            + "\nEnd Isle: " + endIsle.toString()
                            + "\nMissing Isles: " + isleCount
                            + " Indices Size: " + indices.size()
                            + "\n");
                    return endIsle;
                }
            }
        return null;
    }

    /*Returns a list of possible directions for the specified isle
     * @param isle - the isle for which the directions need to be found
     * */
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

    /*Returns a list of possible distances for the specified isle and direction
     * @param isle - the isle for which the directions need to be found
     * @param direction - the direction where to get the distances
     * */
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

    private boolean collides(Coordinate start, Coordinate end) {
        if (start.compareTo(end) > 0) {
            return collidesIsles(end, start) || collidesBridges(end, start);
        }
        return collidesIsles(start, end) || collidesBridges(start, end);
    }

    public boolean collidesIsles(Coordinate start, Coordinate end) {
        return isles.stream().anyMatch(i -> i.getY() > start.getY() && i.getY() < end.getY() && i.getX() == start.getX()
                || i.getX() > start.getX() && i.getX() < end.getX() && i.getY() == start.getY());
    }

    public boolean collidesBridges(Coordinate start, Coordinate end) {
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL
                    && b.getStartY() < start.getY() && b.getEndY() > start.getY()
                    && start.getX() < b.getStartX() && end.getX() > b.getStartX());
        else
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL
                    && b.getStartX() < start.getX() && b.getEndX() > start.getX()
                    && start.getY() < b.getStartY() && end.getY() > b.getStartY());
    }

    public Isle createIsle(int x, int y) {
        Isle isle = new Isle(x, y, 0);
        int index = y * width + x;
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
        int startIndex = bridge.getStartY() * width + bridge.getStartX();
        int endIndex = bridge.getEndY() * width + bridge.getEndX();
        if (bridge.getAlignment() == Alignment.HORIZONTAL)
            IntStream.range(startIndex, endIndex).boxed().forEach(i -> indices.remove(i));
        else
            IntStream.range(startIndex, endIndex).filter(i -> i % width == bridge.getStartX())
                    .boxed().forEach(i -> indices.remove(i));
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

    public Object[][] getFinalIsles() {
        Object[][] finalIsles = new Object[isles.size()][2];
        for (int i = 0; i < isles.size(); i++) {
            Isle isle = isles.get(i);
            finalIsles[i] = new Object[]{isle.getPos(), isle.getBridgeCount()};
        }
        return finalIsles;
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public Coordinate[][] getFinalBridges() {
        Coordinate[][] finalBridges = new Coordinate[bridges.size()][2];
        for (int i = 0; i < bridges.size(); i++)
            finalBridges[i] = Converter.convertBridgeToData(bridges.get(i));
        return finalBridges;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<Integer> getIndices() {
        return indices;
    }
}
