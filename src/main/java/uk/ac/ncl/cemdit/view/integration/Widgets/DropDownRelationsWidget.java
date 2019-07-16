package uk.ac.ncl.cemdit.view.integration.Widgets;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.QueryResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropDownRelationsWidget extends JPanel implements ActionListener {
    private JPanel jPanel = new JPanel();
    private JLabel title = new JLabel();
    private String[] relations = {"=", "<", ">", "!"};
    private JComboBox relList = new JComboBox(relations);
    private ActionListener actionListener = this;
    private Logger logger = Logger.getLogger(this.getClass());


    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public DropDownRelationsWidget(QueryResults queryResults, Color background) {
        String color = "black";
        switch (queryResults.getOperator()) {
            case "=":
                color = "green";
                break;
            case ">":
                color = "orange";
                break;
            case "X":
                color = "red";
                break;
        }
        relList.setSelectedIndex(0);
        relList.addActionListener(this);
        jPanel.add(relList);
        title.setText("<html>" + queryResults.getLabel() + "<font color=" + color + ">" + queryResults.getOperator()
                + "</font>" + queryResults.getValue() + "</html>");
        add(title, BorderLayout.WEST);
        add(jPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getParent().dispatchEvent(e);
        logger.debug(e.getActionCommand());

    }
}
