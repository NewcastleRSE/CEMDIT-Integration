package uk.ac.ncl.cemdit.view;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.Centroid;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.Exceptions.ElementDoesntExistException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidCollapseElementsException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidPolicySyntaxException;
import uk.ac.ncl.cemdit.controller.InvalidBlurLevel;
import uk.ac.ncl.cemdit.controller.ParsePolicy;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;
import java.util.Vector;

import static uk.ac.ncl.cemdit.controller.ProvConvertItems.convertProvN;
import static uk.ac.ncl.cemdit.controller.ProvConvertItems.merge;

public class MenuBar extends JMenuBar implements ActionListener {
    private Logger logger = Logger.getLogger(this.getClass());
    static private Properties properties = new Properties();

    enum tabs {PROVN, POLICY, TEXT, TEMPLATE, DATA, PROCESS}

    private JCheckBoxMenuItem oldStuff;


    private ComponentPointers componentPointers = ComponentPointers.getInstance();


    public MenuBar() {
        super();
        JMenu file;
        JMenuItem newfile;
        JMenuItem openfile;
        JMenuItem savefile;
        JMenuItem newPolicy;
        JMenuItem openPolicy;
        JMenuItem savePolicy;

        JMenu printMenu;
        JMenuItem printProvn;
        JMenuItem printDOT;

        JMenu editMenu;
        JMenuItem settings;

        JMenu viewMenu;
        JMenuItem provGraph;
        JMenuItem polGraph;

        JMenu toolMenu;
        JMenuItem applyPol;
        JMenuItem stats;
        JMenuItem clear;
        JMenuItem merge;
        JMenuItem toolbox;


        file = new JMenu("File");
        file.setMnemonic('F');
        newfile = new JMenuItem("New ProvN", 'N');
        newfile.addActionListener(this);
        newfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        openfile = new JMenuItem("Open ProvN", 'O');
        openfile.addActionListener(this);
        openfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        savefile = new JMenuItem("Save ProvN", 'S');
        savefile.addActionListener(this);
        savefile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        newPolicy = new JMenuItem("New Policy", 'P');
        newPolicy.addActionListener(this);
        newPolicy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        openPolicy = new JMenuItem("Open Policy", 'L');
        openPolicy.addActionListener(this);
        openPolicy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        savePolicy = new JMenuItem("Save Policy", 'V');
        savePolicy.addActionListener(this);
        savePolicy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

        printMenu = new JMenu("Print");
        printMenu.setMnemonic('R');
        printProvn = new JMenuItem("Print ProvN");
        printProvn.addActionListener(this);
        printProvn.setActionCommand("printprovn");
        printDOT = new JMenu("Print DOT");
        printDOT.addActionListener(this);
        printDOT.setActionCommand("printdot");

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        settings = new JMenuItem("Settings");
        settings.addActionListener(this);
        settings.setActionCommand("settings");
        oldStuff = new JCheckBoxMenuItem("Legacy");
        oldStuff.addActionListener(this);
        oldStuff.setActionCommand("legacy");

        viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        provGraph = new JMenuItem("Graph Panel");
        provGraph.addActionListener(this);
        polGraph = new JMenuItem("Policy Applied Panel");
        polGraph.addActionListener(this);

        toolMenu = new JMenu("Tools");
        toolMenu.setMnemonic('T');
        applyPol = new JMenuItem("Apply Policy", 'Y');
        applyPol.addActionListener(this);
        applyPol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        stats = new JMenuItem("Stats", 'T');
        stats.addActionListener(this);
        stats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        clear = new JMenuItem("Clear Stats Area", 'C');
        clear.addActionListener(this);
        clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));

        merge = new JMenuItem("Merge", 'M');
        merge.addActionListener(this);
        merge.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
        toolbox = new JMenuItem("Conversion Tool");
        toolbox.setActionCommand("Toolbox");
        toolbox.addActionListener(this);

        file.add(newfile);
        file.add(openfile);
        file.add(savefile);
        file.add(newPolicy);
        file.add(openPolicy);
        file.add(savePolicy);

        printMenu.add(printProvn);
        printMenu.add(printDOT);

        editMenu.add(settings);
        editMenu.add(oldStuff);

        viewMenu.add(provGraph);
        viewMenu.add(polGraph);

        toolMenu.add(applyPol);
        toolMenu.add(stats);
        toolMenu.add(clear);
        toolMenu.add(merge);
        toolMenu.add(toolbox);

        this.add(file);
        this.add(printMenu);
        this.add(editMenu);
        this.add(viewMenu);
        this.add(toolMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("printprovn")) {
            if (e.getActionCommand().equals("printprovn")) {
                JOptionPane.showMessageDialog(this, "Doesn't work yet!");
            }
        }
        if (e.getActionCommand().equals("printdot")) {
            JOptionPane.showMessageDialog(this, "Doesn't work yet!");
        }
        if (e.getActionCommand().equals("Clear Stats Area")) {
            componentPointers.getRightHandPanes().getStatsTextArea().setText("");
        }
        if (e.getActionCommand().equals("Stats")) {
            componentPointers.getRightHandPanes().getStatsTextArea().append("Average degree: " + Toolkit.averageDegree(componentPointers.getGsProvGraph()) + "\n");
        }
        if (e.getActionCommand().equals("New ProvN")) {
            // clear text panel
            componentPointers.getTextPanes().getProvN().setText("");
            // set tab title
            componentPointers.getTextPanes().setTitleAt(0, "PROVN: Untitled");
            componentPointers.setProvnfile("Untitled");
        }
        if (e.getActionCommand().equals("Open ProvN")) {
            logger.debug("Open provn file");
            // default file opener to the last directory used
            final JFileChooser fc = new JFileChooser(ComponentPointers.getLastDir());
            fc.setCurrentDirectory(new java.io.File(ComponentPointers.getLastDir()));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PROVN", "provn");
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                componentPointers.setProvnfile(file.getAbsolutePath());
                componentPointers.loadProvnFile(file);
                componentPointers.setProperty("lastdir", file.getParent());
            }
            // Select PROVN tab
            tabset(tabs.PROVN);
            Graph graph = componentPointers.getGsProvGraph();
            APSP apsp = new APSP();
            apsp.init(graph);
            apsp.compute();
            Centroid centroid = new Centroid();
            centroid.init(graph);
            centroid.compute();

            String centroidElement = null;
            for (Node n : graph.getEachNode()) {
                Boolean in = n.getAttribute("centroid");
                if (in) {
                    centroidElement = "Centroid: " + n.getId() + "\n";
                }
            }
            componentPointers.getRightHandPanes().getStatsTextArea().append(centroidElement);
            componentPointers.getRightHandPanes().getStatsTextArea().append("Centroid attribute: " + centroid.getCentroidAttribute() + "\n");
        }
        if (e.getActionCommand().equals("Save ProvN")) {
            logger.debug("Save provn file");
            boolean save = false;
            // get name of file to save to
            File file = new File(componentPointers.getTextPanes().getProvnFile());
            // if untitled then get a new name
            if (componentPointers.getProvnfile().equals("Untitled")) {
                JFileChooser fileChooser = new JFileChooser(ComponentPointers.getLastDir());
                fileChooser.setDialogTitle("Save file as");
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    componentPointers.getTextPanes().setProvnFile(file.getAbsolutePath());
                    componentPointers.setProperty("lastdir", file.getPath());
                    save = true;
                }
            } else save = true;
            if (save) {
                // get the text from the text panel
                String text = componentPointers.getTextPanes().getProvN().getText();
                // save the contents of the file
                ComponentPointers.saveFile(file, text);
                componentPointers.getTextPanes().setTitleAt(0, "PROVN: " + componentPointers.getProvnfile());
                // confirm saving the file
                JOptionPane.showMessageDialog(this, "File " + file.getName() + " saved");
                // reload the file and create svg to display
                componentPointers.loadProvnFile(file);
            }
        }
        if (e.getActionCommand().equals("New Policy")) {
            // set tab title to Untitled
            componentPointers.setPolfile("Untitled");
            // clear text panel
            componentPointers.getTextPanes().getPolicy().setText("");
            // set tab title
            componentPointers.getTextPanes().setTitleAt(1, "Policy: " + componentPointers.getPolfile());
        }
        if (e.getActionCommand().equals("Open Policy")) {
            logger.debug("Open policy file");
            // default file opener to the last directory used
            final JFileChooser fc = new JFileChooser(ComponentPointers.getLastDir());
            fc.setCurrentDirectory(new java.io.File(ComponentPointers.getLastDir()));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Policy", "pol");
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                ComponentPointers.setLastDir(file.getParent());
                componentPointers.setPolfile(file.getName());
                try {
                    FileReader fileReader = new FileReader(ComponentPointers.getLastDir() + "/" + componentPointers.getPolfile());
                    logger.debug("Set policy filename as " + file.getName());
                    componentPointers.setPolfile(file.getName());
                    componentPointers.getTextPanes().setTitleAt(1, "Policy: " + componentPointers.getPolfile());
                    componentPointers.getTextPanes().getPolicy().read(fileReader, componentPointers.getPolfile());
                    componentPointers.setProperty("lastdir", file.getParent());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // Select PROVN tab
            tabset(tabs.POLICY);
        }
        if (e.getActionCommand().equals("Save Policy")) {
            logger.debug("savepol");
            boolean save = false;
            if (componentPointers.getPolfile().equals("Untitled")) {
                JFileChooser fileChooser = new JFileChooser(ComponentPointers.getLastDir());
                fileChooser.setDialogTitle("Save file as");
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    componentPointers.getTextPanes().setPolicyFile(fileToSave.getAbsolutePath());
                    componentPointers.setProperty("lastdir", fileToSave.getParent());
                    componentPointers.setPolfile(fileToSave.getName());
                    componentPointers.getTextPanes().setTitleAt(1, "Policy: " + componentPointers.getPolfile());
                    save = true;
                }

            } else save = true;
            if (save) {
                // get the text from the text panel
                String text = componentPointers.getTextPanes().getPolicy().getText();
                // save the contents of the file
                ComponentPointers.saveFile(new File(ComponentPointers.getLastDir() + "/" + componentPointers.getPolfile()), text);
                // confirm saving the file
                JOptionPane.showMessageDialog(this, "File " + componentPointers.getPolfile() + " saved");
            }

        }
        if (e.getActionCommand().equals("Graph Panel")) {
            componentPointers.getRightHandPanes().getGraphStreamPanel().getViewer().getDefaultView().openInAFrame(false);
        }
        if (e.getActionCommand().equals("Policy Applied Panel")) {
            componentPointers.getRightHandPanes().getGs_policyApplied().getViewer().getDefaultView().openInAFrame(false);
        }
        if (e.getActionCommand().equals("settings")) {
            Object[] possibilities = {"default", "default-alt", "provn"};
            String s = (String) JOptionPane.showInputDialog(
                    this,
                    "Select Theme",
                    "Theme",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "provn");
            if ((s != null) && (s.length() > 0)) {
                componentPointers.getTextPanes().setTheme(componentPointers.getTextPanes().getProvN(), "/themes/" + s + ".xml");
            }
        }
        if (e.getActionCommand().equals("legacy")) {
            componentPointers.getButtonPanel().oldStuffVisible(oldStuff.getState());
            logger.debug("Old stuff: " + oldStuff.getState());
        }
        if (e.getActionCommand().equals("Apply Policy")) {
            logger.debug("Menu selection: Apply Policy");
            if (componentPointers.getProvnfile() != null) {
                // Filename for blurred graph
                String blurredfile = componentPointers.getProvnfile().replace(".provn", "_blurred.provn");
                // Read the policy from the textarea
                String policy = componentPointers.getTextPanes().getPolicy().getText().trim();
                Vector<String[]> parsedPolicy = null;
                // Filename for the svg export of the blurred graph
                String svgfile = blurredfile.replace("_blurred.provn", "_blurred.svg");
                svgfile = FilenameUtils.separatorsToUnix(svgfile);

                try {
                    // Place each line of the policy into a vector of strings
                    if (!(policy.equals(""))) {
                        parsedPolicy = ParsePolicy.parseLines(policy);
                    }
                    // Apply the policy to the graph
                    ProvGraph blurredProvGraph = ParsePolicy.applyPolicy(componentPointers.getGsProvGraph(), componentPointers.getProvGraph(), parsedPolicy);
                    // Update componentPointers with a pointer to the blurred graph
                    componentPointers.setPolGraph(blurredProvGraph);
                    componentPointers.loadPolicyAppliedGraph();
                    updatePolicyAppliedTextArea(blurredProvGraph);
                    ProvGraph.writeGraphToFile(blurredProvGraph, blurredfile);
                    File svgFile = new File(svgfile);
                    try {
                        convertProvN(blurredfile, svgfile);
                        componentPointers.getRightHandPanes().getPolAppSVGCanvas().setURI(svgFile.toURI().toURL().toString());
                        componentPointers.getRightHandPanes().setSelectedIndex(1);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "An error occured while converting prov to svg: " + ex.getMessage());
                    }
                } catch (InvalidPolicySyntaxException ips) {
                    JOptionPane.showMessageDialog(this, "Invalid Policy syntax. Please check your policy.", "Policy Syntax Error:", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidBlurLevel ibl) {
                    JOptionPane.showMessageDialog(this, "Invalid blur level. Blur levels have to be odd numbers", "Invalid blur level:", JOptionPane.ERROR_MESSAGE);
                } catch (ElementDoesntExistException ede) {
                    JOptionPane.showMessageDialog(this, "One of the elements specified in your policy doesn't exisit.\n" +
                            "Did you specify the domain?", "Element doesn't exist:", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidCollapseElementsException ece) {
                    JOptionPane.showMessageDialog(this, "Elements have to be of the same type.",
                            "Invalid element specified:", JOptionPane.ERROR_MESSAGE);
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(this, "No provenance description found.", "Missing Information:", JOptionPane.ERROR_MESSAGE);
            }

        }
        if (e.getActionCommand().equals("Toolbox")) {
            new ToolboxFrame("Provenance Conversion Tool GUI");
        }
        if (e.getActionCommand().equals("Merge")) {
            // get bound files (.provn)
            JFileChooser chooserTemplateFile = new JFileChooser();
            chooserTemplateFile.setDialogTitle("Select Provenance Files To Merge");
            chooserTemplateFile.setCurrentDirectory(new java.io.File(ComponentPointers.getLastDir()));
            chooserTemplateFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter prefixFilterfilter = new FileNameExtensionFilter("ProvN files", "provn");
            chooserTemplateFile.setFileFilter(prefixFilterfilter);
            chooserTemplateFile.setMultiSelectionEnabled(true);
            int retTemplateFile = chooserTemplateFile.showOpenDialog(this);
            if (retTemplateFile == JFileChooser.APPROVE_OPTION) {
                File[] input_file = chooserTemplateFile.getSelectedFiles();
                JFileChooser chooserSaveToFile = new JFileChooser();
                chooserSaveToFile.setDialogTitle("Select directory to save to");
                chooserSaveToFile.setCurrentDirectory(new java.io.File(ComponentPointers.getLastDir()));
                chooserSaveToFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooserSaveToFile.setMultiSelectionEnabled(false);
                int retSaveToFile = chooserSaveToFile.showSaveDialog(this);
                if (retSaveToFile == JFileChooser.APPROVE_OPTION) {
                    // output directory
                    File output_directory = chooserSaveToFile.getSelectedFile();
                    if (input_file != null || output_directory != null) {
                        PrintWriter pw;
                        try {
                            pw = new PrintWriter(new File(output_directory + "/index.txt"));
                            if (input_file != null)
                                for (int i = 0; i < input_file.length; i++) {
                                    File f = input_file[i];
                                    String outfile = f.getAbsolutePath().replace("converted", "bound").replace(".json", ".provn");
                                    pw.print("file, " + outfile + ", provn");
                                    if (i != input_file.length - 1) {
                                        pw.println(",");
                                    }
                                }
                            pw.close();
                            String indexfile = output_directory + "/index.txt";
                            merge(indexfile, output_directory + "/workflow.provn");
                            try {
                                convertProvN(output_directory + "/workflow.provn", output_directory + "/workflow.svg");
                            } catch (Exception ex1) {
                                JOptionPane.showMessageDialog(this, "An error occured while converting prov to svg: " + ex1.getMessage());

                            }
                            try {
                                convertProvN(output_directory + "/workflow.provn", output_directory + "/workflow.png");
                            } catch (Exception ex2) {
                                JOptionPane.showMessageDialog(this, "An error occured while converting prov to png: " + ex2.getMessage());

                            }

                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        // DO MERGING OF BOUND FILES
                    }
                }
            }
        }
    }

    private void updatePolicyAppliedTextArea(ProvGraph g) {
//        Vector<String> namespaces = g.getNamespaces();
        componentPointers.getTextPanes().getChanged().setText(ProvGraph.graphToString(g));
    }

    private void tabset(tabs tab) {
        int selecttab = 0;

        switch (tab) {
            case POLICY:
                selecttab = 1;
                break;
            case PROVN:
                selecttab = 0;
                break;
            case TEXT:
                selecttab = 3;
                break;
            case TEMPLATE:
                selecttab = 4;
                break;
//            case DATA:
//                selecttab = 0;
//                break;
//            case PROCESS:
//                selecttab = 0;
//                break;
        }


        componentPointers.getTextPanes().setSelectedIndex(selecttab);
    }
}
