package net.joedoe.views;

import javafx.event.Event;

/**
 * Enth�lt aktuellen Punktestatus, �ber den die Punkte-Zeile im
 * {@link Board} informiert wird.
 */
class PointEvent extends Event {
    private int points;

    /**
     * Werden aktuelle Punkte �bergeben.
     *
     * @param points Punkte des Spielers
     */
    PointEvent(int points) {
        super(null);
        this.points = points;
    }

    /**
     * Gibt die aktuellen Punkte des Spielers zur�ck.
     *
     * @return Punkte des Spielers
     */
    int getPoints() {
        return points;
    }
}
