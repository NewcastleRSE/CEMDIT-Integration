package uk.ac.ncl.cemdit.view.integration;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MatchTableCBCellRenderer extends JComboBox implements TableCellRenderer {
    public MatchTableCBCellRenderer(String[] items) {
        super(items);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelectedItem(value);
        return this;
    }
}
