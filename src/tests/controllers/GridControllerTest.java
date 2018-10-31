package tests.controllers;

import net.joedoe.controllers.GridController;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

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
        gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);
        gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);
//        gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);

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
        gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);
        gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);
//        gridController.addBridge(endIsle.getRow(), endIsle.getColumn(), direction.getOpposite());

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
        Bridge added = gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);
//        Bridge removed = gridController.removeBridge(startIsle.getRow(), startIsle.getColumn(), direction);
        Bridge removed = gridController.removeBridge(endIsle.getRow(), endIsle.getColumn(), direction.getOpposite());

        //then
        assertEquals(added, removed);
    }

    @Test
    public void removeBridgeFromEnd() {
        //given
        Direction direction = Direction.DOWN;
        Isle startIsle = gridController.getIsle(0, 0);
        startIsle.getBridges().clear();
        Isle endIsle = gridController.getIsle(3, 0);
        endIsle.getBridges().clear();
        Bridge added = gridController.addBridge(startIsle.getRow(), startIsle.getColumn(), direction);

        //when
        Bridge removed = gridController.removeBridge(endIsle.getRow(), endIsle.getColumn(), direction.getOpposite());

        //then
        assertEquals(added, removed);
        assertEquals(0, startIsle.getBridges().size());
        assertEquals(0, endIsle.getBridges().size());
    }

//    @Test
//    public void collides() {
//        //given
//        gridController.addBridge(3, 0, Direction.RIGHT);
//        Bridge newBridge = new Bridge(gridController.getIsle(6, 1), gridController.getIsle(1, 1));
//
//        //when
//        boolean collides = gridController.collides(newBridge);
//
//        //then
//        assertTrue(collides);
//    }

    @Test
    public void findIsleUP() {
        //given
        Isle startIsle = gridController.getIsle(5, 3);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.UP);

        //then
        assertEquals(startIsle.getColumn(), endIsle.getColumn());
        assertEquals(3, endIsle.getRow());
    }

    @Test
    public void findIsleLEFT() {
        //given
        Isle startIsle = gridController.getIsle(6, 6);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.LEFT);

        //then
        assertEquals(startIsle.getRow(), endIsle.getRow());
        assertEquals(4, endIsle.getColumn());
    }

    @Test
    public void findIsleDOWN() {
        //given
        Isle startIsle = gridController.getIsle(1, 3);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.DOWN);

        //then
        assertEquals(startIsle.getColumn(), endIsle.getColumn());
        assertEquals(3, endIsle.getRow());
    }

    @Test
    public void findIsleRIGHT() {
        //given
        Isle startIsle = gridController.getIsle(6, 1);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.RIGHT);

        //then
        assertEquals(startIsle.getRow(), endIsle.getRow());
        assertEquals(4, endIsle.getColumn());
    }
}