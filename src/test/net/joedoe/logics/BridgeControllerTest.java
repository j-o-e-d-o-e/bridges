package test.net.joedoe.logics;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;
import net.joedoe.logics.BridgeController;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class BridgeControllerTest {
    private BridgeController controller;
    private Isle startIsle, endIsle;
    private Coordinate start;
    private Direction direction;

    @Before
    public void setUp() {
        controller = new BridgeController();
        List<IIsle> isles = Mocks.getIsles();
        controller.setIsles(isles);
        startIsle = (Isle) isles.get(0);
        start = startIsle.getPos();
        endIsle = (Isle) isles.get(1);
        direction = Direction.DOWN;
    }

    @Test
    public void addBridge() {
        IBridge bridge = controller.addBridge(start, direction);

        assertEquals(startIsle.getBridges() - 1, startIsle.getMissingBridges());
        assertEquals(1, startIsle.getNeighbours().size());
        assertEquals(endIsle.getBridges() - 1, endIsle.getMissingBridges());
        assertEquals(1, endIsle.getNeighbours().size());
        assertEquals(startIsle.getPos(), bridge.getStart());
        assertEquals(endIsle.getPos(), bridge.getEnd());
    }

    @Test
    public void removeBridge() {
        IBridge added = controller.addBridge(start, direction);
        IBridge removed = controller.removeBridge(start, direction);

        assertEquals(added.getStart(), removed.getStart());
        assertEquals(added.getEnd(), removed.getEnd());
    }

    @Test
    public void getEndIsle() {
        Isle endIsle = controller.getEndIsle(startIsle, direction);

        assertEquals(this.endIsle, endIsle);
    }
}