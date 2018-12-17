package test.net.joedoe.entities;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BridgeTest {

    @Test
    public void create() {
        Isle startIsle = new Isle(new Coordinate(4, 3), 1);
        Isle endIsle = new Isle(new Coordinate(1, 3), 2);

        Bridge bridge = new Bridge(startIsle.getPos(), endIsle.getPos(), false);

        assertEquals(Alignment.HORIZONTAL, bridge.getAlignment());
        assertFalse(bridge.isDoubleBridge());
        assertTrue(bridge.contains(startIsle.getPos(), endIsle.getPos()));
    }
}