package test.net.joedoe.logics;

import static net.joedoe.utils.GameInfo.MAX_HEIGHT;
import static net.joedoe.utils.GameInfo.MAX_ISLES_COUNT;
import static net.joedoe.utils.GameInfo.MAX_WIDTH;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.joedoe.logics.Generator;
import org.junit.Before;
import org.junit.Test;

import net.joedoe.entities.Isle;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;

public class GeneratorTest {
    private Generator generator;

    @Before
    public void setUp() {
        generator = new Generator();
        generator.setData(MAX_WIDTH, MAX_HEIGHT, MAX_ISLES_COUNT);
        generator.setIndices(IntStream.range(0, MAX_WIDTH * MAX_HEIGHT).boxed().collect(Collectors.toList()));
    }

    @Test
    public void generateGame() {
        generator.generateGame();

        assertEquals(generator.getIsleCount(), generator.getIsles().size());
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
}