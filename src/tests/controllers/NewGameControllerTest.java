package tests.controllers;

import net.joedoe.controllers.NewGameController;
import net.joedoe.entities.Isle;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class NewGameControllerTest {
    private NewGameController controller;
    private static final int HEIGHT = 5; //MIN: 2, MAX: 25
    private static final int WIDTH = 5;
    private final static Logger LOGGER = Logger.getLogger(NewGameControllerTest.class.getName());

    @Before
    public void setUp() {
        controller = new NewGameController();
        controller.setHeight(HEIGHT);
        controller.setWidth(WIDTH);
        controller.setIsleCount();
        //        LOGGER.setLevel(Level.OFF);
    }

    @Test
    public void generateGame() {
        //given
        int initialIsleCount = controller.getIsleCount();

        //when
        List<Isle> isles = controller.generateGame();

        //then
        assertEquals(initialIsleCount, isles.size());
        isles.forEach(isle -> System.out.println(isle.toString()));
    }

    @Test
    public void getEndIsle() {
        //given
        List<Integer> indices = IntStream.range(0, controller.getHeight() * controller.getWidth())
                .boxed().collect(Collectors.toList());
        controller.setIndices(indices);
        controller.addIsle(3, 3);
        Isle startIsle = controller.getIsles().get(0);

        //when
        Isle endIsle = controller.getEndIsle(startIsle);

        //then
        LOGGER.info("End isle: " + endIsle.toString());
    }
}