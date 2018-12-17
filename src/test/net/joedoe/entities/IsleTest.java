
package test.net.joedoe.entities;

import net.joedoe.entities.Isle;
import net.joedoe.utils.Coordinate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsleTest {
    private Isle startIsle, endIsle;

    @Before
    public void setUp() {
        startIsle = new Isle(new Coordinate(3, 3), 3);
        endIsle = new Isle(new Coordinate(1, 1), 1);
    }

    @Test
    public void addBridge() {
        startIsle.addBridge(false);
        startIsle.addNeighbour(endIsle);

        // then
        assertEquals(startIsle.getBridges() - 1, startIsle.getMissingBridges());
        assertEquals(1, startIsle.getNeighbours().size());
    }

    @Test
    public void removeBridge() {
        startIsle.addBridge(false);
        startIsle.addNeighbour(endIsle);

        startIsle.removeBridge();
        startIsle.removeNeighbour(endIsle);

        assertEquals(startIsle.getBridges(), startIsle.getMissingBridges());
        assertEquals(0, startIsle.getNeighbours().size());
    }
}