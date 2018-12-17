package net.joedoe.entities;

import net.joedoe.utils.Coordinate;

/**
 * Interface zur Speicherung und Injektion von Inseln.
 *
 */
public interface IIsle {

    Coordinate getPos();

    int getX();

    int getY();

    int getBridges();
}
