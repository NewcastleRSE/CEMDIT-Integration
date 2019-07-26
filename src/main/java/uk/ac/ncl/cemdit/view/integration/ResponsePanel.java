package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class ResponsePanel extends JPanel implements ListSelectionListener {
    private JList<String> resultsList;
    private ArrayList<String> data;
    private DefaultListModel listModel = new DefaultListModel();
    private Object allEventsListenener = this;
    private Logger logger = Logger.getLogger(this.getClass());


    public ResponsePanel(ArrayList d, Object eventsListener) {
        super();
        allEventsListenener = eventsListener;
        setLayout(new BorderLayout());

        for (int i = 0; i < 8;i++) {
            d.add("  ");
        }
        populateList(d);
        resultsList = new JList(listModel); //data has type Object[]
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setLayoutOrientation(JList.VERTICAL_WRAP);
        resultsList.setVisibleRowCount(8);
        resultsList.addListSelectionListener((ListSelectionListener) allEventsListenener);
        resultsList.setSelectedIndex(0);
        JScrollPane listScroller = new JScrollPane(resultsList);
        listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JViewport jViewport = listScroller.getViewport();
        int w = 900;
        int h = jViewport.getPreferredSize().height;
        jViewport.setPreferredSize(new Dimension(w, h));
        add(listScroller);
    }

    public void populateList(ArrayList d) {
        listModel.clear();
        data = d;
        for (int i = 0; i < d.size(); i++) {
            listModel.addElement(data.get(i));
        }

    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();

        for (int i = 0; i <= lsm.getMaxSelectionIndex(); i++) {
            if (lsm.isSelectedIndex(i)) {
                logger.debug("Selected: " + i);
            }
        }
        logger.debug(lsm.getMinSelectionIndex());
    }

}
