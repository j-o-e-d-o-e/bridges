package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import java.util.List;

/**
 * Checkt, ob eine vom Nutzer erstellte oder vom Programm generierte Br�cke mit
 * einer bereits existierenden Br�cke kollidiert.
 */
class BridgeDetector {
    private List<Bridge> bridges;

    /**
     * Wird eine Liste von Br�cken zur �berpr�fung auf Kollision �bergeben.
     *
     * @param bridges Liste von Br�cken
     */
    BridgeDetector(List<Bridge> bridges) {
        this.bridges = bridges;
    }

    /**
     * Checkt, ob eine neue Br�cke zwischen den beiden �bergebenen Inseln mit einer
     * bereits existierenden Br�cke kollidieren w�rde.
     *
     * @param startIsle erste Insel, die mit der Br�cke verbunden sein soll
     * @param endIsle   zweite Insel, die mit der Br�cke verbunden sein soll
     * @return true, falls die neue Br�cke mit einer existierenden Br�cke kollidiert
     */
    boolean collides(Isle startIsle, Isle endIsle) {
        // falls 'startIsle' weiter vom Koordinatenursprung entfernt ist als 'endIsle'
        if (startIsle.compareTo(endIsle) > 0) return collides(endIsle.getPos(), startIsle.getPos());
        else return collides(startIsle.getPos(), endIsle.getPos());
    }

    /**
     * Checkt, ob eine neue Br�cke zwischen den beiden �bergebenen Koordinaten mit
     * einer bereits existierenden Br�cke kollidieren w�rde.
     *
     * @param start erste Koordinate, aus der die Br�cke bestehen soll
     * @param end   zweite Koordinate, aus der die Br�cke bestehen soll
     * @return true, falls die neue Br�cke mit einer existierenden Br�cke kollidiert
     */
    boolean collides(Coordinate start, Coordinate end) {
        // falls neue Br�cke horizontal ist
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL && b.getStartY() < start.getY()
                    && b.getEndY() > start.getY() && start.getX() < b.getStartX() && end.getX() > b.getStartX());
            // falls neue Br�cke vertikal ist
        else return bridges.stream()
                .anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL && b.getStartX() < start.getX()
                        && b.getEndX() > start.getX() && start.getY() < b.getStartY() && end.getY() > b.getStartY());
    }
}
