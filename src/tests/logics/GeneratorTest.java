package net.joedoe.logics;

import net.joedoe.logics.Generator;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static net.joedoe.utils.GameInfo.*;

public class GeneratorTest {
    private Generator generator;

    @Before
    public void setUp() {
        generator = new Generator();
        generator.setHeight(MAX_HEIGHT);
        generator.setWidth(MAX_WIDTH);
        generator.setIslesCount(MAX_ISLES_COUNT);
        generator.setIndices(IntStream.range(0, MAX_WIDTH * MAX_HEIGHT).boxed().collect(Collectors.toList()));
    }

    @Test
    public void generateGame() {
        int initialIsleCount = generator.getIsleCount();

        generator.generateGame();

        assertEquals(initialIsleCount, generator.getIsles().size());
    }

    @Test
    public void getDirections() {
        Isle startIsle = new Isle(new Coordinate(2, 2), 0);

        List<Direction> directions = generator.getDirections(startIsle);

        assertEquals(4, directions.size());
    }

    @Test
    public void getDirectionsBORDER() {
        Isle startIsle = new Isle(new Coordinate(23, 2), 0);

        List<Direction> directions = generator.getDirections(startIsle);

        assertEquals(3, directions.size());
    }

    @Test
    public void getDirectionsCORNER() {
        Isle startIsle = new Isle(new Coordinate(0, 0), 0);

        List<Direction> directions = generator.getDirections(startIsle);

        assertEquals(2, directions.size());
    }

    @Test
    public void getDistances() {
        Isle startIsle = new Isle(new Coordinate(0, 9), 0);
        Direction direction = Direction.UP;

        List<Integer> distances = generator.getDistances(startIsle, direction);

        assertEquals(8, distances.size());
    }

    @Test
    public void collidesIsles() {
        Isle isle = generator.createIsle(new Coordinate(3, 3));
        generator.getIsles().add(isle);

        boolean collides = generator.collidesIsles(new Coordinate(0,3), new Coordinate(6,3));

        assertTrue(collides);
    }
}