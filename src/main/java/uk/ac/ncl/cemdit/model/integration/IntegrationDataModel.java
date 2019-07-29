package uk.ac.ncl.cemdit.model.integration;

import org.apache.log4j.Logger;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class IntegrationDataModel extends AbstractTableModel {
    private Logger logger = Logger.getLogger(this.getClass());
    ArrayList<ArrayList<Object>> data = new ArrayList();
    ArrayList<String> columnName = new ArrayList<>();

    public IntegrationDataModel() {
        super();
    }

    public void setData(ArrayList<ArrayList<Object>> data) {
        this.data = data;
    }

    public ArrayList<ArrayList<Object>> getData() {
        return data;
    }

    public void fire() {
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        if (data.size() > 0)
            return data.get(0).size();
        else
            return 0;
    }

    public void setValueAt(int rowIndex, int columIndex, Object o) {
        data.get(rowIndex).add(columIndex, o);
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    @Override
    public String getColumnName(int col) {
        return columnName.get(col);
    }

    public void setColumName(int columnIndex, String col) {
        columnName.add(columnIndex, col);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
