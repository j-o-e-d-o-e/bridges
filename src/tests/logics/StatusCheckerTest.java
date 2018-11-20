package tests.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatusCheckerTest {
    private StatusChecker checker;
    private GridController controller;

    @Before
    public void setUp() {
        controller = new GridController();
        checker = new StatusChecker(controller);
        controller.setIsles(Mocks.ISLES);
        controller.setBridges(Mocks.BRIDGES);
    }

    @Test
    public void error() {
    }

    @Test
    public void unsolvable() {
        //given
        Isle isle1 = controller.getIsle(0, 0);
        controller.removeBridge(isle1.getY(), isle1.getX(), Direction.DOWN);
        controller.addBridge(isle1.getY(), isle1.getX(), Direction.RIGHT);
        Isle isle2 = controller.getIsle(6, 4);
        controller.removeBridge(isle2.getY(), isle2.getX(), Direction.UP);
        controller.addBridge(isle2.getY(), isle2.getX(), Direction.RIGHT);

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
}