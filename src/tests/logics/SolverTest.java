package tests.logics;

import net.joedoe.entities.Isle;
import net.joedoe.logics.GridController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class SolverTest {
    private Solver solver;
    private GridController controller;
    private Isle startIsle;
    private Isle endIsle;

    @Before
    public void setUp() {
        controller = new GridController();
        controller.setIsles(Mocks.ISLES);
        StatusChecker checker = new StatusChecker(controller);
        solver = new Solver(controller, checker);
        startIsle = controller.getIsle(0, 0);
        endIsle = controller.getIsle(0, 3);
//        LOGGER.setLevel(Level.OFF);
    }

    @Test
    public void getStartIsles() {
        //given
        int islesSize = controller.getIslesSize();
        startIsle.setBridgeCount(0);

        //when
        List<Isle> isles = solver.getStartIsles();

        //then
        assertEquals(islesSize - 1, isles.size());
    }

    @Test
    public void getNeighbours(){
        //given
//        controller.addBridge(startIsle.getX(), startIsle.getY(), Direction.DOWN);
//        endIsle.setBridgeCount(0);
        startIsle.setBridgeCount(2);
        endIsle.setBridgeCount(2);
        controller.addBridge(startIsle.getX(), startIsle.getY(), Direction.DOWN);

        //when
        List<Isle> neighbours = solver.getNeighbours(startIsle);

        //then
        assertEquals(1, neighbours.size());
    }

    @Test
    public void getNeighboursOneBridge(){
        //given
        List<Isle> neighbours = solver.getNeighbours(startIsle);
//        endIsle.setBridgeCount(1);
        controller.addBridge(startIsle.getX(), startIsle.getY(), Direction.DOWN);

        //when
        int count = solver.getNeighboursOneBridge(neighbours, startIsle);

        //then
        assertEquals(1, count);
    }

    @Test
    public void getEndIsle(){
        //given
        controller.addBridge(endIsle.getX(), endIsle.getY(), Direction.UP);
        List<Isle> neighbours = solver.getNeighbours(startIsle);
        Isle isle = controller.getIsle(4, 0);

        //when
        Isle endIsle = solver.getEndIsle(neighbours, startIsle);

        //then
        assertEquals(isle, endIsle);
    }
}