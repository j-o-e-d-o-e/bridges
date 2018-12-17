package net.joedoe.views;

import javafx.event.Event;

/**
 * Enth�lt aktuellen Spielstatus, �ber den die Status-Zeile im
 * {@link net.joedoe.views.MainFrame} informiert wird.
 */
class StatusEvent extends Event {
    private static final long serialVersionUID = 225178481951279734L;
    private String status;

    /**
     * Wird Text �bergeben, der den aktuellen Spielstatus beschreibt: Entweder
     * "Enth�lt Fehler.", "Nicht mehr l�sbar.", "Gel�st!" oder "Noch nicht gel�st."
     *
     * @param status Text, der den aktuellen Spielstatus beschreibt
     */
    StatusEvent(String status) {
        super(null);
        this.status = status;
    }

    /**
     * Gibt den aktuellen Spiel-Status zur�ck.
     *
     * @return Text zum aktuellen Spielstatus
     */
    String getStatus() {
        return status;
    }
}
