package tests.controllers;

import net.joedoe.controllers.NewGameController;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class NewGameControllerTest {
    private NewGameController controller;
    private static final int MAX = 10; // 25
    private static final int MIN = 2;
    private final static Logger LOGGER = Logger.getLogger(NewGameControllerTest.class.getName());

    @Before
    public void setUp() {
        controller = new NewGameController();
        controller.setHeight(MAX);
        controller.setWidth(MAX);
        List<Integer> indices = IntStream.range(0, controller.getHeight() * controller.getWidth())
                .boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        //        LOGGER.setLevel(Level.OFF);
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
    public void getEndIsle(){
        //given
        Isle startIsle = new Isle(0, 0, 0);

        //when
        Isle endIsle = controller.getEndIsle(startIsle);

        //then
        LOGGER.info("End isle: " + endIsle.toString());

    }
}