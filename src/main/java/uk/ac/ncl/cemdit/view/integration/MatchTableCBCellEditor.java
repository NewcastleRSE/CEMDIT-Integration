package uk.ac.ncl.cemdit.view.integration;

import javax.swing.*;

public class MatchTableCBCellEditor extends DefaultCellEditor {

    public MatchTableCBCellEditor(String[] items) {
        super(new JComboBox(items));
    }
}
