package net.joedoe.views;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import net.joedoe.utils.Coordinate;

import java.io.File;

import static net.joedoe.utils.GameInfo.ISLE_SIZE;

/**
 * Repräsentiert eine Insel im View.
 */
public class IslePane extends StackPane {
    private final Coordinate pos;
    private Label label;

    /**
     * Insel werden Koordinate und Brücken-Anzahl übergeben.
     *
     * @param pos         Koordinate, wo die Insel auf dem Raster platziert wird
     * @param bridgeCount Anzahl an Brücken, die sie anzeigt
     */
    public IslePane(Coordinate pos, int bridgeCount) {
        this.pos = pos;
        label = new Label(Integer.toString(bridgeCount));
        int fontSize = ISLE_SIZE / 2 + 5;
        label.setStyle("-fx-text-fill: #F8F8F8; -fx-font:" + fontSize + " Tahoma; -fx-font-weight: bold;");
        getChildren().add(label);
        setImage();
    }

    private void setImage() {
        String url = "file:assets" + File.separator + "images" + File.separator + "isle.png";
        Image image = new Image(url, ISLE_SIZE, ISLE_SIZE, true, true);
        BackgroundImage bg = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        setBackground(new Background(bg));
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

    @Override
    public String toString() {
        return "IslePane{" + "y=" + pos.getY() + ", x=" + pos.getX() + '}';
    }
}
