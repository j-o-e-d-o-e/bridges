package tests.controllers;

import net.joedoe.controllers.GameGenerator;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameGeneratorTest {
    private GameGenerator controller;
    private static final int HEIGHT = 25; //MIN: 2, MAX: 25
    private static final int WIDTH = 25;
    private final static Logger LOGGER = Logger.getLogger(GameGeneratorTest.class.getName());

    @Before
    public void setUp() {
        controller = new GameGenerator();
        controller.setHeight(HEIGHT);
        controller.setWidth(WIDTH);
        int maxIsles = (int) (0.2 * HEIGHT * WIDTH);
        controller.setIsleCount(maxIsles);
        LOGGER.setLevel(Level.OFF);
    }

    @Test
    public void generateGame() {
        //given
        int initialIsleCount = controller.getIsleCount();

        //when
        controller.generateGame();

        //then
        assertEquals(initialIsleCount, controller.getIsles().size());
    }

    @Test
    public void getEndIsle() {
        //given
        List<Integer> indices = IntStream.range(0, HEIGHT * WIDTH).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        Isle isle = controller.createIsle(3, 3);
        controller.getIsles().add(isle);
        Isle startIsle = controller.getIsles().get(0);

        //when
        Isle endIsle = controller.getEndIsle(startIsle);

        //then
        LOGGER.info("End isle: " + endIsle.toString());
    }

    //basics:

    @Test
    public void getDirectionsMIDDLE() {
        //given
        Isle startIsle = new Isle(2, 2, 0);

        //when
        List<Direction> directions = controller.getDirections(startIsle);

        //then
        assertEquals(4, directions.size());
    }

    @Test
    public void getDirectionsBORDER() {
        //given
        Isle startIsle = new Isle(2, WIDTH - 2, 0);

        //when
        List<Direction> directions = controller.getDirections(startIsle);

        //then
        assertEquals(3, directions.size());
    }

    @Test
    public void getDirectionsCORNER() {
        //given
        int y = HEIGHT - 2;
        int x = WIDTH - 2;
        Isle startIsle = new Isle(y, x, 0);

        //when
        List<Direction> directions = controller.getDirections(startIsle);

        //then
        assertEquals(2, directions.size());
    }

    @Test
    public void getDistancesUP() {
        //given
        Isle startIsle = new Isle(9, 0, 0);
        Direction direction = Direction.UP;
        int expectedSize = startIsle.getY() - 1;
        if (expectedSize < 0) expectedSize = 0;

        //when
        List<Integer> distances = controller.getDistances(startIsle, direction);

        //then
        assertEquals(expectedSize, distances.size());
    }

    @Test
    public void getDistancesLEFT() {
        //given
        Isle startIsle = new Isle(9, 9, 0);
        Direction direction = Direction.LEFT;
        int expectedSize = startIsle.getX() - 1;
        if (expectedSize < 0) expectedSize = 0;

        //when
        List<Integer> distances = controller.getDistances(startIsle, direction);

        //then
        assertEquals(expectedSize, distances.size());
    }

    @Test
    public void getDistancesDOWN() {
        //given
        Isle startIsle = new Isle(7, 3, 0);
        Direction direction = Direction.DOWN;
        int expectedSize = HEIGHT - startIsle.getY() - 2;
        if (expectedSize < 0) expectedSize = 0;

        //when
        List<Integer> distances = controller.getDistances(startIsle, direction);

        //then
        assertEquals(expectedSize, distances.size());
    }

    @Test
    public void getDistancesRIGHT() {
        //given
        Isle startIsle = new Isle(8, 6, 0);
        Direction direction = Direction.RIGHT;
        int expectedSize = WIDTH - startIsle.getX() - 2;
        if (expectedSize < 0) expectedSize = 0;

        //when
        List<Integer> distances = controller.getDistances(startIsle, direction);

        //then
        assertEquals(expectedSize, distances.size());
    }

    @Test
    public void collidesIslesVERTICAL() {
        //given: vertical connection
        Isle startIsle = new Isle(3, 3, 0);
        Isle endIsle = new Isle(6, 3, 0);
        Isle isle = controller.createIsle(4, 3);
        controller.getIsles().add(isle);

        //when
        boolean collides = controller.collidesIsles(startIsle.getY(), startIsle.getX(), endIsle.getY(), endIsle.getX());

        //then
        assertTrue(collides);
    }

    @Test
    public void collidesIslesHORIZONTAL() {
        //given: horizontal connection
        Isle startIsle = new Isle(4, 2, 0);
        Isle endIsle = new Isle(4, 5, 0);
        Isle isle = controller.createIsle(4, 4);
        controller.getIsles().add(isle);

        //when
        boolean collides = controller.collidesIsles(startIsle.getY(), startIsle.getX(), endIsle.getY(), endIsle.getX());

        //then
        assertTrue(collides);
    }

    @Test
    public void collidesBridgesVERTICAL() {
        //given: vertical bridge
        Isle startIsle = new Isle(3, 3, 0);
        Isle endIsle = new Isle(6, 3, 0);
        Bridge bridge = controller.createBridge(startIsle, endIsle);
        controller.getBridges().add(bridge);

        //when: horizontal bridge
        boolean collides = controller.collidesBridges(4, 2, 4, 5);

        //then
        assertTrue(collides);
    }

    @Test
    public void collidesBridgesHORIZONTAL() {
        //given: horizontal bridge
        Isle startIsle = new Isle(4, 2, 0);
        Isle endIsle = new Isle(4, 5, 0);
        Bridge bridge = controller.createBridge(startIsle, endIsle);
        controller.getBridges().add(bridge);

        //when: vertical bridge
        boolean collides = controller.collidesBridges(3, 3, 6, 3);

        //then
        assertTrue(collides);
    }

    @Test
    public void createIsle() {
        //given
        List<Integer> indices = IntStream.range(0, HEIGHT * WIDTH).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        int expectedSize = indices.size() - 5;

        //when
        controller.createIsle(3, 3);

        //then
        assertEquals(expectedSize, controller.getIndices().size());
    }

    @Test
    public void createBridgeHORIZONTAL() {
        //given
        int startX = 3;
        int endX = 6;
        List<Integer> indices = IntStream.range(0, HEIGHT * WIDTH).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        int expectedSize = indices.size() - Math.abs(endX - startX);
        Isle startIsle = new Isle(3, startX, 0);
        Isle endIsle = new Isle(3, endX, 0);

        //when
        controller.createBridge(startIsle, endIsle);

        //then
        assertEquals(expectedSize, controller.getIndices().size());
    }

    @Test
    public void createBridgeVERTICAL() {
        //given
        int startY = 0;
        int endY= 5;
        List<Integer> indices = IntStream.range(0, HEIGHT * WIDTH).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        int expectedSize = indices.size() - Math.abs(endY - startY);
        Isle startIsle = new Isle(startY, 3, 0);
        Isle endIsle = new Isle(endY, 3, 0);

        //when
        controller.createBridge(startIsle, endIsle);

        //then
        assertEquals(expectedSize, controller.getIndices().size());
    }
}