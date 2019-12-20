/**
 * https://www.tutorialspoint.com/java-program-to-add-combo-box-to-jtable
 */
package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class MatchTable extends JTable {
    private Logger logger = Logger.getLogger(this.getClass());
    private String[] relations = {"=", "<", ">", "!"};
    private JComboBox<String> relList = new JComboBox(relations);

    public MatchTable() {
        super();
    }

    /**
     * Set the datamodel for the table
     *
     * @param matchTableModel
     */
    public void setDataModel(TableModel matchTableModel) {
        this.setModel(matchTableModel);
        TableColumn tableColumn = this.getColumnModel().getColumn(3);
        tableColumn.setCellEditor(new DefaultCellEditor(relList));
        logger.trace("Cell editor set");
   }


}
