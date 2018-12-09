package net.joedoe.logics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import static net.joedoe.utils.GameInfo.*;

public class Generator {
    private int height, width, islesCount;
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
        Coordinate start = new Coordinate(index % width, index / width);
        Isle initialIsle = createIsle(start);
        isles.add(initialIsle);
        LOGGER.info("Height: " + height + " Width: " + width + " Isles: " + islesCount + "\nRandom Index: " + index
                + " Initial Isle: " + initialIsle.toString() + "\nIndices Size: " + indices.size() + "\n");
        while (islesCount - isles.size() > 0) {
            Collections.shuffle(isles);
            for (Isle startIsle : isles) {
                if (startIsle.getBridges() == 8) continue;
                Isle endIsle = getEndIsle(startIsle);
                if (endIsle != null) {
                    isles.add(endIsle);
                    if (random.nextBoolean()) bridges.add(createBridge(endIsle, startIsle, true));
                    else bridges.add(createBridge(endIsle, startIsle, false));
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

    /*
     * Returns an isle to which a bridge can be build from the specified isle
     * 
     * @param isle - the isle from which a bridge shall be build
     */
    private Isle getEndIsle(Isle startIsle) {
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
                Coordinate end = new Coordinate(x, y);
                // collision detection for new isle & for new bridge
                if (indices.contains(end.getY() * width + end.getX()) && !collides(startIsle.getPos(), end)) {
                    Isle endIsle = createIsle(end);
                    LOGGER.info("Start Isle: " + startIsle.toString() + " Direction: " + direction.toString()
                            + " Distance: " + distance + "\nEnd Isle: " + endIsle.toString() + "\nMissing Isles: "
                            + islesCount + " Indices Size: " + indices.size() + "\n");
                    return endIsle;
                }
            }
        return null;
    }

    /*
     * Returns a list of possible directions for the specified isle
     * 
     * @param isle - the isle for which the directions need to be found
     */
    public List<Direction> getDirections(Isle startIsle) {
        List<Direction> directions = new ArrayList<>();
        if (startIsle.getY() > 1) directions.add(Direction.UP);
        if (startIsle.getX() > 1) directions.add(Direction.LEFT);
        if (startIsle.getY() < height - 2) directions.add(Direction.DOWN);
        if (startIsle.getX() < width - 2) directions.add(Direction.RIGHT);
        Collections.shuffle(directions);
        return directions;
    }

    /*
     * Returns a list of possible distances for the specified isle and direction
     * 
     * @param isle - the isle for which the directions need to be found
     * 
     * @param direction - the direction where to get the distances
     */
    public List<Integer> getDistances(Isle startIsle, Direction direction) {
        int max = 0;
        if (direction == Direction.UP) max = startIsle.getY();
        if (direction == Direction.LEFT) max = startIsle.getX();
        if (direction == Direction.DOWN) max = height - startIsle.getY() - 1;
        if (direction == Direction.RIGHT) max = width - startIsle.getX() - 1;
        List<Integer> distances = IntStream.range(2, max + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(distances);
        return distances;
    }

    private boolean collides(Coordinate start, Coordinate end) {
        if (start.compareTo(end) > 0) {
            return collidesIsles(end, start) || BridgeDetector.collides(end, start, bridges);
        }
        return collidesIsles(start, end) || BridgeDetector.collides(start, end, bridges);
    }

    public boolean collidesIsles(Coordinate start, Coordinate end) {
        return isles.stream().anyMatch(i -> i.getY() > start.getY() && i.getY() < end.getY() && i.getX() == start.getX()
                || i.getX() > start.getX() && i.getX() < end.getX() && i.getY() == start.getY());
    }

    public Isle createIsle(Coordinate pos) {
        Isle isle = new Isle(pos, 0);
        int index = pos.getY() * width + pos.getX();
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index + width));
        return isle;
    }

    private Bridge createBridge(Isle startIsle, Isle endIsle, boolean doubleBridge) {
        Bridge bridge;
        if (doubleBridge) {
            bridge = new Bridge(startIsle, endIsle, true);
            startIsle.increaseBridges(2);
            endIsle.increaseBridges(2);
        } else {
            bridge = new Bridge(startIsle, endIsle, false);
            startIsle.increaseBridges(1);
            endIsle.increaseBridges(1);
        }
        int startIndex = bridge.getStartY() * width + bridge.getStartX();
        int endIndex = bridge.getEndY() * width + bridge.getEndX();
        if (bridge.getAlignment() == Alignment.HORIZONTAL)
            IntStream.range(startIndex, endIndex).boxed().forEach(i -> indices.remove(i));
        else IntStream.range(startIndex, endIndex).filter(i -> i % width == bridge.getStartX()).boxed()
                .forEach(i -> indices.remove(i));
        return bridge;
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public List<Isle> getIsles() {
        return isles;
    }

    public Object[][] getFinalIsles() {
        Collections.sort(isles);
        Object[][] islesData = new Object[isles.size()][2];
        for (int i = 0; i < isles.size(); i++) {
            Isle isle = isles.get(i);
            islesData[i][0] = isle.getPos();
            islesData[i][1] = isle.getBridges();
        }
        return islesData;
    }

    @SuppressWarnings("unused")
    public Object[][] getFinalBridges() {
        Object[][] bridgesData = new Object[bridges.size()][3];
        for (int i = 0; i < bridges.size(); i++) {
            Bridge bridge = bridges.get(i);
            Isle startIsle = bridge.getStartIsle();
            Isle endIsle = bridge.getEndIsle();
            bridgesData[i][0] = startIsle.getPos();
            bridgesData[i][1] = endIsle.getPos();
            bridgesData[i][2] = bridge.isDoubleBridge();
        }
        return bridgesData;
    }

    public void setHeight() {
        height = random.nextInt((MAX_HEIGHT - MIN_HEIGHT) + 1) + MIN_HEIGHT;
    }

    public void setHeight(int height) {
        if (height < 4 || height > 25) {
            throw new IllegalArgumentException();
        }
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth() {
        width = random.nextInt((MAX_WIDTH - MIN_WIDTH) + 1) + MIN_WIDTH;
    }

    public void setWidth(int width) {
        if (width < MIN_WIDTH || width > MAX_WIDTH) {
            throw new IllegalArgumentException();
        }
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setIslesCount() {
        int maxIsles = (int) (0.2 * height * width);
        islesCount = random.nextInt((maxIsles - MIN_WIDTH) + 1) + MIN_WIDTH;
    }

    public void setIslesCount(int isleCount) {
        if (isleCount < 2 || isleCount > 0.2 * width * height) {
            throw new IllegalArgumentException();
        }
        this.islesCount = isleCount;
    }

    public int getIsleCount() {
        return islesCount;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
}
