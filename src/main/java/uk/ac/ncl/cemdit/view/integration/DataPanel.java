package uk.ac.ncl.cemdit.view.integration;

import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * A table for holding and displaying the results returned by a query
 */
public class DataPanel extends JTable {

    public DataPanel() {
        super();
    }

    /**
     * Set the datamodel for the table
     * @param integrationDataModel
     */
    public void setDataModel(TableModel integrationDataModel) {
        this.setModel(integrationDataModel);
    }

}
