package tests.controllers;

import net.joedoe.controllers.GridController;
import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class GridControllerTest {
    private GridController gridController;
    private List<Isle> isles = Mocks.ISLES;

    @Before
    public void setUp() {
        gridController =  new GridController();
    }

    @Test
    public void createBridge() {
        Isle startIsle = isles.get(0);
        Bridge bridge = gridController.createBridge(startIsle, Direction.RIGHT);
        assertNotNull(bridge);
    }
}