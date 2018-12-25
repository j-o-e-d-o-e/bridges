package net.joedoe.viewmodel;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import static net.joedoe.utils.GameInfo.*;

/**
 * Repräsentiert eine Brücke im View-Modell.
 */
public class BridgeLine {
    private final Coordinate start, end;
    private final Alignment alignment;
    private Line line = new Line();

    /**
     * Brücke werden zwei Koordinaten übergeben und ein boolean, ob sie versetzt
     * platziert werden soll.
     *
     * @param start  erste Koordinate
     * @param end    zweite Koordinate
     * @param offset falls true, Linie wird versetzt platziert
     */
    public BridgeLine(Coordinate start, Coordinate end, boolean offset) {
        this.start = start;
        this.end = end;
        alignment = Alignment.getAlignment(start.getY(), end.getY());
        line.setStroke(Color.web("#ff4500"));
        line.setStrokeWidth(1.5);
        translateToLayout(offset);
    }

    /**
     * Übersetzt Koordinaten in {@link javafx.scene.layout.GridPane}.
     *
     * @param offset true, falls Linie versetzt platziert werden soll
     */
    private void translateToLayout(boolean offset) {
        if (alignment == Alignment.HORIZONTAL) {
            int tiles = Math.abs(start.getX() - end.getX());
            line.setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getX() < end.getX()) line.setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
            else line.setTranslateX(-ONE_TILE * (tiles - 1) - BRIDGE_OVERLAP);
        } else {
            int tiles = Math.abs(start.getY() - end.getY());
            line.setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getY() < end.getY()) line.setTranslateY((ONE_TILE >> 1) * tiles);
            else line.setTranslateY(-(ONE_TILE >> 1) * tiles);
        }
        setOffset(offset);
    }

    /**
     * Platziert Linie, die die Brücke repräsentiert, zentriert oder versetzt.
     *
     * @param offset true, Linie wird versetzt platziert - andernfalls zentriert
     */
    void setOffset(boolean offset) {
        if (alignment == Alignment.HORIZONTAL) {
            if (offset) {
                if (start.getX() < end.getX()) line.setTranslateY(BRIDGE_OFFSET);
                else line.setTranslateY(-BRIDGE_OFFSET);
            } else line.setTranslateY(0);
        } else {
            if (offset) {
                if (start.getY() < end.getY()) line.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
                else line.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
            } else line.setTranslateX(ONE_TILE >> 1);
        }
    }

    /**
     * Identifiziert, ob es sich um eine Brücke handelt, die die erste und zweite
     * Koordinate miteinander verbindet.
     *
     * @param start erste Koordinate
     * @param end   zweite Koordinate
     * @return true, falls diese Brücke die beiden Koordinaten miteinander verbindet
     */
    public boolean contains(Coordinate start, Coordinate end) {
        return (this.start == start && this.end == end) || (this.start == end && this.end == start);
    }

    /**
     * Gibt den x-Wert der Koordinate zurück, wo die Linie beginnt.
     *
     * @return x-Wert der Koordinate
     */
    public int getStartX() {
        return start.getX();
    }

    /**
     * Gibt den y-Wert der Koordinate zurück, wo die Linie beginnt.
     *
     * @return y-Wert der Koordinate
     */
    public int getStartY() {
        return start.getY();
    }

    /**
     * Gibt erste Koordinate der Brücke zurück.
     *
     * @return erste Koordinate
     */
    public Coordinate getStart() {
        return start;
    }

    /**
     * Gibt zweite Koordinate der Brücke zurück.
     *
     * @return zweite Koordinate
     */
    public Coordinate getEnd() {
        return end;
    }

    /**
     * Färbt die Linie in der Standard-Farbe.
     */
    void setStdColor() {
        line.setStroke(BRIDGES_STD_COLOR);
    }

    /**
     * Gibt die Linie zurück.
     *
     * @return Linie, die die Brücke im View repräsentiert
     */
    public Line getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "BridgeLine{" + "start=" + start + ", end=" + end + '}';
    }
}