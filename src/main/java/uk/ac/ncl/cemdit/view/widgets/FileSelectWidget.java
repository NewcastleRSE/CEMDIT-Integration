package uk.ac.ncl.cemdit.view.widgets;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class FileSelectWidget extends JPanel {

    private String returnString = null;
    private JFileChooser jfc;
    private JTextField filename = new JTextField();
    JPanel parent = this;

    public FileSelectWidget(String heading, String lastdir, String extension) {
        super();
        jfc = new JFileChooser(lastdir);
        filename.setColumns(30);
        filename.
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(350, 75));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(heading),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        getBorder()));
        filename.setEditable(true);
        filename.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filename.setText(null);
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (extension != null) {
                    FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("", extension);
                    jfc.setFileFilter(extensionFilter);
                }
                jfc.setMultiSelectionEnabled(false);
                int i = jfc.showOpenDialog(parent);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File input_file = jfc.getSelectedFile();
                    filename.setText(input_file.getAbsolutePath());
                    returnString = filename.getText();
                } else {
                    filename.setText(null);
                    returnString = null;
                }

            }


            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
        add(filename);
    }

    public String getReturnString() {
        returnString = (filename.getText().equals("")?null:filename.getText());
        return returnString;
    }

}
