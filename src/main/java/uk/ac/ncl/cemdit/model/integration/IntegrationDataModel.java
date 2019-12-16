package uk.ac.ncl.cemdit.model.integration;

import org.apache.log4j.Logger;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This model sits behind the JTable that displays the data returned by the REST query
 **/
public class IntegrationDataModel extends AbstractTableModel {
    final static Logger logger = Logger.getLogger(IntegrationDataModel.class);
    protected ArrayList<ArrayList<Object>> data = new ArrayList();
    protected Object[] columnClasses;
    protected String[] columnNames = {};

    // This table model works for any one given directory
    public IntegrationDataModel() {
        // Store a list of files in the directory
    }

    public void setData(ArrayList<ArrayList<Object>> data) {
        this.data = data;
    }

    public void setClasses(Class[] columnClasses) {
        this.columnClasses = columnClasses;
    }

    // These are easy methods
    @Override
    public int getColumnCount() {
        ArrayList<Class> classes = new ArrayList<>();
        for (int i = 0; i < columnNames.length; i++) {
            classes.add(String.class);
        }
        columnClasses = classes.toArray();
        return columnNames.length;
    }

    // A constant for this model
    @Override
    public int getRowCount() {
        return data.size();
    }

    // # of files in dir
    // Information about each column
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return (Class) columnClasses[col];
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setColumnNames(ArrayList columnNames) {
        this.columnNames = new String[columnNames.size()];
        columnNames.forEach((name)->logger.trace((String)name));
        for (int i = 0; i < columnNames.size(); i++) {
            this.columnNames[i] = (String)columnNames.get(i);
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (getRowCount() > 0 && getColumnCount() > 0) {
            return data.get(row).get(col);
        } else
            return null;
    }

    public String getRowAsCSV(int row) {
        String returnValue = "";
        for (int col = 0; col < getColumnCount(); col++) {
            returnValue += getValueAt(row, col);
            if (col < getColumnCount() - 1)
                returnValue += ",";
        }
        return returnValue;
    }

    public LinkedHashMap<String, Object> getRowAsHashMap(int row) {
        LinkedHashMap returnValue = new LinkedHashMap();
        for (int col = 0; col < getColumnCount(); col++) {
            returnValue.put(getColumnName(col),getValueAt(row, col));
        }
        return returnValue;

    }

    public String getColumnNamesAsCSV() {
        String returnValue = "";
        for (int col = 0; col < getColumnCount(); col++) {
            returnValue += getColumnName(col);
            if (col < getColumnCount() - 1)
                returnValue += ",";
        }
        return returnValue;
    }
}