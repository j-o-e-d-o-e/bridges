package tests.logics;

import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
    public void unsolvable() {
        //given
        Isle isle1 = controller.getIsle(0, 0);
        controller.removeBridge(isle1.getX(), isle1.getY(), Direction.DOWN);
        controller.addBridge(isle1.getX(), isle1.getY(), Direction.RIGHT);
        Isle isle2 = controller.getIsle(4, 6);
        controller.removeBridge(isle2.getX(), isle2.getY(), Direction.UP);
        controller.addBridge(isle2.getX(), isle2.getY(), Direction.RIGHT);

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
        List<Coordinate[]> bridges = new ArrayList<>(Mocks.BRIDGES);
        bridges.remove(0);
        controller.setBridges(bridges);

        //when
        boolean status = checker.solved();

        //then
        assertFalse(status);
    }
}