package test.net.joedoe.logics;

import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;
import net.joedoe.logics.BridgeController;
import net.joedoe.logics.Solver;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SolverTest {
    private Solver solver;
    private BridgeController controller;
    private List<IIsle> isles;
    private Isle startIsle;
    private Coordinate start;

    @Before
    public void setUp() {
        controller = new BridgeController();
        isles = Mocks.getIsles();
        controller.setIsles(isles);
        solver = new Solver(controller);
        startIsle = (Isle) isles.get(0);
        start = startIsle.getPos();
    }

    @Test
    public void getStartIsles() {
        int islesSize = controller.getIslesSize();
        while (startIsle.getMissingBridges() > 0)
            startIsle.addBridge(false);

        List<Isle> isles = solver.getStartIsles();

        assertEquals(islesSize - 1, isles.size());
    }

    @Test
    public void getNeighbours() {
        controller.addBridge(start, Direction.DOWN);
        controller.addBridge(start, Direction.DOWN);

        List<Isle> neighbours = solver.getConnectables(startIsle);

        assertEquals(1, neighbours.size());
    }

    @Test
    public void getNeighboursOneBridge() {
        controller.addBridge(start, Direction.DOWN);
        List<Isle> connectables = solver.getConnectables(startIsle);

        int count = solver.getConnectablesOneBridge(startIsle, connectables);

        assertEquals(1, count);
    }

    @Test
    public void getEndIsle() {
        controller.addBridge(start, Direction.DOWN);
        List<Isle> connectables = solver.getConnectables(startIsle);
    
        Isle endIsle = solver.getEndIsle(startIsle, connectables);
    
        assertEquals(isles.get(8).getPos(), endIsle.getPos());
    }
}