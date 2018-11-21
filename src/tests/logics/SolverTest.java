package tests.logics;

import net.joedoe.logics.GridController;
import net.joedoe.logics.Solver;
import net.joedoe.logics.StatusChecker;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.logging.Logger;

public class SolverTest {
    private Solver solver;
    private final static Logger LOGGER = Logger.getLogger(SolverTest.class.getName());

    @Before
    public void setUp() {
        GridController controller = new GridController();
        controller.setIsles(Mocks.ISLES);
        StatusChecker checker = new StatusChecker(controller);
        solver = new Solver(controller, checker);
//        LOGGER.setLevel(Level.OFF);
    }

    @Test
    public void getNextBridge() {
        //given


        //when
        Coordinate[] next = solver.getNextBridge();

        //then
        LOGGER.info("Next: " + next[0].toString() + "/" + next[1].toString());
    }
}