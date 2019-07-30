package uk.ac.ncl.cemdit.model.integration;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * The methods in this class allow the JTable component to get * and display data about
 * the files in a specified directory. * It represents a table with six columns: filename,
 * size, modification date, * plus three columns for flags: directory, readable, writable.
 **/
public class IntegrationDataModel extends AbstractTableModel {
    protected File dir = new File(System.getProperty("user.home"));
    protected ArrayList<ArrayList<Object>> data = new ArrayList();
    protected Object[] columnClasses;
    protected String[] columnNames = {};

    // This table model works for any one given directory
    public IntegrationDataModel() {
        this.dir = dir;
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
        return (Class)columnClasses[col];
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    // The method that must actually return the value of each cell
    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row).get(col);
    }
}