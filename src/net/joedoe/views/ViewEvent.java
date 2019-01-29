package net.joedoe.views;

import javafx.event.Event;

class ViewEvent extends Event {
    private ViewController.View view;

    ViewEvent(ViewController.View view) {
        super(null);
        this.view = view;
    }

    ViewController.View getView() {
        return view;
    }
}