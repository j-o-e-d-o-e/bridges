package tests.entities;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
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
        assertEquals(bridge, startIsle.getBridge(endIsle, true));
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
        assertNull(startIsle.getBridge(endIsle, true));

    }
}