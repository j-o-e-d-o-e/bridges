package net.joedoe.logics;

import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GridControllerTest {
    private GridController controller;
    private Coordinate start, end;
    private Isle startIsle, endIsle;
    private Direction direction;

    @Before
    public void setUp() {
        controller = new GridController();
        controller.setIsles(Mocks.ISLES);
        start = (Coordinate) Mocks.ISLES[0][0];
        startIsle = controller.getIsle(start);
        end = (Coordinate) Mocks.ISLES[1][0];
        endIsle = controller.getIsle(end);
        direction = Direction.DOWN;
    }

    @Test
    public void addBridge() {
        Coordinate[] bridge = controller.addBridge(start, direction);

        assertEquals(startIsle.getBridges() - 1, startIsle.getMissingBridges());
        assertEquals(1, startIsle.getNeighbours().size());
        assertEquals(endIsle.getBridges() - 1, endIsle.getMissingBridges());
        assertEquals(1, endIsle.getNeighbours().size());
        assertEquals(startIsle.getPos(), bridge[0]);
        assertEquals(endIsle.getPos(), bridge[1]);
    }

    @Test
    public void removeBridge() {
        Coordinate[] added = controller.addBridge(start, direction);
        Coordinate[] removed = controller.removeBridge(start, direction);

        assertEquals(added[0], removed[0]);
        assertEquals(added[1], removed[1]);
    }

    @Test
    public void getEndIsle() {
        Isle endIsle = controller.getEndIsle(startIsle, direction);

        assertEquals(this.endIsle, endIsle);
    }
}