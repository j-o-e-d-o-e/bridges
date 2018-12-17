package net.joedoe.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.utils.Coordinate;
import net.joedoe.views.IsleListener;

/**
 * Verwaltet die aktuellen Br�cken-Linien und Inseln im View.
 *
 */
public class GridController {
    private List<IslePane> panes = new ArrayList<>();
    private List<BridgeLine> lines = new ArrayList<>();
    private IsleListener listener;

    /**
     * Wird Listener �bergeben, um {@link net.joedoe.views.Grid} �ber angeklickte
     * Insel zu benachrichtigen.
     * 
     * @param listener
     *            beobachtet Inseln auf Klicks
     */
    public GridController(IsleListener listener) {
        this.listener = listener;
    }

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Falls bereits zwei Br�cken
     * mit den beiden Koordinaten existieren, wird die Methode beendet. Falls eine
     * Br�cke existiert, wird eine zweite hinzugef�gt und beide versetzt platziert.
     * Falls keine Br�cke existiert, die die beiden Koordinaten miteinander
     * verbindet, wird eine neue, zentrierte Br�cke hinzugef�gt.
     * 
     * @param bridge
     *            hinzuzuf�gende Br�cke (Modell)
     */
    public BridgeLine addLine(IBridge bridge) {
        updateLines();
        // suche existierende Linien mit diesen Koordinaten
        List<BridgeLine> lines = getLines(bridge.getStart(), bridge.getEnd());
        BridgeLine line;
        // neue Linie
        if (lines.size() == 0) {
            line = new BridgeLine(bridge.getStart(), bridge.getEnd(), false);
            this.lines.add(line);
            return line;
        } // existierende Linie wird versetzt und neue Linie ebenfalls versetzt
        else if (lines.size() == 1) {
            line = lines.get(0);
            line.setOffset(true);
            line = new BridgeLine(line.getEnd(), line.getStart(), true);
            this.lines.add(line);
            return line;
        } else {
            return null;
        }
    }

    /**
     * Entfernt Br�cken-Linie, falls m�glich. Falls zwei Br�cken mit den beiden
     * Koordinaten existieren, wird die erste entfernt und die zweite zentriert
     * dargestellt. Falls eine Br�cke existiert, wird diese entfernt. Falls keine
     * Br�cke existiert, die die beiden Koordinaten miteinander verbindet, wird die
     * Methode beendet.
     * 
     * @param bridge
     *            zu entferndende Br�cke (Modell)
     */
    public BridgeLine removeLine(IBridge bridge) {
        updateLines();
        List<BridgeLine> lines = getLines(bridge.getStart(), bridge.getEnd());
        BridgeLine line;
        // falls keine entsprechende Linie existiert
        if (lines.size() == 0) {
            return null;
        } // falls eine Linie existiert, diese entfernen
        else if (lines.size() == 1) {
            line = lines.get(0);
            this.lines.remove(line);
            return line;
        } // falls zwei Linie existieren
        else {
            line = lines.get(0);
            // erste Linie zentrieren
            line.setOffset(false);
            // zweite Linie entfernen
            line = lines.get(1);
            this.lines.remove(line);
            return line;
        }
    }

    /**
     * Gibt alle Br�cken-Linien zur�ck, die die beiden Koordinaten enthalten.
     * 
     * @param start
     *            erste Koordinate
     * @param end
     *            zweite Koordinate
     * @return Liste mit Br�cken-Linien (View), die die beiden Koordinaten
     *         miteinander verbinden
     */
    private List<BridgeLine> getLines(Coordinate start, Coordinate end) {
        return lines.stream().filter(bridge -> bridge.contains(start, end)).collect(Collectors.toList());
    }

    /**
     * Setzt die Farbe der zuletzt hinzugef�gten Br�cken-Linie auf die
     * Standard-Farbe, bevor eine neue Br�cken-Linie hinzugef�gt wird.
     */
    public void updateLines() {
        if (lines.size() > 0) lines.get(lines.size() - 1).setStdColor();
    }

    /**
     * L�dt Br�cken-Linien.
     * 
     * @param bridges
     *            Liste mit zu ladenen Br�cken
     */
    public void setLines(List<IBridge> bridges) {
        for (IBridge bridge : bridges) {
            BridgeLine line = new BridgeLine(bridge.getStart(), bridge.getEnd(), false);
            line.setStdColor();
            lines.add(line);
            if (bridge.isDoubleBridge()) {
                line.setOffset(true);
                line = new BridgeLine(bridge.getEnd(), bridge.getStart(), true);
                line.setStdColor();
                lines.add(line);
            }
        }
    }

    /**
     * Gibt aktuelle Br�cken-Linien zur�ck.
     * 
     * @return Liste mit aktuellen Br�cken-Linien.
     */
    public List<BridgeLine> getLines() {
        return lines;
    }

    /**
     * L�dt Inseln im View.
     * 
     * @param isles
     *            Liste mit zu ladenen Inseln
     */
    public void setPanes(List<IIsle> isles) {
        for (IIsle i : isles) {
            IslePane isle = new IslePane(i.getPos(), i.getBridges());
            isle.setOnMouseClicked(e -> listener.handle(e));
            this.panes.add(isle);
        }
    }

    /**
     * Gibt Inseln zur�ck.
     * 
     * @return Liste mit aktuellen Inseln
     */
    public List<IslePane> getPanes() {
        return panes;
    }

    /**
     * Setzt den Spielstatus zur�ck. Br�cken werden entfernt.
     */
    public void reset() {
        lines.clear();
    }
}
