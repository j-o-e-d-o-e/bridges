package test.net.joedoe.logics;

import net.joedoe.entities.IBridge;
import net.joedoe.logics.Solver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolverTestFile extends FileHandlerTesting {
    private Solver solver;

    @Before
    public void setUp() {
        solver = new Solver(controller);
    }

    private void solve() {
        IBridge next = solver.getNextBridge();
        while (next != null)
            next = solver.getNextBridge();
        controller.saveGame();
    }

    @Test
    public void puzzleMocks() {
        String file = "mocks.bgs";
        loadData(file);

        solve();

        saveData(file);
        String exp = convertSolutionToString(file);
        String res = convertResultToString(file);
        assertEquals(exp, res);
    }

    @Test
    public void puzzle25x25() {
        String file = "25x25.bgs";
        loadData(file);

        solve();

        saveData(file);
        String exp = convertSolutionToString(file);
        String res = convertResultToString(file);
        assertEquals(exp, res);
    }

    @Test
    public void puzzle4ones() {
        String file = "4ones.bgs";
        loadData(file);

        solve();

        saveData(file);
        String exp = convertSolutionToString(file);
        String res = convertResultToString(file);
        assertEquals(exp, res);
    }

    @Test
    public void puzzle4with3Ones() {
        String file = "4with3Ones.bgs";
        loadData(file);

        solve();

        saveData(file);
        String exp = convertSolutionToString(file);
        String res = convertResultToString(file);
        assertEquals(exp, res);
    }
}