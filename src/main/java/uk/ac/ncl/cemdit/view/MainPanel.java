package uk.ac.ncl.cemdit.view;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {
    private TextPanes textPanes = new TextPanes();
    private RightHandPanes rightHandPanes = new RightHandPanes();
    private ButtonPanel buttonPanel;
    private ComponentPointers componentPointers = ComponentPointers.getInstance();
    private Logger logger = Logger.getLogger(this.getClass());

    public MainPanel(JFrame frame) {


        buttonPanel = new ButtonPanel();
        componentPointers.setFrame(frame);
        componentPointers.setTextPanes(textPanes);
        componentPointers.setRightHandPanes(rightHandPanes);
        componentPointers.setButtonPanel(buttonPanel);

        this.setLayout(new BorderLayout());

        JScrollPane svgScrollPane = new JScrollPane(rightHandPanes);
        svgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        svgScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JScrollPane textScrollPane = new JScrollPane(textPanes);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPanes, rightHandPanes);
        splitPane.setResizeWeight(0.5);
        this.add(buttonPanel, BorderLayout.PAGE_START);
        this.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getParent().dispatchEvent(e);
    }
}
