package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

class Grid extends GridPane {
    private List<Isle> isles = new ArrayList<>();
    private EventHandler<Event> listener;

    Grid() {
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        grid.setVgap(offset * 2);
//        grid.setHgap(offset * 2);
//        grid.getStyleClass().add("grid");
        setGridLinesVisible(true);
        for (int i = 0; i < 20; i++) {
            if (i % 2 != 0)
                continue;
            Isle isle = new Isle();
            isle.setBridges(i);
            isle.setOnMouseClicked(e -> {
                isle.getCircle().setFill(Color.RED);
                listener.handle(e);
            });
            GridPane.setConstraints(isle, i, i);
            isles.add(isle);
        }
        getChildren().addAll(isles);
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }

    void setShowMissingBridges(boolean showMissingBridges) {
        for (Isle isle : isles) {
            isle.setText(showMissingBridges);
        }
    }
}
