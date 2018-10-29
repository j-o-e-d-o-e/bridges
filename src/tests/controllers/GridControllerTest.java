package tests.controllers;

import javafx.embed.swing.JFXPanel;
import net.joedoe.controllers.GridController;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class GridControllerTest {
    private GridController gridController;
    private Isle startIsle;
    private Isle endIsle;
    private Direction direction;
    private Direction oppositeDirection;

    @Before
    public void setUp() {
        //workaround for initialization error
        new JFXPanel(); // todo: separate entities and views more clearly
        gridController = new GridController();
        startIsle = gridController.getIsle(0);
        endIsle = gridController.getIsle(1);
        direction = Direction.RIGHT;
        oppositeDirection = Direction.LEFT;
    }

    @Test
    public void createBridge() {
        //when
        Bridge bridge = gridController.createBridge(startIsle, direction);

        //then
        assertEquals(endIsle, bridge.getEndIsle());
    }

    @Test
    public void createBridgeFromEndIsle() {
        //given
        startIsle.addBridge(new Bridge(startIsle, direction, endIsle));

        //when
        Bridge bridge = gridController.createBridge(startIsle, direction);

        //then
        assertEquals(startIsle, bridge.getEndIsle());
    }

    @Test
    public void createBridgeReturnsNullNoEndIsleFound() {
        //given
        Direction direction = Direction.UP;

        //when
        Bridge bridge = gridController.createBridge(startIsle, direction);

        //then
        assertNull(bridge);
    }

    @Test
    public void createBridgeReturnsNullAlreadyTwoBridges() {
        //given
        startIsle.addBridge(new Bridge(startIsle, direction, endIsle));
        endIsle.addBridge(new Bridge(endIsle, oppositeDirection, startIsle));

        //when
        Bridge bridge = gridController.createBridge(startIsle, direction);

        //then
        assertNull(bridge);
    }

    @Test
    public void createBridgeReturnsNullCrossingBridge() {
        //given
        direction = Direction.RIGHT;
        gridController.createBridge(gridController.getIsle(3), Direction.DOWN);
        startIsle = gridController.getIsle(5);
        List<Bridge> bridges = startIsle.getBridges();

        //when
        Bridge bridge = gridController.createBridge(startIsle, direction);

        //then
        assertNull(bridge);
        assertEquals(0, bridges.size());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void removeBridge() {
        //given
        List<Bridge> isles = startIsle.getBridges();
        Bridge bridge = new Bridge(startIsle, direction, endIsle);
        startIsle.addBridge(bridge);

        //when
        Bridge removedBridge = gridController.removeBridge(startIsle, direction);

        //then
        assertEquals(bridge, removedBridge);
        assertEquals(0, isles.size());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void removeBridgeFromOppositeIsle() {
        //given
        List<Bridge> isles = endIsle.getBridges();
        Bridge bridge = new Bridge(endIsle, oppositeDirection, startIsle);
        endIsle.addBridge(bridge);

        //when
        Bridge removedBridge = gridController.removeBridge(startIsle, direction);

        //then
        assertEquals(bridge, removedBridge);
        assertEquals(0, isles.size());
    }

    @Test
    public void removeBridgeNoBridges() {
        //when
        Bridge bridge = gridController.removeBridge(startIsle, direction);

        //then
        assertNull(bridge);
    }
}