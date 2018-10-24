package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import net.joedoe.entities.Isle;
import net.joedoe.entities.MockData;

import java.util.List;

import static net.joedoe.GameInfo.CIRCLE_RADIUS;
import static net.joedoe.GameInfo.ONE_TILE;

class Board extends StackPane {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private EventHandler<Event> listener;
    @SuppressWarnings("FieldCanBeLocal")
    private List<Isle> isles = MockData.ISLES;
    private static final int ISLES_PER_ROW = 4;

    Board() {
        setAlignment(Pos.CENTER);
        getChildren().add(new Grid());
    }

    private void addGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * ONE_TILE);
                tile.setTranslateY(i * ONE_TILE);
                getChildren().add(tile);
            }
        }
    }

    private void addIsles() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Isle isle = new Isle(3, 3, 3);
                isle.setTranslateX(CIRCLE_RADIUS * 2 * (i % ISLES_PER_ROW));
                isle.setTranslateY(CIRCLE_RADIUS * 2 * (i / ISLES_PER_ROW));
                getChildren().add(isle);
            }
        }
//        for (int i = 0; i < isles.size(); i++) {
//            Isle isle = isles.get(i);
//            isle.setTranslateX(CIRCLE_RADIUS * 2 * (i % ISLES_PER_ROW));
//            isle.setTranslateY(CIRCLE_RADIUS * 2 * (i / ISLES_PER_ROW));
////            getChildren().add(isle);
//        }
//        getChildren().addAll(isles);
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }


//    Board() {
////        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        setAlignment(Pos.CENTER);
////        Rectangle border = new Rectangle(50, 50);
////        border.setFill(null);
////        border.setStroke(Color.BLACK);
////        getChildren().add(border);
////        addIsles();
//        getChildren().add(new Grid());
////        addGrid();
//    }
}
