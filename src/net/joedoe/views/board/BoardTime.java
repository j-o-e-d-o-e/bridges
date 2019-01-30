package net.joedoe.views.board;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.joedoe.utils.Timer;
import net.joedoe.views.ToolBar;
import net.joedoe.views.board.StatusEvent.Status;

import java.io.File;
import java.time.LocalTime;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public class BoardTime extends Board {
    private String difficulty;
    private Timer timer;

    public BoardTime(EventHandler<Event> listener, String difficulty) {
        super();
        this.difficulty = difficulty;
        timer = new Timer();
        timer.setListener(() -> Platform.runLater(() -> info.setText(timer.getStringTime())));
        setLayout(listener);
    }

    @Override
    void setLayout(EventHandler<Event> listener) {
        setLayout();
        ToolBar toolbar = new ToolBar("Time mode (" + difficulty + ")");
        toolbar.setListener(e -> {
            timer.stop();
            player.pause();
            listener.handle(new Event(null));
        });
        setTop(toolbar);
        view.setImage(new Image("file:assets" + File.separator + "images" + File.separator + "clock.png"));
        info.setText(timer.getStringTime());
    }

    @SuppressWarnings("Duplicates")
    @Override
    VBox createBottom() {
        VBox vBox = new VBox(CONTAINER_OFFSET);
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        checkBox = new CheckBox("Show missing bridges");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> grid.setShowMissingBridges(checkBox.isSelected()));
        HBox controls = new HBox(CONTAINER_OFFSET);
        controls.setAlignment(Pos.CENTER);
        controls.setPrefWidth(100);
        Button undoBtn = new Button("_Undo");
        undoBtn.setAlignment(Pos.CENTER);
        undoBtn.setMnemonicParsing(true);
        undoBtn.setMinWidth(100);
        undoBtn.setOnAction(e -> grid.undoBridge());
        controls.getChildren().add(undoBtn);
        vBox.getChildren().addAll(checkBox, controls, status);
        return vBox;
    }

    @Override
    void reset() {
        timer.restart();
        info.setText(timer.getStringTime());
        grid.reset();
    }

    @Override
    public void setGrid() {
        super.setGrid();
        timer.start();
    }

    @Override
    void handlePoints(PointEvent e) {
        // Nothing happens
    }

    @Override
    void handleStatus(StatusEvent e) {
        Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == Status.SOLVED) {
            timer.stop();
            showAlert("Puzzle solved in " + info.getText() + ".");
            next.handle(new Event(null));
        }
    }

    public void restartTimer() {
        timer.start();
    }

    public LocalTime getTime() {
        return timer.getTime();
    }

    @Override
    public void close() {
        super.close();
        if (timer != null) timer.shutdown();
    }
}
