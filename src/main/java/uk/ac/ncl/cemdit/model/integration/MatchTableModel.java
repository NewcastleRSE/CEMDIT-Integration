package uk.ac.ncl.cemdit.model.integration;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class MatchTableModel extends AbstractTableModel {
    String[] columnNames = {"Source",
            "Operator",
            "Target",
            "Match"};

    ArrayList<QueryResults> data = new ArrayList<>();

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

        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

}
