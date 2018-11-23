package tests.entities;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsleTest {
    private Isle startIsle, endIsle;
    private Bridge bridge;

    @Before
    public void setUp() {
        startIsle = new Isle(3, 3, 3);
        endIsle = new Isle(1, 1, 1);
        bridge = new Bridge(startIsle, endIsle);
    }

    @Test
    public void addBridge() {
        //given
        int initialBridgeCount = startIsle.getBridgeCount();

        //when
        startIsle.addBridge(bridge);

        //then
        assertEquals(1, startIsle.getBridges().size());
        assertEquals(1, startIsle.getNeighbours().size());
        assertEquals(initialBridgeCount - 1, startIsle.getMissingBridgeCount());
        assertEquals(bridge, startIsle.getBridgeTo(endIsle));
    }

    @Test
    public void removeBridge() {
        //given
        startIsle.addBridge(bridge);
        int missingBridgeCount = startIsle.getMissingBridgeCount();

        //when
        startIsle.removeBridge(bridge);

        //then
        assertEquals(0, startIsle.getBridges().size());
        assertEquals(0, startIsle.getNeighbours().size());
        assertEquals(missingBridgeCount + 1, startIsle.getMissingBridgeCount());
        assertNull(startIsle.getBridgeTo(endIsle));
    }

    @Test
    public void getBridgeCountTo(){
        //given
        GridController controller = new GridController();
        controller.setIsles(Mocks.ISLES);
        Isle startIsle = controller.getIsle(0, 0);
        Isle endIsle = controller.getEndIsle(startIsle, Direction.RIGHT);
        Bridge bridge1 = new Bridge(startIsle, endIsle);
        Bridge bridge2 = new Bridge(endIsle, startIsle);
//        startIsle.addBridge(bridge1);
        startIsle.addBridge(bridge2);
        endIsle.addBridge(bridge1);
//        endIsle.addBridge(bridge2);

        //when
        int count1 = startIsle.getBridgeCountTo(endIsle);
        int count2 = endIsle.getBridgeCountTo(startIsle);

        //then
        assertEquals(count1,count2);
        assertEquals(1, count1);
        assertEquals(1, count2);

    }
}