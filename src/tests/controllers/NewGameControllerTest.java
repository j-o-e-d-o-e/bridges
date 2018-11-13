package tests.controllers;

import net.joedoe.controllers.NewGameController;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class NewGameControllerTest {
    private NewGameController controller;
    private static final int MAX = 10; // 25
    private static final int MIN = 2;

    @Before
    public void setUp() {
        controller = new NewGameController();
    }

    @Test
    public void createGameAUTO() {
        //when
        controller.createGame();

        //then
        assertEquals(controller.getIsleCount(), controller.getIsles().size());
    }

    @Test
    public void generateGame() {
        //given
        int height = 5;
        int width = 5;
        int isleCount = (int) (0.2 * height * width);
        controller.setHeight(height);
        controller.setWidth(width);
        controller.setIsleCount(isleCount);
    }

    @Test
    public void createIsle(){
        //given
        int height = 5;
        controller.setHeight(height);
        int width = 5;
        controller.setWidth(width);
        List<Integer> indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        controller.setIndices(indices);
    }
}