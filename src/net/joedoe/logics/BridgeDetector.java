package net.joedoe.logics;

import java.util.List;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

class BridgeDetector {

    static boolean collides(Isle startIsle, Isle endIsle, List<Bridge> bridges) {
        if (startIsle.compareTo(endIsle) > 0) return collides(endIsle.getPos(), startIsle.getPos(), bridges);
        else return collides(startIsle.getPos(), endIsle.getPos(), bridges);
    }

    static boolean collides(Coordinate start, Coordinate end, List<Bridge> bridges) {
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL)
            return bridges.stream().anyMatch(b -> b.getAlignment() == Alignment.VERTICAL && b.getStartY() < start.getY()
                    && b.getEndY() > start.getY() && start.getX() < b.getStartX() && end.getX() > b.getStartX());
        else return bridges.stream()
                .anyMatch(b -> b.getAlignment() == Alignment.HORIZONTAL && b.getStartX() < start.getX()
                        && b.getEndX() > start.getX() && start.getY() < b.getStartY() && end.getY() > b.getStartY());
    }
}
