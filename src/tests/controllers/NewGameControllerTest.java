package tests.controllers;

import net.joedoe.controllers.NewGameController;
import org.junit.Before;
import org.junit.Test;

public class NewGameControllerTest {
    private NewGameController controller;

    @Before
    public void setUp() {
        controller = new NewGameController();
    }

    @Test
    public void generateGame() {
        //given
        int rows = 10;
        int columns = 10;
        int isles = (int) (0.2 * rows * columns);
        controller.setRows(rows);
        controller.setColumns(columns);
        controller.setIsleCount(10);

        //when
        controller.generateGame();

        //then
    }
}