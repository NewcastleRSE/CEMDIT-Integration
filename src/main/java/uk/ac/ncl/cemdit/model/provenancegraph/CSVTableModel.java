package uk.ac.ncl.cemdit.model.provenancegraph;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Table Model for displaying CSV file in a spreadsheet format
 */
public class CSVTableModel extends AbstractTableModel {
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<Object[]> data = new ArrayList<>();
    private int numberOfColumns = 0;
    private int numberOfRows = 0;

    public CSVTableModel() {
        //load(new File("/home/campus.ncl.ac.uk/njss3/blurandcollapse/ProvenanceExplorer/data/UO/CCTV.csv"));

    }

    public void load(File file) {
        columnNames.clear();
        data.clear();
        try {
            Scanner sc = new Scanner(file);
            // Read first line of header
            String line = sc.nextLine();
            String[] columns = line.split(",");
            // Read second line of header;
            line = sc.nextLine();
            String[] secondLine = line.split(",");
            for (int i = 0; i < columns.length; i++) {
                columnNames.add(columns[i] + " " + secondLine[i]);
            }

            while (sc.hasNext()) {
                line = sc.nextLine();
                data.add(line.split(","));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        numberOfColumns = columnNames.size();
        numberOfRows = data.size();
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }


}
