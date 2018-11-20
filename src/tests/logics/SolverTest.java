package tests.logics;

import net.joedoe.logics.GridController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import org.junit.Before;
import org.junit.Test;

public class SolverTest {
    private Solver solver;

    @Before
    public void setUp() {
        GridController controller = new GridController();
        StatusChecker checker = new StatusChecker(controller);
        solver = new Solver(controller, checker);
    }

    @Test
    public void getNextBridge() {
        //given


        //when
        Coordinate[] next = solver.getNextBridge();

        //then

    }
}