package uk.ac.ncl.cemdit.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnOffWidget extends JPanel implements ActionListener {

    private String returnString = "";
    
    public OnOffWidget(String title) {
        super();
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(150,75));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        getBorder()));
        ButtonGroup flatten = new ButtonGroup();
        JRadioButton flattenOn = new JRadioButton("On");
        flattenOn.addActionListener(this);
        JRadioButton flattenOff = new JRadioButton("Off");
        flattenOff.addActionListener(this);
        flatten.add(flattenOn);
        flatten.add(flattenOff);

        add(flattenOn);
        add(flattenOff);

    }

    public String getReturnString() {
        return returnString;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("On")) {
            returnString = "On";
        }
        if (e.getActionCommand().equals("Off")) {
            returnString = "Off";
        }
    }
}
