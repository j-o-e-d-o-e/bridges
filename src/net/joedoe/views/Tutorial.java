package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class Tutorial extends Stage {

    Tutorial(){
        setTitle("Neues Rätsel");
        setResizable(false);
        Scene scene = new Scene(setLayout(), 300, 280);
        setScene(scene);
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

//        StackPane innerPane = new StackPane();
//        innerPane.setBorder(new Border(
//                new BorderStroke(STD_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        innerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
//        innerPane.getStyleClass().add("innerPane");
//
//        VBox vBox = new VBox();
//        vBox.setSpacing(CONTAINER_OFFSET);
        outerPane.getStyleClass().add("dialog");

        return outerPane;
    }
}
