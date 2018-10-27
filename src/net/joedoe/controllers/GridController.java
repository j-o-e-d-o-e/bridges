package net.joedoe.controllers;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.GridEntity;
import net.joedoe.entities.Isle;
import net.joedoe.entities.Mocks;
import net.joedoe.utils.Direction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridController {
    private List<Isle> isles = Mocks.ISLES;

    private final static Logger LOGGER = Logger.getLogger(GridController.class.getName());

    public GridController() {
        LOGGER.setLevel(Level.OFF);
    }

    public Bridge createBridge(Isle startIsle, Direction direction) {
        Isle endIsle = searchForIsle(startIsle, direction);
        if (endIsle == null) return null;
        Bridge bridge = new Bridge(startIsle, direction, endIsle);
        startIsle.addBridge(bridge);
        return bridge;
    }

    private Isle searchForIsle(GridEntity startIsle, Direction direction) {
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