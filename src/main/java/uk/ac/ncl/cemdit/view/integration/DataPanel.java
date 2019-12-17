package uk.ac.ncl.cemdit.view.integration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DataPanel extends JPanel{

    private JPanel buttons = new JPanel();
    private JButton openProvExpl = new JButton("Bind Data to Template");

    private JPanel tablePanel = new JPanel(new BorderLayout());
    private DataTable dataTable = new DataTable();
    private JScrollPane sp_dataPanel;


    /**
     * Panel to hold buttons and data table
     */
    public DataPanel(ActionListener actionListener) {
        super();
        // This class won't handle its own events. Use this actionListener to do it.
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
