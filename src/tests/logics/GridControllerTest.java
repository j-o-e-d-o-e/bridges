package tests.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GridControllerTest {
    private GridController controller;

    @Before
    public void setUp() {
        controller = new GridController();
        controller.setIsles(Mocks.ISLES);
    }

    @Test
    public void addBridge() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = controller.getIsle(0, 0);
        Isle endIsle = controller.getEndIsle(startIsle, direction);

        //when
        controller.addBridge(startIsle.getPos(), direction);
        controller.addBridge(startIsle.getPos(), direction);

        //then
        assertEquals(2, startIsle.getBridges().size());
        assertEquals(2, endIsle.getBridges().size());
    }

    @Test
    public void removeBridge() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = controller.getIsle(0, 0);
        Coordinate[] added = controller.addBridge(startIsle.getPos(), direction);

        //when
        Coordinate[] removed = controller.removeBridge(startIsle.getPos(), direction);

        //then
        assertEquals(added[0].getY(), removed[0].getY());
        assertEquals(added[0].getX(), removed[0].getX());
        assertEquals(added[1].getY(), removed[1].getY());
        assertEquals(added[1].getX(), removed[1].getX());
    }

    @Test
    public void findIsleUP() {
        //given
        Isle startIsle = controller.getIsle(3, 5);

        //when
        Isle endIsle = controller.getEndIsle(startIsle, Direction.UP);

        //then
        assertEquals(startIsle.getX(), endIsle.getX());
        assertEquals(3, endIsle.getY());
    }

    @Test
    public void findIsleLEFT() {
        //given
        Isle startIsle = controller.getIsle(6, 6);

        //when
        Isle endIsle = controller.getEndIsle(startIsle, Direction.LEFT);

        //then
        assertEquals(startIsle.getY(), endIsle.getY());
        assertEquals(4, endIsle.getX());
    }

    @Test
    public void findIsleDOWN() {
        //given
        Isle startIsle = controller.getIsle(3, 1);

        //when
        Isle endIsle = controller.getEndIsle(startIsle, Direction.DOWN);

        //then
        assertEquals(startIsle.getX(), endIsle.getX());
        assertEquals(3, endIsle.getY());
    }

    @Test
    public void findIsleRIGHT() {
        //given
        Isle startIsle = controller.getIsle(1, 6);

        //when
        Isle endIsle = controller.getEndIsle(startIsle, Direction.RIGHT);

        //then
        assertEquals(startIsle.getY(), endIsle.getY());
        assertEquals(4, endIsle.getX());
    }

    @Test
    public void collidesHORIZONTAL() {
        //given
        Isle startIsle = new Isle(1, 6, 1);
        Isle endIsle = new Isle(1, 1, 1);
        List<Bridge> bridges = controller.getBridges();
        bridges.add(new Bridge(startIsle, endIsle));
        Coordinate start = new Coordinate(0, 3);
        Coordinate end = new Coordinate(3, 3);

        //when
        boolean collides = controller.collides(start, end);

        //then
        assertTrue(collides);
    }

    @Test
    public void collidesVERTICAL() {
        //given
        Isle startIsle = new Isle(0, 3, 1);
        Isle endIsle = new Isle(3, 3, 1);
        List<Bridge> bridges = controller.getBridges();
        bridges.add(new Bridge(startIsle, endIsle));
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(1, 6);

        //when
        boolean collides = controller.collides(start, end);

        //then
        assertTrue(collides);
    }
}