package tests.controllers;

import net.joedoe.controllers.GridController;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GridControllerTest {
    private GridController gridController;

    @Before
    public void setUp() {
        gridController = new GridController();
    }

    @Test
    public void addBridge() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = gridController.getIsle(0, 0);
        Isle endIsle = gridController.findIsle(startIsle, direction);

        //when
        gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);
        gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);
//        gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);

        //then
        assertEquals(2, startIsle.getBridges().size());
        assertEquals(2, endIsle.getBridges().size());
        Bridge startBridge = startIsle.getBridge(endIsle, false);
        Bridge endBridge = endIsle.getBridge(startIsle, true);
        assertEquals(startBridge, endBridge);
    }

    @Test
    public void addBridgeFromEnd() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = gridController.getIsle(0, 0);
        Isle endIsle = gridController.findIsle(startIsle, direction);

        //when
        gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);
        gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);
//        gridController.addBridge(endIsle.getY(), endIsle.getX(), direction.getOpposite());

        //then
        assertEquals(2, startIsle.getBridges().size());
        assertEquals(2, endIsle.getBridges().size());
        assertEquals(startIsle.getBridge(endIsle, false),
                endIsle.getBridge(startIsle, true));
        assertEquals(startIsle.getBridge(endIsle, true),
                endIsle.getBridge(startIsle, false));
    }

    @Test
    public void removeBridge() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = gridController.getIsle(0, 0);
        Isle endIsle = gridController.findIsle(startIsle, direction);

        //when
//        Bridge added = gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);
//        Bridge removed = gridController.removeBridge(startIsle.getY(), startIsle.getX(), direction);
//        Bridge removed = gridController.removeBridge(endIsle.getY(), endIsle.getX(), direction.getOpposite());

        //then
//        assertEquals(added, removed);
    }

    @Test
    public void removeBridgeFromEnd() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = gridController.getIsle(0, 0);
        Isle endIsle = gridController.getIsle(3, 0);
//        Bridge added = gridController.addBridge(startIsle.getY(), startIsle.getX(), direction);

        //when
//        Bridge removed = gridController.removeBridge(endIsle.getY(), endIsle.getX(), direction.getOpposite());

        //then
//        assertEquals(added, removed);
        assertEquals(0, startIsle.getBridges().size());
        assertEquals(0, endIsle.getBridges().size());
    }

    @Test
    public void findIsleUP() {
        //given
        Isle startIsle = gridController.getIsle(5, 3);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.UP);

        //then
        assertEquals(startIsle.getX(), endIsle.getX());
        assertEquals(3, endIsle.getY());
    }

    @Test
    public void findIsleLEFT() {
        //given
        Isle startIsle = gridController.getIsle(6, 6);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.LEFT);

        //then
        assertEquals(startIsle.getY(), endIsle.getY());
        assertEquals(4, endIsle.getX());
    }

    @Test
    public void findIsleDOWN() {
        //given
        Isle startIsle = gridController.getIsle(1, 3);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.DOWN);

        //then
        assertEquals(startIsle.getX(), endIsle.getX());
        assertEquals(3, endIsle.getY());
    }

    @Test
    public void findIsleRIGHT() {
        //given
        Isle startIsle = gridController.getIsle(6, 1);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.RIGHT);

        //then
        assertEquals(startIsle.getY(), endIsle.getY());
        assertEquals(4, endIsle.getX());
    }

    @Test
    public void collidesHORIZONTAL() {
        //given
        Bridge vertical = new Bridge(new Isle(6, 1, 1), new Isle(1, 1, 1));
        List<Bridge> bridges = gridController.getBridges();
        bridges.add(vertical);

        //when
        Bridge horizontal = new Bridge(new Isle(3, 0, 1), new Isle(3, 3, 1));
        boolean collides = gridController.collides(horizontal);

        //then
        assertTrue(collides);
    }

    @Test
    public void collidesVERTICAL() {
        //given
        Bridge horizontal = new Bridge(new Isle(3, 0, 1), new Isle(3, 3, 1));
        List<Bridge> bridges = gridController.getBridges();
        bridges.add(horizontal);

        //when
        Bridge vertical = new Bridge(new Isle(6, 1, 1), new Isle(1, 1, 1));
        boolean collides = gridController.collides(vertical);

        //then
        assertTrue(collides);
    }
}