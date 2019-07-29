package uk.ac.ncl.cemdit;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.Date;

/**
 * The methods in this class allow the JTable component to get * and display data about
 * the files in a specified directory. * It represents a table with six columns: filename,
 * size, modification date, * plus three columns for flags: directory, readable, writable.
 **/
public class FileTableModel extends AbstractTableModel {
    protected File dir;
    protected String[] filenames;
    protected String[] columnNames = new String[]{"name", "size", "last modified", "directory?", "readable?", "writable?"};
    protected Class[] columnClasses = new Class[]{String.class, Long.class, Date.class, Boolean.class, Boolean.class, Boolean.class};

    // This table model works for any one given directory
    public FileTableModel(File dir) {
        this.dir = dir;
        this.filenames = dir.list();
        // Store a list of files in the directory
    }

    // These are easy methods
    public int getColumnCount() {
        return 6;
    }

    // A constant for this model
    public int getRowCount() {
        return filenames.length;
    }

    // # of files in dir
    // Information about each column
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    // The method that must actually return the value of each cell
    public Object getValueAt(int row, int col) {
        File f = new File(dir, filenames[row]);
        switch (col) {
            case 0:
                return filenames[row];
            case 1:
                return new Long(f.length());
            case 2:
                return new Date(f.lastModified());
            case 3:
                return f.isDirectory() ? Boolean.TRUE : Boolean.FALSE;
            case 4:
                return f.canRead() ? Boolean.TRUE : Boolean.FALSE;
            case 5:
                return f.canWrite() ? Boolean.TRUE : Boolean.FALSE;
            default:
                return null;
        }
    }
}