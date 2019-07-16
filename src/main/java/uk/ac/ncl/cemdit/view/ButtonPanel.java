package uk.ac.ncl.cemdit.view;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openprovenance.prov.model.exception.QualifiedNameException;
import uk.ac.ncl.cemdit.controller.*;
import uk.ac.ncl.cemdit.controller.Exceptions.ElementDoesntExistException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidCollapseElementsException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidPolicySyntaxException;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import static uk.ac.ncl.cemdit.controller.ComponentPointers.saveFile;
import static uk.ac.ncl.cemdit.controller.ProvConvertItems.*;

public class ButtonPanel extends JPanel implements ActionListener {


    enum tabs {PROVN, POLICY, TEXT, TEMPLATE, DATA, PROCESS}

    Logger logger = Logger.getRootLogger();
    static private ComponentPointers componentPointers = ComponentPointers.getInstance();
    static private String polfile;
    static private String textfile;
    static private String templfile;
    static private Properties properties = new Properties();
    static private String lastdir = "";
    static private File f = new File("server.properties");
    static private InputStream is = null;
    static private JRadioButton radio_provn = new JRadioButton("PROVN");
    static private JRadioButton radio_policy = new JRadioButton("Policy");
    static private JRadioButton radio_templ = new JRadioButton("Templates");
    static private JRadioButton radio_new = new JRadioButton("Text Docs");
    static private JRadioButton radio_data = new JRadioButton("Data");
    static private JRadioButton radio_process = new JRadioButton("Process");
    static private JButton provn_load = new JButton("Load ProvN");
    static private JButton provn_save = new JButton("Save ProvN");
    static private JButton provn_png = new JButton("Save PNG");
    static private JButton pol_load = new JButton("Load Policy");
    static private JButton pol_save = new JButton("Save Policy");
    static private JButton pol_apply = new JButton("Apply Policy");
    static private JButton new_load = new JButton("Load Text File");
    static private JButton new_save = new JButton("Save Text File As ");
    static private JButton bind_templateLoad = new JButton("Load Template");
    static private JButton bind_templateSave = new JButton("Save Template");
    static private JButton bind_templateGen = new JButton("Generate CSV Template");
    static private JButton data_CSV = new JButton("View CSV");
    static private JButton data_CSV2JSON = new JButton("CSV to JSON");
    static private JButton data_bind = new JButton("Binding");
    static private JButton process = new JButton("Process data");
    static private ButtonGroup buttonGroup = new ButtonGroup();
    static private JPanel btn_enclosure = new JPanel();
    static private JPanel btn_subPanel = new JPanel(new FlowLayout());
    static private JPanel rad_subPanel = new JPanel(new FlowLayout());
    static private JPanel operator_subPanel = new JPanel(new FlowLayout());
    static private JButton clear_selection = new JButton("Clear");
    static private JLabel firstSelected = new JLabel("From");
    static private JLabel secondSelected = new JLabel("To");
    static private final int FPS_MIN = 0;
    static private final int FPS_MAX = 20;
    static private final int FPS_INIT = 10;

    public ButtonPanel() {
        super();
        try {
            File f = new File("server.properties");
            is = new FileInputStream(f);
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream("server.properties");
            }

            // Try loading properties from the file (if found)
            properties.load(is);
            lastdir = properties.getProperty("lastdir");
        } catch (Exception e) {
            e.printStackTrace();
        }
        radio_provn.setSelected(true);

        // action commands
        radio_provn.setActionCommand("radprov");
        radio_policy.setActionCommand("radpol");
        radio_new.setActionCommand("radtext");
        radio_templ.setActionCommand("radtempl");
        radio_data.setActionCommand("raddata");
        radio_process.setActionCommand("radproc");
        provn_load.setActionCommand("provn");
        provn_save.setActionCommand("saveprovn");
        provn_png.setActionCommand("savepng");
        pol_load.setActionCommand("policy");
        pol_save.setActionCommand("savePol");
        pol_apply.setActionCommand("apply");
        new_load.setActionCommand("textfile");
        new_save.setActionCommand("save");
        bind_templateLoad.setActionCommand("tempload");
        bind_templateSave.setActionCommand("tempsave");
        bind_templateGen.setActionCommand("tempgen");
        data_CSV.setActionCommand("viewcsv");
        data_CSV2JSON.setActionCommand("csv2json");
        data_bind.setActionCommand("bindtemplate2json");
        process.setActionCommand("process");

        // action listeners
        radio_provn.addActionListener(this);
        radio_policy.addActionListener(this);
        radio_new.addActionListener(this);
        radio_templ.addActionListener(this);
        radio_process.addActionListener(this);
        radio_data.addActionListener(this);

        provn_load.addActionListener(this);
        provn_save.addActionListener(this);
        provn_png.addActionListener(this);

        pol_load.addActionListener(this);
        pol_save.addActionListener(this);
        pol_apply.addActionListener(this);

        new_load.addActionListener(this);
        new_save.addActionListener(this);

        bind_templateLoad.addActionListener(this);
        bind_templateSave.addActionListener(this);
        bind_templateGen.addActionListener(this);
        data_CSV.addActionListener(this);
        data_CSV2JSON.addActionListener(this);
        data_bind.addActionListener(this);
        process.addActionListener(this);
        // mnemonics
        new_save.setMnemonic(KeyEvent.VK_S);

        provn_load.setVisible(true);
        provn_save.setVisible(true);
        provn_png.setVisible(true);
        pol_load.setVisible(false);
        pol_save.setVisible(false);
        new_load.setVisible(false);
        pol_apply.setVisible(false);
        new_save.setVisible(false);
        bind_templateLoad.setVisible(false);
        bind_templateSave.setVisible(false);
        bind_templateGen.setVisible(false);
        data_CSV.setVisible(false);
        data_CSV2JSON.setVisible(false);
        data_bind.setVisible(false);
        process.setVisible(false);

        this.setLayout(new FlowLayout());
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Actions"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        this.getBorder()));

//        this.add(slider);

        buttonGroup.add(radio_provn);
        buttonGroup.add(radio_policy);
        buttonGroup.add(radio_templ);
        buttonGroup.add(radio_new);
        buttonGroup.add(radio_data);
        buttonGroup.add(radio_process);

        btn_subPanel.add(provn_load);
        btn_subPanel.add(provn_save);
        btn_subPanel.add(provn_png);

        btn_subPanel.add(pol_load);
        btn_subPanel.add(pol_save);
        btn_subPanel.add(pol_apply);

        btn_subPanel.add(bind_templateLoad);
        btn_subPanel.add(bind_templateSave);
        btn_subPanel.add(bind_templateGen);

        btn_subPanel.add(data_CSV);
        btn_subPanel.add(data_CSV2JSON);
        btn_subPanel.add(data_bind);

        btn_subPanel.add(new_load);
        btn_subPanel.add(new_save);

        btn_subPanel.add(process);

        rad_subPanel.add(radio_provn);
        rad_subPanel.add(radio_policy);
        rad_subPanel.add(radio_new);
        rad_subPanel.add(radio_templ);
        rad_subPanel.add(radio_data);
        rad_subPanel.add(radio_process);

        btn_enclosure.add(btn_subPanel);
        btn_enclosure.add(rad_subPanel);

        firstSelected.setForeground(Color.black);
        firstSelected.setText("From: NodeName");
        secondSelected.setForeground(Color.black);
        secondSelected.setText("To: NodeName");
        clear_selection.addActionListener(this);
        operator_subPanel.add(firstSelected);
        operator_subPanel.add(secondSelected);
        operator_subPanel.add(clear_selection);

        btn_enclosure.add(rad_subPanel, BorderLayout.NORTH);
        btn_enclosure.add(btn_subPanel, BorderLayout.SOUTH);
        this.add(btn_enclosure);
        this.add(operator_subPanel);

        btn_enclosure.setVisible(false);
        operator_subPanel.setVisible(true);
    }

    public static JButton getClear_selection() {
        return clear_selection;
    }

    public static JLabel getFirstSelected() {
        return firstSelected;
    }

    public static JLabel getSecondSelected() {
        return secondSelected;
    }

    public static void setClear_selection(JButton clear_selection) {
        ButtonPanel.clear_selection = clear_selection;
    }

    public static void setFirstSelected(JLabel firstSelected) {
        ButtonPanel.firstSelected = firstSelected;
    }

    public static void setSecondSelected(JLabel secondSelected) {
        ButtonPanel.secondSelected = secondSelected;
    }

    public void oldStuffVisible(boolean visible) {
        operator_subPanel.setVisible(!visible);
        btn_enclosure.setVisible(visible);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            lastdir = properties.getProperty("lastdir");
            if (lastdir == null || lastdir.equals("")) {
                lastdir = "~";
            }
            final JFileChooser fc = new JFileChooser(lastdir);
            fc.setCurrentDirectory(new java.io.File(lastdir));

            if (e.getActionCommand().equals("Clear")) {
                firstSelected.setForeground(Color.black);
                firstSelected.setText("From: NodeName");
                secondSelected.setForeground(Color.black);
                secondSelected.setText("To: NodeName");
                componentPointers.getRightHandPanes().getGraphStreamPanel().resetSelection();
            }
            if (e.getActionCommand().equals("save")) {
                logger.debug("save");
                JFileChooser fileChooser = new JFileChooser(lastdir);
                fileChooser.setDialogTitle("Specify a file to save");
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String text = componentPointers.getTextPanes().getNewprov().getText();
                    saveFile(fileToSave, text);
                    JOptionPane.showMessageDialog(this, "File " + fileToSave.getName() + " saved");
                }
            }
            if (e.getActionCommand().equals("saveprovn")) {
                logger.debug("saveprovn");
                if (!(componentPointers.getProvnfile() == null)) {
                    File fileToSave = new File(componentPointers.getProvnfile());
                    String text = componentPointers.getTextPanes().getProvN().getText();
                    saveFile(fileToSave, text);
                    JOptionPane.showMessageDialog(this, "File " + fileToSave.getName() + " saved");
                    componentPointers.loadProvnFile(fileToSave);
                }
            }
            if (e.getActionCommand().equals("savePol")) {
                logger.debug("savepol");
                if (!(polfile == null)) {
                    File fileToSave = new File(polfile);
                    String text = componentPointers.getTextPanes().getPolicy().getText();
                    saveFile(fileToSave, text);
                    JOptionPane.showMessageDialog(this, "File " + fileToSave.getName() + " saved");
                }
            }
            // Load PROV-N file
            if (e.getActionCommand().equals("provn")) {
                logger.debug("provn");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PROVN", "provn");
                fc.setFileFilter(filter);
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    componentPointers.setProvnfile(file.getAbsolutePath());
                    componentPointers.loadProvnFile(file);
                }
            }
            if (e.getActionCommand().equals("savepng")) {
                logger.debug("savepng");
                String png = componentPointers.getProvnfile().replace(".provn", ".png");
                try {
                    convertProvN(componentPointers.getProvnfile(), png);
                    JOptionPane.showMessageDialog(this, "File " + png + " saved");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "An error occured while converting prov to png: " + ex.getMessage());

                }
            }
            if (e.getActionCommand().equals("apply")) {
                logger.debug("apply");
                if (componentPointers.getProvnfile() != null) {
                    String blurredfile = componentPointers.getProvnfile().replace(".provn", "_blurred.provn");
                    String policy = componentPointers.getTextPanes().getPolicy().getText().trim();
                    Vector<String[]> parsedPolicy = null;
                    String svgfile = blurredfile.replace("_blurred.provn", "_blurred.svg");
                    svgfile = FilenameUtils.separatorsToUnix(svgfile);

                    try {
                        if (!(policy.equals(""))) {
                            parsedPolicy = ParsePolicy.parseLines(policy);
                        }

                        ProvGraph blurredProvGraph = ParsePolicy.applyPolicy(componentPointers.getGsProvGraph(), componentPointers.getProvGraph(), parsedPolicy);
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
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "No provenance description found.", "Missing Information:", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getActionCommand().equals("policy")) {
                logger.debug("policy");
                componentPointers.getTextPanes().setSelectedIndex(1);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Policy", "pol");
                fc.setFileFilter(filter);
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    polfile = file.getAbsolutePath();
                    try {
                        FileReader reader = new FileReader(polfile);
                        componentPointers.getTextPanes().getPolicy().read(reader, polfile);
                        componentPointers.getTextPanes().getPolicy().setEditable(true);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //This is where a real application would open the file.
                }
            }
            if (e.getActionCommand().equals("textfile")) {
                logger.debug("textfile");
                componentPointers.getTextPanes().setSelectedIndex(3);
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    textfile = file.getAbsolutePath();
                    try {
                        FileReader reader = new FileReader(textfile);
                        componentPointers.getTextPanes().getNewprov().read(reader, textfile);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getActionCommand().equals("tempload")) {
                logger.debug("tempload");
                componentPointers.getTextPanes().setSelectedIndex(4); // set focus on tab
                FilenamePrefixFilter filter = new FilenamePrefixFilter("Template", "templ_");
                FileNameExtensionFilter efilter = new FileNameExtensionFilter("PROVN file", "provn");
                fc.setFileFilter(efilter);

                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    lastdir = file.getParent();
                    templfile = file.getAbsolutePath();
                    componentPointers.setProvGraph(ParsePROVN.parseFile(new File(templfile)));
                    componentPointers.getTextPanes().setTitleAt(4, "Template: " + file.getName());
                    try {
                        FileReader reader = new FileReader(templfile);
                        componentPointers.getTextPanes().getTemplate().read(reader, templfile);
                        componentPointers.getTextPanes().getTemplate().setEditable(true);
                        componentPointers.getTextPanes().getChanged().setText(null);
                        componentPointers.setProperty("lastdir", lastdir);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getActionCommand().equals("tempsave")) {
                logger.debug("tempsave");
                if (!(templfile == null)) {
                    File fileToSave = new File(templfile);
                    String text = componentPointers.getTextPanes().getTemplate().getText();
                    saveFile(fileToSave, text);
                    JOptionPane.showMessageDialog(this, "File " + fileToSave.getName() + " saved");
                }
            }
            if (e.getActionCommand().equals("tempgen")) {
                logger.debug("tempgen");
                //OPEN CSV FILE
                // READ HEAD LINE ONE
                // READ HEAD LINE TWO
                // INSTANTIATE LINE 3 ONWARDS AS ATTRIBUTES TO ELEMENTS AND RELATIONSHIPS
            }
            if (e.getActionCommand().equals("viewcsv")) {
                logger.debug("viewcsv");
                componentPointers.getRightHandPanes().setSelectedIndex(2);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Data file", "csv");
                fc.setFileFilter(filter);
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    componentPointers.getRightHandPanes().getCsvTableModel().load(file);
                    componentPointers.getRightHandPanes().getCsvTableModel().fireTableDataChanged();
                    componentPointers.getRightHandPanes().setTitleAt(2, "CSV: " + file.getName());
                }
            }
            // Convert a csv file to JSON for binding

            if (e.getActionCommand().equals("csv2json")) {
                logger.debug("csv2json");
                componentPointers.getRightHandPanes().setSelectedIndex(2);
                JFileChooser chooserInputFiles = new JFileChooser();
                chooserInputFiles.setDialogTitle("CSV file to convert");
                chooserInputFiles.setCurrentDirectory(new java.io.File(lastdir));
                chooserInputFiles.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter efilter = new FileNameExtensionFilter("CSV file", "csv");
                chooserInputFiles.setFileFilter(efilter);
                chooserInputFiles.setMultiSelectionEnabled(false);
                int retInputFiles = chooserInputFiles.showOpenDialog(this);
                if (retInputFiles == JFileChooser.APPROVE_OPTION) {
                    File input_file = chooserInputFiles.getSelectedFile();
                    JFileChooser chooserOutputDir = new JFileChooser();
                    chooserOutputDir.setDialogTitle("File to save to");
                    chooserOutputDir.setCurrentDirectory(new java.io.File(lastdir));
                    chooserOutputDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooserOutputDir.setMultiSelectionEnabled(false);
                    int retOutputDir = chooserOutputDir.showOpenDialog(this);
                    File output_files = chooserOutputDir.getSelectedFile();
                    if (retOutputDir == JFileChooser.APPROVE_OPTION) {
                        String output_file = output_files.getAbsolutePath() + "/" + input_file.getName().substring(0, input_file.getName().lastIndexOf("."));
                        CSV2JSON.convert(input_file, output_file);
                        lastdir = output_files.getAbsolutePath();
                        properties.setProperty("lastdir", lastdir);
                        JOptionPane.showMessageDialog(this, "Files converted to JSON.");
                    }
                }
            }
            if (e.getActionCommand().equals("bindtemplate2json")) {
                logger.debug("bindtemplate2json");
                // Select template
                JFileChooser chooserTemplateFile = new JFileChooser();
                chooserTemplateFile.setDialogTitle("Select Provenance Template");
                chooserTemplateFile.setCurrentDirectory(new java.io.File(lastdir));
                chooserTemplateFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FilenamePrefixFilter prefixFilterfilter = new FilenamePrefixFilter("Template", "templ_");
                chooserTemplateFile.setFileFilter(prefixFilterfilter);
                chooserTemplateFile.setMultiSelectionEnabled(false);
                int retTemplateFile = chooserTemplateFile.showOpenDialog(this);
                if (retTemplateFile == JFileChooser.APPROVE_OPTION) {
                    File templ_file = chooserTemplateFile.getSelectedFile();
                    // Select json files
                    JFileChooser chooserJSONFiles = new JFileChooser();
                    chooserJSONFiles.setDialogTitle("Select JSON file containing data");
                    chooserJSONFiles.setCurrentDirectory(new java.io.File(lastdir));
                    chooserJSONFiles.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("JSON files to bind to template", "json");
                    chooserJSONFiles.setFileFilter(extensionFilter);
                    chooserJSONFiles.setMultiSelectionEnabled(true);
                    int retJSONFiles = chooserJSONFiles.showOpenDialog(this);
                    if (retJSONFiles == JFileChooser.APPROVE_OPTION) {
                        File[] data_files = chooserJSONFiles.getSelectedFiles();
                        // Select directory to save to
                        JFileChooser chooserSaveToFile = new JFileChooser();
                        chooserSaveToFile.setDialogTitle("Select directory to save to");
                        chooserSaveToFile.setCurrentDirectory(new java.io.File(lastdir));
                        chooserSaveToFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooserSaveToFile.setMultiSelectionEnabled(false);
                        int retSaveToFile = chooserSaveToFile.showSaveDialog(this);
                        if (retSaveToFile == JFileChooser.APPROVE_OPTION) {
                            File out_file = chooserSaveToFile.getSelectedFile();

                            for (File f : data_files) {
                                String outfile = out_file.getAbsolutePath() + "/" + f.getName().substring(0, f.getName().lastIndexOf('.')) + ".provn";
                                bind("3", templ_file.getAbsolutePath(), f.getAbsolutePath(), outfile);
                            }
                            // Create an index file for the mergin process
                            String filenames = "";
                            File indexfile = new File(out_file.getPath() + "/index.txt");
                            PrintWriter pw = new PrintWriter(indexfile);
                            for (int f = 0; f < data_files.length; f++) {
                                String fn = out_file.getPath() + "/" + data_files[f].getName();
                                fn = fn.replace(".json", ".provn");
                                filenames += "file, " + fn + ", provn";
                                if (f < data_files.length - 1) {
                                    filenames += ", \n";
                                }
                            }
                            pw.print(filenames);
                            pw.close();
                            String outputfile = out_file.getAbsolutePath() + "/mergedfile.provn";
                            merge(indexfile.getAbsolutePath(), outputfile);
                            String svgfile = outputfile.replace(".provn", ".svg");
                            // Convert provn to svg
                            try {
                                convertProvN(outputfile, svgfile);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "An error occured while converting prov to svg: " + ex.getMessage());

                            }
                        }
                    }
                }
            }

            if (e.getActionCommand().equals("process")) {
                logger.debug("process");
                JFileChooser chooserTemplateFile = new JFileChooser(ComponentPointers.getLastDir());
                chooserTemplateFile.setDialogTitle("Select Provenance Template");
                chooserTemplateFile.setCurrentDirectory(new java.io.File(lastdir));
                chooserTemplateFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FilenamePrefixFilter prefixFilterfilter = new FilenamePrefixFilter("Template", "templ_");
                chooserTemplateFile.setFileFilter(prefixFilterfilter);
                chooserTemplateFile.setMultiSelectionEnabled(false);
                int retTemplateFile = chooserTemplateFile.showOpenDialog(this);
                if (retTemplateFile == JFileChooser.APPROVE_OPTION) {
                    File templ_file = chooserTemplateFile.getSelectedFile();
                    // get data (csv)

                    JFileChooser chooserInputFiles = new JFileChooser(ComponentPointers.getLastDir());
                    chooserInputFiles.setDialogTitle("Select CSV file containing data");
                    chooserInputFiles.setCurrentDirectory(new java.io.File(lastdir));
                    chooserInputFiles.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    FileNameExtensionFilter efilter = new FileNameExtensionFilter("CSV file", "csv");

                    chooserInputFiles.setFileFilter(efilter);
                    chooserInputFiles.setMultiSelectionEnabled(false);
                    int retInputFiles = chooserInputFiles.showOpenDialog(this);
                    if (retInputFiles == JFileChooser.APPROVE_OPTION) {
                        File input_file = chooserInputFiles.getSelectedFile();
                        // output directory

                        JFileChooser chooserSaveToFile = new JFileChooser(ComponentPointers.getLastDir());
                        chooserSaveToFile.setDialogTitle("Select directory and enter filename to save to");
                        chooserSaveToFile.setCurrentDirectory(new java.io.File(lastdir));
                        chooserSaveToFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooserSaveToFile.setMultiSelectionEnabled(false);
                        int retSaveToFile = chooserSaveToFile.showSaveDialog(this);
                        if (retSaveToFile == JFileChooser.APPROVE_OPTION) {
                            File output_directory = chooserSaveToFile.getSelectedFile();
                            // convert data to json
                            if (input_file != null || output_directory != null) {
                                PrintWriter pw = new PrintWriter(new File(output_directory + "_index.txt"));
                                ArrayList<String> bindings = CSV2JSON.convert(input_file, output_directory + "_converted");
                                for (int i = 0; i < bindings.size(); i++) {
                                    File f = new File(bindings.get(i));
                                    String outfile = f.getAbsolutePath().replace("converted", "bound").replace(".json", ".provn");
                                    bind("3", templ_file.getAbsolutePath(), f.getAbsolutePath(), outfile);
                                    pw.print("file, " + outfile + ", provn");
                                    if (i != bindings.size() - 1) {
                                        pw.println(",");
                                    }
                                }
                                pw.close();
                                // DO MERGING OF BOUND FILES
                                String indexfile = output_directory + "_index.txt";
                                merge(indexfile, output_directory + "_workflow.provn");
                                try {
                                    convertProvN(output_directory + "_workflow.provn", output_directory + "_workflow.svg");
                                } catch (Exception ex1) {
                                    JOptionPane.showMessageDialog(this, "An error occured while converting prov to svg: " + ex1.getMessage());

                                }
                                try {
                                    convertProvN(output_directory + "_workflow.provn", output_directory + "_workflow.png");
                                } catch (Exception ex2) {
                                    JOptionPane.showMessageDialog(this, "An error occured while converting prov to png: " + ex2.getMessage());

                                }
                            }

                            tabset(tabs.PROCESS, tabs.PROVN);

                            File svgFile = new File(output_directory + "_workflow.svg");
                            String provnfile = output_directory + "_workflow.provn";
                            FileReader reader = new FileReader(output_directory + "_workflow.provn");
                            componentPointers.getTextPanes().getProvN().read(reader, provnfile);
                            componentPointers.getTextPanes().getProvN().setEditable(true);
                            componentPointers.getTextPanes().getChanged().setText(null);
                            componentPointers.getRightHandPanes().getProvSVGCanvas().setURI(svgFile.toURI().toURL().toString());

                        }
                    }
                }

            }

            if (e.getActionCommand().equals("radprov")) {
                logger.debug("radprov");
                tabset(tabs.PROVN, tabs.PROVN);

            }
            if (e.getActionCommand().equals("radpol")) {
                logger.debug("radpol");
                tabset(tabs.POLICY, tabs.POLICY);

            }
            if (e.getActionCommand().equals("radtext")) {
                logger.debug("radtext");
                tabset(tabs.TEXT, tabs.TEXT);

            }
            if (e.getActionCommand().equals("radtempl")) {
                logger.debug("radtempl");
                tabset(tabs.TEMPLATE, tabs.TEMPLATE);
            }
            if (e.getActionCommand().equals("radproc")) {
                logger.debug("radproc");
                tabset(tabs.PROCESS, tabs.PROCESS);
            }
            if (e.getActionCommand().equals("raddata")) {
                logger.debug("raddata");
                tabset(tabs.DATA, tabs.DATA);

            }
        } catch (QualifiedNameException e1) {
            JOptionPane.showMessageDialog(this, "An error occured while loading the file: QualifiedNameException");
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (CloneNotSupportedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Which buttons to switch on and which tab to select
     * buttongroups:
     * 0 = provn group, provn tab
     * 1 = policy group, policy tab
     * 2 = new group, new tab
     * 3 = binding group, last selected
     *
     * @param radio
     * @param tab
     */
    private void tabset(tabs radio, tabs tab) {
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
            case DATA:
                selecttab = 0;
                break;
            case PROCESS:
                selecttab = 0;
                break;
        }


        componentPointers.getTextPanes().setSelectedIndex(selecttab);

        provn_load.setVisible(radio == tabs.PROVN);
        provn_save.setVisible(radio == tabs.PROVN);
        provn_png.setVisible(radio == tabs.PROVN);

        pol_save.setVisible(radio == tabs.POLICY);
        pol_load.setVisible(radio == tabs.POLICY);
        pol_apply.setVisible(radio == tabs.POLICY);

        new_load.setVisible(radio == tabs.TEXT);
        new_save.setVisible(radio == tabs.TEXT);

        bind_templateLoad.setVisible(radio == tabs.TEMPLATE);
        bind_templateSave.setVisible(radio == tabs.TEMPLATE);

        data_bind.setVisible(radio == tabs.DATA);
        data_CSV.setVisible(radio == tabs.DATA);
        data_CSV2JSON.setVisible(radio == tabs.DATA);

        process.setVisible(radio == tabs.PROCESS);


    }

    private void updatePolicyAppliedTextArea(ProvGraph g) {
        Vector<String> namespaces = g.getNamespaces();
        Iterator graphIterator = g.entrySet().iterator();
        componentPointers.getTextPanes().getChanged().setText(ProvGraph.graphToString(g));
    }
}


