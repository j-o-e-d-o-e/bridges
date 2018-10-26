package net.joedoe.controllers;

import net.joedoe.entities.GridEntity;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Direction;
import net.joedoe.views.Grid;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridController {
    private Grid grid;
    private List<Isle> isles = Mocks.ISLES;

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController(Grid grid) {
        this.grid = grid;
        LOGGER.setLevel(Level.OFF);
    }

    public Isle searchForIsle(GridEntity startIsle, Direction direction) {
        LOGGER.info(startIsle.toString() + " " + direction);
        for (Isle isle : isles) {
            if (isle != startIsle) {
                if (direction == Direction.RIGHT && isle.getRow() == startIsle.getRow()
                        && isle.getColumn() > startIsle.getColumn())
                    return isle;
                if (direction == Direction.LEFT && isle.getRow() == startIsle.getRow()
                        && isle.getColumn() < startIsle.getColumn())
                    return isle;
                if (direction == Direction.DOWN && isle.getRow() > startIsle.getRow()
                        && isle.getColumn() == startIsle.getColumn())
                    return isle;
                if (direction == Direction.UP && isle.getRow() < startIsle.getRow()
                        && isle.getColumn() == startIsle.getColumn())
                    return isle;
            }
        }
        return null;
    }

    public List<Isle> getIsles() {
        return isles;
    }
}