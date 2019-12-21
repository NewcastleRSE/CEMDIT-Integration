package uk.ac.ncl.cemdit.model.integration;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class MatchTableModel extends AbstractTableModel {
    private Logger logger = Logger.getLogger(this.getClass());
    private String[] columnNames = {"Source",
            "Operator",
            "Target",
            "Match"};

    private ArrayList<QueryResults> data = new ArrayList<>();

    public MatchTableModel() {
        super();
    }

    public void setData(ArrayList<QueryResults> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return data.get(rowIndex).getLabel();
            case 1:
                return data.get(rowIndex).getOperator();
            case 2:
                return data.get(rowIndex).getValue();
            case 3:
                return data.get(rowIndex).getNewoperator();

        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        QueryResults q = data.get(row);
        q.setNewoperator((String)value.toString().substring(0,1));
        fireTableCellUpdated(row, col);
    }
}
