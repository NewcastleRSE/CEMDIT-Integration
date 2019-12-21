
package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * https://www.tutorialspoint.com/java-program-to-add-combo-box-to-jtable
 */
class MatchTable extends JTable {
    private Logger logger = Logger.getLogger(this.getClass());

    MatchTable() {
        super();
    }

    /**
     * Set the datamodel for the table
     *
     * @param matchTableModel A TableModel for the MatchTable
     */
    void setDataModel(TableModel matchTableModel) {
        this.setModel(matchTableModel);
        logger.trace("Cell editor set");
   }


}
