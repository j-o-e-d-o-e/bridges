package net.joedoe.views;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static net.joedoe.GameInfo.ONE_TILE;

public class Tile extends StackPane {

    public Tile() {
        Rectangle border = new Rectangle(ONE_TILE, ONE_TILE);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        setAlignment(Pos.CENTER);
        getChildren().add(border);
    }
}
