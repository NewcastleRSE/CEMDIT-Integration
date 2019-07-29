package uk.ac.ncl.cemdit;

import javax.swing.*;
import javax.swing.table.*;
import java.io.File;
import java.util.Date;

public class FileTableDemo {
    public static void main(String[] args) {

// Figure out what directory to display
        File dir;
        if (args.length > 0) dir = new File(args[0]);
        else
            dir = new File(System.getProperty("user.home"));

// Create a TableModel object to represent the contents of the directory
        FileTableModel model = new FileTableModel(dir);

// Create a JTable and tell it to display our model
        JTable table = new JTable(model);

// Display it all in a scrolling window and make the window appear
        JFrame frame = new JFrame("FileTableDemo");
        frame.getContentPane().add(new JScrollPane(table), "Center");
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}

