package net.joedoe.entities;

import net.joedoe.utils.Coordinate;

/**
 * Interface zur Speicherung und Injektion von Brücken.
 *
 */
public interface IBridge {

    Coordinate getStart();

    Coordinate getEnd();

    boolean isDoubleBridge();
}
