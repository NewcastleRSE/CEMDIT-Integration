package uk.ac.ncl.cemdit.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileFormatWidget extends JPanel implements ActionListener {

    private String[] fileFormats = {null, "json", "provn", "ttl", "provx", "xml", "pdf","svg"};
    private String returnString = null;
    JComboBox fileFormatCB;

    public FileFormatWidget(String heading) {
        super();
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(150,75));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(heading),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        getBorder()));
        fileFormatCB = new JComboBox(fileFormats);
        fileFormatCB.setSelectedIndex(0);
        fileFormatCB.addActionListener(this);
        add(fileFormatCB);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        returnString = (String)cb.getSelectedItem();
    }

    public String getReturnString() {
        return returnString;
    }

    public String[] getFileFormats() {
        return fileFormats;
    }

    public void setFileFormats(String[] fileFormats) {
        this.fileFormats = fileFormats;
        fileFormatCB.removeAllItems();

        for (int i = 0; i < this.fileFormats.length; i++) {
            fileFormatCB.addItem(this.fileFormats[i]);
        }
    }

 }
