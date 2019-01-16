package net.joedoe.views;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

class Tutorial extends BorderPane {

    Tutorial(SceneController controller) {
        Button ok = new Button("Ok");
        ok.setOnAction(e -> controller.switchToBoard());
        setCenter(ok);
    }
}
