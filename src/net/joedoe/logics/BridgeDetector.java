package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import java.util.List;

/**
 * Checkt, ob eine vom Nutzer erstellte oder vom Programm generierte Brücke mit
 * einer bereits existierenden Brücke kollidiert.
 */
class BridgeDetector {
    private List<Bridge> bridges;

    /**
     * Wird eine Liste von Brücken zur Überprüfung auf Kollision übergeben.
     *
     * @param bridges Liste von Brücken
     */
    BridgeDetector(List<Bridge> bridges) {
        this.bridges = bridges;
    }

    /**
     * Checkt, ob eine neue Brücke zwischen den beiden übergebenen Inseln mit einer
     * bereits existierenden Brücke kollidieren würde.
     *
     * @param startIsle erste Insel, die mit der Brücke verbunden sein soll
     * @param endIsle   zweite Insel, die mit der Brücke verbunden sein soll
     * @return true, falls die neue Brücke mit einer existierenden Brücke kollidiert
     */
    boolean collides(Isle startIsle, Isle endIsle) {
        // falls 'startIsle' weiter vom Koordinatenursprung entfernt ist als 'endIsle'
        if (startIsle.compareTo(endIsle) > 0) return collides(endIsle.getPos(), startIsle.getPos());
        else return collides(startIsle.getPos(), endIsle.getPos());
    }

    /**
     * Checkt, ob eine neue Brücke zwischen den beiden übergebenen Koordinaten mit
     * einer bereits existierenden Brücke kollidieren würde.
     *
     * @param start erste Koordinate, aus der die Brücke bestehen soll
     * @param end   zweite Koordinate, aus der die Brücke bestehen soll
     * @return true, falls die neue Brücke mit einer existierenden Brücke kollidiert
     */
    boolean collides(Coordinate start, Coordinate end) {
        // falls neue Brücke horizontal ist
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL && b.getStartY() < start.getY()
                    && b.getEndY() > start.getY() && start.getX() < b.getStartX() && end.getX() > b.getStartX());
            // falls neue Brücke vertikal ist
        else return bridges.stream()
                .anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL && b.getStartX() < start.getX()
                        && b.getEndX() > start.getX() && start.getY() < b.getStartY() && end.getY() > b.getStartY());
    }
}
