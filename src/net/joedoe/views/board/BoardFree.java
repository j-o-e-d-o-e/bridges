package net.joedoe.views.board;

import javafx.event.Event;
import javafx.event.EventHandler;
import net.joedoe.views.ToolBar;
import net.joedoe.views.board.StatusEvent.Status;

public class BoardFree extends Board {

    public BoardFree(EventHandler<Event> listener) {
        super();
        setLayout(listener);
    }

    @Override
    void setLayout(EventHandler<Event> listener) {
        setLayout();
        ToolBar toolbar = new ToolBar("Free mode");
        toolbar.setListener(e -> {
            player.pause();
            listener.handle(new Event(null));
        });
        setTop(toolbar);
    }

    @Override
    void reset() {
        gameManager.resetTempPoints();
        info.setText(Integer.toString(gameManager.getTempPoints()));
        grid.reset();
    }

    @Override
    public void setGrid() {
        super.setGrid();
        gameManager.resetTempPoints();
    }

    @Override
    void handlePoints(PointEvent e) {
        gameManager.addPoints(e.getPoints());
        info.setText(Integer.toString(gameManager.getTempPoints()));
    }

    @Override
    void handleStatus(StatusEvent e) {
        Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == Status.SOLVED) {
            showAlert("Solved.");
            next.handle(new Event(null));
        }
    }
}
