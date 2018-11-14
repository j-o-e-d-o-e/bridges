package tests.controllers;

import net.joedoe.controllers.NewGameController;
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

public class NewGameControllerTest {
    private NewGameController controller;
    private static final int HEIGHT = 25; //MIN: 2, MAX: 25
    private static final int WIDTH = 25;
    private final static Logger LOGGER = Logger.getLogger(NewGameControllerTest.class.getName());

    @Before
    public void setUp() {
        controller = new NewGameController();
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
        List<Isle> isles = controller.generateGame();

        //then
        assertEquals(initialIsleCount, isles.size());
//        isles.forEach(isle -> System.out.println(isle.toString()));
    }

    @Test
    public void getEndIsle() {
        //given
        List<Integer> indices = IntStream.range(0, HEIGHT * WIDTH).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        controller.addIsle(3, 3);
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
        Isle startIsle = new Isle(7, 7, 0);

        //when
        List<Direction> directions = controller.getDirections(startIsle);

        //then
        assertEquals(4, directions.size());
    }

    @Test
    public void getDirectionsBORDER() {
        //given
        int x = WIDTH - 2;
        Isle startIsle = new Isle(3, x, 0);

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
    public void collidesBridgesVERTICAL() {
        //given: vertical bridge
        Isle startIsle = new Isle(3, 3, 0);
        Isle endIsle = new Isle(6, 3, 0);
        controller.addBridge(startIsle, endIsle, false);

        //when: horizontal bridge
        boolean collides = controller.collidesBridges(4, 2, 4,5);

        //then
        assertTrue(collides);
    }

    @Test
    public void collidesBridgesHORIZONTAL() {
        //given: horizontal bridge
        Isle startIsle = new Isle(4, 2, 0);
        Isle endIsle = new Isle(4, 5, 0);
        controller.addBridge(startIsle, endIsle, false);

        //when: vertical bridge
        boolean collides = controller.collidesBridges(3, 3, 6,3);

        //then
        assertTrue(collides);
    }

    @Test
    public void addIsle() {
        //given
        List<Integer> indices = IntStream.range(0, HEIGHT * WIDTH).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        int expectedSize = indices.size() - 5;

        //when
        controller.addIsle(3, 3);

        //then
        assertEquals(expectedSize, controller.getIndices().size());
    }
}