package uk.ac.ncl.cemdit.view.integration;

import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;

import javax.swing.*;
import javax.swing.table.TableModel;

public class DataPanel extends JTable {

    public DataPanel() {
        super();
    }

    public void setDataModel(TableModel integrationDataModel) {
        this.setModel(integrationDataModel);
    }

}
