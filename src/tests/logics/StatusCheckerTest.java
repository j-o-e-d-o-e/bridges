package net.joedoe.logics;

import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatusCheckerTest {
    private StatusChecker checker;
    private GridController controller;
    private Coordinate start, end;
    private Isle startIsle, endIsle;

    @Before
    public void setUp() {
        controller = new GridController();
        checker = new StatusChecker(controller);
        controller.setIsles(Mocks.ISLES);
        controller.setBridges(Mocks.BRIDGES);
        start = (Coordinate) Mocks.ISLES[0][0];
        startIsle = controller.getIsle(start);
        end = (Coordinate) Mocks.ISLES[9][0];
        endIsle = controller.getIsle(end);
    }

    @Test
    public void unsolvable() {
        controller.removeBridge(startIsle.getPos(), Direction.DOWN);
        controller.addBridge(startIsle.getPos(), Direction.RIGHT);
        controller.removeBridge(endIsle.getPos(), Direction.UP);
        controller.addBridge(endIsle.getPos(), Direction.RIGHT);

        boolean status = checker.unsolvable();

        assertTrue(status);
    }

    @Test
    public void solved() {
        boolean status = checker.solved();

        assertTrue(status);
    }

    @Test
    public void notSolved() {
        controller.removeBridge(start, Direction.RIGHT);

        boolean status = checker.solved();

        assertFalse(status);
    }
}