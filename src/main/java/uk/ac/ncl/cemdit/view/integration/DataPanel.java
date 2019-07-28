package uk.ac.ncl.cemdit.view.integration;

import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;

import javax.swing.*;

public class DataPanel extends JTable {

    public DataPanel(IntegrationDataModel integrationDataModel) {
        setModel(integrationDataModel);
    }

    public void populateDataPanel() {

    }

}
