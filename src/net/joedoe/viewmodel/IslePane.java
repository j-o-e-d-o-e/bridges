package net.joedoe.viewmodel;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.joedoe.utils.Coordinate;

import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;
import static net.joedoe.utils.GameInfo.STD_COLOR;

/**
 * Repräsentiert eine Insel im View.
 */
public class IslePane extends StackPane {
    private final Coordinate pos;
    private Circle circle;
    private Label label;

    /**
     * Insel werden Koordinate und Brücken-Anzahl übergeben.
     *
     * @param pos         Koordinate, wo die Insel auf dem Raster platziert wird
     * @param bridgeCount Anzahl an Brücken, die sie anzeigt
     */
    public IslePane(Coordinate pos, int bridgeCount) {
        this.pos = pos;
        circle = new Circle(CIRCLE_RADIUS, STD_COLOR);
        label = new Label(Integer.toString(bridgeCount));
        getChildren().addAll(circle, label);
    }

    /**
     * Gibt x-Wert der Insel-Koordinate zurück.
     *
     * @return x-Wert der Insel-Koordinate
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Gibt y-Wert der Insel-Koordinate zurück.
     *
     * @return y-Wert der Insel-Koordinate
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Gibt Insel-Koordinate zurück.
     *
     * @return Insel-Koordinate
     */
    public Coordinate getPos() {
        return pos;
    }

    /**
     * Legt die Zahl fest, die in der Insel angezeigt wird.
     *
     * @param text Anzahl an (fehlenden) Brücken
     */
    public void setText(String text) {
        label.setText(text);
    }

    /**
     * Legt die Farbe der Insel fest.
     *
     * @param color Farbe des Insel-Kreises
     */
    public void setColor(Color color) {
        circle.setFill(color);
    }

    @Override
    public String toString() {
        return "IslePane{" + "y=" + pos.getY() + ", x=" + pos.getX() + '}';
    }
}
