package tests.controllers;

import net.joedoe.controllers.GridController;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GridControllerTest {
    private GridController gridController;
    private List<Isle> isles;

    @Before
    public void setUp() {
        gridController = new GridController();
        isles = gridController.getIsles();
    }

    @Test
    public void sortDirectionUp() {
        //given
        Isle startIsle = isles.get(0); //8

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.UP);

        //then
        assertEquals(startIsle.getColumn(), endIsle.getColumn());
        assertEquals(3, endIsle.getRow());
    }

    @Test
    public void sortDirectionLeft() {
        //given
        Isle startIsle = isles.get(2);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.LEFT);

        //then
        assertEquals(startIsle.getRow(), endIsle.getRow());
        assertEquals(4, endIsle.getColumn());
    }

    @Test
    public void sortDirectionDown() {
        //given
        Isle startIsle = isles.get(4);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.DOWN);

        //then
        assertEquals(startIsle.getColumn(), endIsle.getColumn());
        assertEquals(3, endIsle.getRow());
    }

    @Test
    public void sortDirectionRight() {
        //given
        Isle startIsle = isles.get(0);

        //when
        Isle endIsle = gridController.findIsle(startIsle, Direction.RIGHT);

        //then
        assertEquals(startIsle.getRow(), endIsle.getRow());
        assertEquals(4, endIsle.getColumn());
    }
}