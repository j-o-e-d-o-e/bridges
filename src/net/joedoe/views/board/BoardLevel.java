package net.joedoe.views.board;

import javafx.event.Event;
import javafx.event.EventHandler;
import net.joedoe.views.ToolBar;
import net.joedoe.views.board.StatusEvent.Status;

public class BoardLevel extends Board {
    private ToolBar toolbar;

    public BoardLevel(EventHandler<Event> listener) {
        super();
        setLayout(listener);
    }

    @Override
    void setLayout(EventHandler<Event> listener) {
        setLayout();
        toolbar = new ToolBar("Level " + gameManager.getLevel() + "/25");
        toolbar.setListener(e -> {
            player.pause();
            listener.handle(new Event(null));
        });
        setTop(toolbar);
    }

    @Override
    void reset() {
        gameManager.resetTempPoints();
        info.setText(Integer.toString(gameManager.getAllPoints()));
        grid.reset();
    }

    @Override
    public void setGrid() {
        super.setGrid();
        info.setText(Integer.toString(gameManager.getAllPoints()));
    }

    @Override
    void handleStatus(StatusEvent e) {
        Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == Status.SOLVED) {
            showAlert("Level " + gameManager.getLevel() + " solved.");
            info.setText(Integer.toString(gameManager.getAllPoints()));
            next.handle(new Event(null));
        }
    }

    public void updateToolbar() {
        toolbar.updateTitle("Level " + gameManager.getLevel() + "/25");
    }
}
