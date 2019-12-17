package uk.ac.ncl.cemdit.view.integration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataPanel extends JPanel{
    /**
     * This class won't handle its own events. Use this actionListener to do it.
     */
    ActionListener actionListener;

    JPanel buttons = new JPanel();
    JButton openProvExpl = new JButton("Bind Data to Template");

    JPanel tablePanel = new JPanel(new BorderLayout());
    DataTable dataTable = new DataTable();
    private JScrollPane sp_dataPanel;


    /**
     * Panel to hold buttons and data table
     */
    public DataPanel(ActionListener actionListener) {
        super();
        this.actionListener = actionListener;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        openProvExpl.addActionListener(actionListener);
        sp_dataPanel = new JScrollPane(dataTable);
        sp_dataPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        buttons.add(openProvExpl);
        add(buttons);
        tablePanel.add(sp_dataPanel);
        add(tablePanel);
    }

    public JButton getOpenProvExpl() {
        return openProvExpl;
    }

    public void setOpenProvExpl(JButton openProvExpl) {
        this.openProvExpl = openProvExpl;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
}
