package net.joedoe.views;

import javafx.event.Event;

class ViewEvent extends Event {
    private View view;

    ViewEvent(View view) {
        super(null);
        this.view = view;
    }

    View getView() {
        return view;
    }
}