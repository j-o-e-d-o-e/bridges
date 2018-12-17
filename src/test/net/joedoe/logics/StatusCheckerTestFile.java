package test.net.joedoe.logics;

import net.joedoe.logics.StatusChecker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StatusCheckerTestFile extends FileHandlerTesting {
    private StatusChecker checker;

    @Before
    public void setUp() {
        checker = new StatusChecker(controller);
    }

    @Test
    public void puzzleMocks() {
        String file = "solved.bgs";
        loadData(file);

        assertTrue(checker.solved());
    }

    @Test
    public void puzzleIsolated() {
        String file = "isolated.bgs";
        loadData(file);

        assertTrue(checker.unsolvable());
    }

    @Test
    public void puzzleDisconnected() {
        String file = "disconnected.bgs";
        loadData(file);

        assertTrue(checker.unsolvable());
    }
}