package net.joedoe.views;

import javafx.scene.layout.StackPane;
import net.joedoe.entities.Isle;
import net.joedoe.entities.MockData;

import java.util.List;

import static net.joedoe.GameInfo.ONE_TILE;

public class Grid extends StackPane {
    @SuppressWarnings("FieldCanBeLocal")
    private List<Isle> isles = MockData.ISLES;
    private static final int ISLES_PER_ROW = 4;

    public Grid(){
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * ONE_TILE);
                tile.setTranslateY(i * ONE_TILE);
                getChildren().add(tile);
            }
        }
    }
}
