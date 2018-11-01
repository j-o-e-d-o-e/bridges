package tests.entities;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BridgeTest {


    @Test
    public void create() {
        //given
        Isle startIsle = new Isle(3, 4, 0);
        Isle endIsle = new Isle(2, 1, 2);

        //when
        Bridge bridge = new Bridge(startIsle, endIsle);

        //then
        assertEquals(endIsle.getY(), bridge.getStartY());
        assertEquals(endIsle.getX(), bridge.getStartX());
    }
}