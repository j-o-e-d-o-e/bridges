package net.joedoe.views.board;

import javafx.event.Event;

/**
 * Enth�lt aktuellen Spielstatus, �ber den die Status-Zeile im
 * {@link Board} informiert wird.
 */
class StatusEvent extends Event {
    private static final long serialVersionUID = 225178481951279734L;
    private Status status;

    /**
     * Wird Text �bergeben, der den aktuellen Spielstatus beschreibt: Entweder
     * "Enth�lt Fehler.", "Nicht mehr l�sbar.", "Gel�st!" oder "Noch nicht gel�st."
     *
     * @param status Text, der den aktuellen Spielstatus beschreibt
     */
    StatusEvent(Status status) {
        super(null);
        this.status = status;
    }

    Status getStatus() {
        return status;
    }

    public enum Status {
        ERROR("Error . . ."), SOLVED("Solved!"), UNSOLVABLE("Unsolvable . . ."), NOT_SOLVED("Not solved, yet.");

        private String text;

        Status(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
