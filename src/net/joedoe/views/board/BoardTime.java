package net.joedoe.views.board;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import net.joedoe.utils.Timer;
import net.joedoe.views.ToolBar;
import net.joedoe.views.board.StatusEvent.Status;

import java.io.File;
import java.time.LocalTime;

public class BoardTime extends Board {
    private Timer timer;

    public BoardTime(EventHandler<Event> listener) {
        super();
        timer = new Timer();
        timer.setListener(() -> Platform.runLater(() -> info.setText(timer.getStringTime())));
        setLayout(listener);
        controls.setVisible(false);
    }

    @Override
    void setLayout(EventHandler<Event> listener) {
        setLayout();
        ToolBar toolbar = new ToolBar("Time mode");
        toolbar.setListener(e -> {
            timer.stop();
            player.pause();
            listener.handle(new Event(null));
        });
        setTop(toolbar);
        view.setImage(new Image("file:assets" + File.separator + "images" + File.separator + "clock.png"));
        info.setText(timer.getStringTime());
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

    public LocalTime getTime(){
        return timer.getTime();
    }

    @Override
    public void close() {
        super.close();
        if (timer != null) timer.shutdown();
    }
}
