package tests.entities;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BridgeTest {

    @Test
    public void create() {
        //given
        Isle startIsle = new Isle(4, 3, 0);
        Isle endIsle = new Isle(1, 3, 2);

        //when
        Bridge bridge = new Bridge(startIsle, endIsle);

        //then
        assertEquals(endIsle.getY(), bridge.getStartY());
        assertEquals(endIsle.getX(), bridge.getStartX());
        assertEquals(Alignment.HORIZONTAL, bridge.getAlignment());
        assertEquals(startIsle.getY(), bridge.getEndY());
        assertEquals(startIsle.getX(), bridge.getEndX());
        assertEquals(endIsle.getY(), bridge.getStartY());
        assertEquals(endIsle.getY(), bridge.getStartY());
    }
}