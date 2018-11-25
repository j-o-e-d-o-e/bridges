package tests.logics;

import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatusCheckerTest {
    private StatusChecker checker;
    private GridController controller;
    private Isle startIsle, endIsle;

    @Before
    public void setUp() {
        controller = new GridController();
        checker = new StatusChecker(controller);
        controller.setIsles(Mocks.ISLES);
        controller.setBridges(Mocks.BRIDGES);
        startIsle = controller.getIsle(0, 0);
        endIsle = controller.getIsle(4, 6);
    }

    @Test
    public void unsolvable() {
        //given
        controller.removeBridge(startIsle.getPos(), Direction.DOWN);
        controller.addBridge(startIsle.getPos(), Direction.RIGHT);
        controller.removeBridge(endIsle.getPos(), Direction.UP);
        controller.addBridge(endIsle.getPos(), Direction.RIGHT);

        //when
        boolean status = checker.unsolvable();

        //then
        assertTrue(status);
    }

    @Test
    public void solved() {
        //when
        boolean status = checker.solved();

        //then
        assertTrue(status);
    }

    @Test
    public void notSolved() {
        //given
        controller.removeBridge(startIsle.getPos(), Direction.RIGHT);

        //when
        boolean status = checker.solved();

        //then
        assertFalse(status);
    }
}