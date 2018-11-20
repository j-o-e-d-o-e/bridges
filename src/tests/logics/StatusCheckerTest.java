package tests.logics;

import net.joedoe.logics.GridController;
import net.joedoe.utils.Mocks;
import org.junit.Before;
import org.junit.Test;

public class StatusCheckerTest {
    private GridController controller;

    @Before
    public void setUp() {
        controller = new GridController();
        controller.setIsles(Mocks.ISLES);
        controller.setBridges(Mocks.BRIDGES);
    }

    @Test
    public void errorOccured() {
    }

    @Test
    public void unsolvable() {
    }

    @Test
    public void connected() {
        //given


        //when


        //then
    }
}