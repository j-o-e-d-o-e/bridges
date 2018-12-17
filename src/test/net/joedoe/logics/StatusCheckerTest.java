package test.net.joedoe.logics;

import net.joedoe.entities.IIsle;
import net.joedoe.logics.BridgeController;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatusCheckerTest {
    private StatusChecker checker;
    private BridgeController controller;
    private Coordinate start, end;

    @Before
    public void setUp() {
        controller = new BridgeController();
        checker = new StatusChecker(controller);
        List<IIsle> isles = Mocks.getIsles();
        controller.setIsles(isles);
        controller.setBridges(Mocks.getBridges());
        start = isles.get(0).getPos();
        end = isles.get(9).getPos();
    }

    @Test
    public void error() {
        controller.addBridge(start, Direction.RIGHT);

        assertTrue(checker.error());
        assertFalse(checker.unsolvable());
        assertFalse(checker.solved());
    }

    // @Ignore
    @Test
    public void unsolvable() {
        controller.removeBridge(start, Direction.DOWN);
        controller.addBridge(start, Direction.RIGHT);
        controller.removeBridge(end, Direction.UP);

        assertFalse(checker.error());
        assertTrue(checker.unsolvable());
        assertFalse(checker.solved());
    }

    @Test
    public void solved() {
        assertFalse(checker.error());
        assertFalse(checker.unsolvable());
        assertTrue(checker.solved());
    }

    @Test
    public void notSolved() {
        controller.removeBridge(start, Direction.RIGHT);

        assertFalse(checker.error());
        assertFalse(checker.unsolvable());
        assertFalse(checker.solved());
    }
}