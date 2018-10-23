package net.joedoe.views;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static net.joedoe.GameInfo.RADIUS;

class Grid extends GridPane {
    @SuppressWarnings("FieldCanBeLocal")
    private List<StackPane> circles = new ArrayList<>();

    Grid() {
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        grid.setVgap(offset * 2);
//        grid.setHgap(offset * 2);
//        grid.getStyleClass().add("grid");
        setGridLinesVisible(true);
        for (int i = 0; i < 5; i++) {
            StackPane stack = new StackPane();
            Circle circle = new Circle(RADIUS, Color.GREEN);
            Text text = new Text(Integer.toString((int) (Math.random() * 8) + 1));
            stack.getChildren().addAll(circle, text);
            GridPane.setConstraints(stack, 5 - i, 5 - i);
            circles.add(stack);
        }
        getChildren().addAll(circles);
    }
}
