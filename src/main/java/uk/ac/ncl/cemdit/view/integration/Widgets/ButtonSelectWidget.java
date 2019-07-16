package uk.ac.ncl.cemdit.view.integration.Widgets;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.QueryResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSelectWidget extends JPanel implements ActionListener {
    private JPanel jPanel = new JPanel();
    private JLabel title = new JLabel();

    private ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton downgrade = new JRadioButton("Downgrade");
    private JRadioButton reject = new JRadioButton("Reject");
    private JRadioButton upgrade = new JRadioButton("Upgrade");
    private JRadioButton accept = new JRadioButton("Accept");
    private Logger logger = Logger.getLogger(this.getClass());
    private ActionListener actionListener = this;

    public ButtonSelectWidget(QueryResults queryResults, Color background) {
        super();
        setBackground(background);
        downgrade.setBackground(background);
        downgrade.setActionCommand(queryResults.getLabel() + "_" + downgrade.getActionCommand());
        reject.setBackground(background);
        reject.setActionCommand(queryResults.getLabel() + "_" + reject.getActionCommand());
        upgrade.setBackground(background);
        upgrade.setActionCommand(queryResults.getLabel() + "_" + upgrade.getActionCommand());
        accept.setBackground(background);
        accept.setActionCommand(queryResults.getLabel() + "_" + accept.getActionCommand());

                setLayout(new BorderLayout());
        jPanel.setLayout(new GridLayout(1, 4));
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
        title.setText("<html>" + queryResults.getLabel() + "<font color=" + color + ">" + queryResults.getOperator()
                + "</font>" + queryResults.getValue() + "</html>");
        buttonGroup.add(downgrade);
        buttonGroup.add(reject);
        buttonGroup.add(upgrade);
        buttonGroup.add(accept);
        jPanel.add(downgrade);
        jPanel.add(reject);
        jPanel.add(upgrade);
        jPanel.add(accept);


        add(title, BorderLayout.WEST);
        add(jPanel, BorderLayout.EAST);

    }

    private String padString(String str) {
        return null;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ActionListener al) {
        actionListener = al;
        accept.addActionListener(actionListener);
        upgrade.addActionListener(actionListener);
        downgrade.addActionListener(actionListener);
        reject.addActionListener(actionListener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getParent().dispatchEvent(e);
        logger.debug(e.getActionCommand());
    }
}
