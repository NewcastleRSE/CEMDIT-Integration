package uk.ac.ncl.cemdit.view.integration;

import javax.swing.*;
import javax.swing.table.TableModel;

public class MatchTable extends JTable {

    public MatchTable() {
        super();
    }

    /**
     * Set the datamodel for the table
     * @param matchTableModel
     */
    public void setDataModel(TableModel matchTableModel) {
        this.setModel(matchTableModel);
    }

}
