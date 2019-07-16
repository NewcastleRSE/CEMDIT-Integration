package uk.ac.ncl.cemdit.controller;

import java.util.ArrayList;
import java.util.List;

public interface PanelListener {
    public void Catch();
}

class Thrower {
    //list of catchers & corresponding function to add/remove them in the list
    List<PanelListener> listeners = new ArrayList<PanelListener>();

    public void addThrowListener(PanelListener toAdd) {
        listeners.add(toAdd);
    }

    //Set of functions that Throw Events.
    public void Throw() {
        for (PanelListener hl : listeners) hl.Catch();
        System.out.println("Something thrown");
    }
}