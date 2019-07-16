package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.QueryResults;
import uk.ac.ncl.cemdit.view.integration.Widgets.DropDownRelationsWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MatchPanel extends JPanel implements ActionListener {
    private JLabel similarity = new JLabel("Similarity: ");
    private Logger logger = Logger.getLogger(this.getClass());
    private ActionListener actionListener = this;

    public MatchPanel() {
        super();
        setLayout(new GridLayout(0,1));

    }

    public JPanel populateMatchPanel(JPanel jPanel, ArrayList<QueryResults> queryResults, Object eventsListener) {
        actionListener = (ActionListener) eventsListener;
        jPanel.removeAll();
        Color color = Color.white;

        add(similarity);
        for (int i = 0; i < queryResults.size(); i++) {
            if (i%2==0) {
                color = Color.white;
            }
            else {
                color= Color.lightGray;
            }
            logger.debug(queryResults.get(i).getLabel());
//            ButtonSelectWidget buttonSelectWidget = new ButtonSelectWidget(queryResults.get(i), color);
//            buttonSelectWidget.setActionListener(actionListener);
//            jPanel.add(buttonSelectWidget);

            DropDownRelationsWidget dropDownRelationsWidget = new DropDownRelationsWidget(queryResults.get(i), color);
            dropDownRelationsWidget.setActionListener(actionListener);
            jPanel.add(dropDownRelationsWidget);

            Insets insets = jPanel.getInsets();
//            Dimension size = buttonSelectWidget.getPreferredSize();
            Dimension size = dropDownRelationsWidget.getPreferredSize();
//            buttonSelectWidget.setBounds(25 + insets.left, i * 30 + insets.top,
//                    1000, 20);
        }
        return jPanel;
    }

    public void setSimilarity(double similarityValue) {
        similarity.setText(similarity.getText() + similarityValue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
    }
}
