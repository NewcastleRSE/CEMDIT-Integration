package uk.ac.ncl.cemdit.view.utilities;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

/**
 * Orders a provn file such that lines are in the order:
 * 1. namespaces
 * 2. entities
 * 3. relations
 */
public class OrderFile extends JFrame implements ActionListener,
        ItemListener {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem menuItem = new JMenuItem("Open File", KeyEvent.VK_O);


    OrderFile() {
        super("Order file");
        menuItem.addActionListener(this);
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        menu.add(menuItem);

        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        setSize(1024, 768);
    }

    static public void main(String[] args) {
        OrderFile orderFile = new OrderFile();
    }

    private void openFile(String infile) {

        String outfile = infile.replace(".provn", "_sorted.provn");
        try {
            Scanner sc = new Scanner(new File(infile));
            Vector<String> elements = new Vector<>();
            Vector<String> relations = new Vector<>();
            Vector<String> namespaces = new Vector<>();
            String def = "";

            while (sc.hasNext()) {
                String line = sc.nextLine().trim();
                if (line.startsWith("agent") || line.startsWith("entity") || line.startsWith("activity")) {
                    elements.add(line);
                } else if (line.startsWith("prefix")) {
                    namespaces.add(line);
                } else if (line.startsWith("default")) {
                    def = line;
                } else if (line.toLowerCase().startsWith("document") || line.toLowerCase().startsWith("end") || line.equals("")) {

                } else {
                    relations.add(line);
                }
            }
            sc.close();
            PrintWriter pw = new PrintWriter(new File(outfile));
            pw.println("document\n");
            pw.println(def + "\n");
            for (int i = 0; i < namespaces.size(); i++) {
                pw.println(namespaces.get(i));
            }
            pw.println("");
            for (int i = 0; i < elements.size(); i++) {
                pw.println(elements.get(i));
            }
            pw.println("");
            for (int i = 0; i < relations.size(); i++) {
                pw.println(relations.get(i));
            }
            pw.println("");
            pw.println("endDocument");
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            openFile(filename);

            //This is where a real application would open the file.
        } else {
            JOptionPane.showMessageDialog(this, "Action cancelled.");
        }
    }



    @Override
    public void itemStateChanged(ItemEvent e) {
        System.out.println(e.getItem());

    }
}
