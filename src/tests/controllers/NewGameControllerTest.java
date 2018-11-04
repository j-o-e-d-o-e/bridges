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
        int height = 4;
        int width = 8;
        int isleCount = (int) (0.2 * height * width);
        controller.setHeight(height);
        controller.setWidth(width);
        controller.setIsleCount(isleCount);

        //when
        List<Isle> isles = controller.generateGame();

        //then
        assertEquals(isleCount, isles.size());
        System.out.println("ISLES:");
        isles.forEach(isle -> System.out.println(isle.toString()));
        System.out.println("BRIDGES:");
        controller.getBridges().forEach(bridge -> System.out.println(bridge.toString()));

    }

    @Test
    public void testTest(){
        int rand = (int) (Math.random() * 4) + 2 + 2;
        System.out.println(rand);
    }

    @Test
    @Ignore
    public void getRandomDirection() {
        //given
        int y = 5;
        int x = 5;
        controller.setHeight(6);
        controller.setWidth(6);

        //when
        Direction direction = controller.getRandomDirection(y, x);

        //then
        System.out.println("\n" + direction.toString());
    }

    @Test
    public void generateInitialIsle() {
        //given
        int height = 8;
        int width = 4;
        controller.setHeight(height);
        controller.setWidth(width);

        //when
        Isle isle = controller.generateInitialIsle();

        //then
        System.out.println(isle.toString());
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

        Isle isle = new Isle(3, 5, 0);
        Direction direction = Direction.UP;
        int distance = 2;

        //when
        Isle res = controller.createIsle(isle, direction, distance);

        //then
        System.out.println(res.toString());

    }
}