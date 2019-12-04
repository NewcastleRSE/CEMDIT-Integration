package uk.ac.ncl.cemdit.view;

import org.apache.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import uk.ac.ncl.cemdit.view.widgets.FileFormatWidget;
import uk.ac.ncl.cemdit.view.widgets.FileSelectWidget;
import uk.ac.ncl.cemdit.view.widgets.OnOffWidget;
import uk.ac.ncl.cemdit.view.widgets.TextWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class ToolboxFrame extends JFrame implements ActionListener {
    Logger logger = Logger.getRootLogger();
    static private JMenuBar menuBar = new JMenuBar();
    static private JMenu menu = new JMenu("Menu");
    static private JMenuItem help = new JMenuItem("Help");
    static private JMenuItem version = new JMenuItem("Version");
    static private JMenuItem about = new JMenuItem("About");

    static private String verbose = null;
    static private String debug = null;
    static private String logfile = null;
    static private String infile = null;
    static private String informat = null;
    static private String outfile = null;
    static private String outformat = null;
    static private String namespaces = null;
    static private String title = null;
    static private String layout = null;
    static private String bindings = null;
    static private String bindingformat = null;
    static private String generator = null;
    static private String index = null;
    static private String flatten = null;
    static private String merge = null;
    static private String compare = null;
    static private String compareOut = null;
    static private String template = null;
    static private String bpackage = null;
    static private String location = null;
    static private int bindingsVersion = 3;
    static private boolean addOrderp = false;
    static private boolean listFormatsp = false;
    static private boolean allexpanded = false;
    static private String lastdir = "";

    static private OnOffWidget verboseWidget = new OnOffWidget("Verbose");
    static private OnOffWidget debugWidget = new OnOffWidget("Debug");
    static private OnOffWidget indexWidget = new OnOffWidget("Index");
    static private OnOffWidget flattenWidget = new OnOffWidget("Flatten");
    static private FileSelectWidget mergeIndexFileSelectWidget;
    static private FileFormatWidget logfileSelectWidget;
    static private FileSelectWidget fileinSelectWidget;
    static private FileFormatWidget fileinFormatWidget = new FileFormatWidget("File in format");
    static private FileSelectWidget fileoutSelectWidget;
    static private FileFormatWidget fileoutFormatWidget = new FileFormatWidget("File out format");
    static private FileSelectWidget namespacesSelectWidget;
    static private TextWidget titleWidget = new TextWidget("Enter heading text",30);
    static private FileFormatWidget layoutSelectWidget;
    static private FileSelectWidget bindingsSelectWidget;
    static private FileFormatWidget bindingsFormatWidget = new FileFormatWidget("Bindings file format");
    static private TextWidget bindingsVersionTextWidget = new TextWidget("Bindings version", 10);
    static private OnOffWidget genorderWidget = new OnOffWidget("Generate order");
    static private TextWidget generatorTextWidget = new TextWidget("graph generator N:n:first:seed:e1", 30);
    static private OnOffWidget allexpandedWidget = new OnOffWidget("All expanded");
    static private FileSelectWidget templateFileSelectWidget;
    static private TextWidget packageTextWidget = new TextWidget("Package of bindings been class", 30);
    static private FileSelectWidget locationFileSelectWidget;
    static private FileSelectWidget compareFileSelectWidget;
    static private FileSelectWidget compareOutFileSelectWidget;

    public ToolboxFrame(String title) {
        super(title);
        try {
            File f = new File("server.properties");
            Properties properties = new Properties();
            InputStream is = new FileInputStream(f);
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream("server.properties");
            }
            properties.load(is);
            lastdir = properties.getProperty("lastdir");
            System.out.println(lastdir);
        } catch (Exception e) {
            System.out.println("Properties file not found");
        }
        String[] infiletypes = getFileTypes("input"); //{null, "provn", "ttl", "provx", "xml", "pdf", "svg"};
        String[] outfiletypes = getFileTypes("output"); //{null, "provn", "ttl", "provx", "xml", "pdf", "svg", "png"};
        String[] dotlayouts = {"dot", "circo", "fdp", "neato", "osage", "sfdp", "twopi"};
        String[] bindingformats = getFileTypes("input");
        mergeIndexFileSelectWidget = new FileSelectWidget("Select index file for merging", lastdir, "txt");
        logfileSelectWidget = new FileFormatWidget("Select log file");
        fileinSelectWidget = new FileSelectWidget("Select input file", lastdir, null);
        fileinFormatWidget.setFileFormats(infiletypes);
        fileoutSelectWidget = new FileSelectWidget("Select output file", lastdir, null);
        fileoutFormatWidget.setFileFormats(outfiletypes);
        namespacesSelectWidget = new FileSelectWidget("Select file for declaration of prefix namespaces", lastdir, null);
        layoutSelectWidget = new FileFormatWidget("Select dot output format");
        layoutSelectWidget.setFileFormats(dotlayouts);
        bindingsSelectWidget = new FileSelectWidget("Select file for bindings to template", lastdir, null);
        bindingsFormatWidget = new FileFormatWidget("Select bindings format");
        bindingsVersionTextWidget.setReturnString("3");
        templateFileSelectWidget = new FileSelectWidget("Template resource location", lastdir, null);
        locationFileSelectWidget = new FileSelectWidget("Location of template resource", lastdir, null);
        compareFileSelectWidget = new FileSelectWidget("File to compare with", lastdir, null);
        compareOutFileSelectWidget = new FileSelectWidget("Output file for log comparison", lastdir, null);

        help.addActionListener(this);
        version.addActionListener(this);
        about.addActionListener(this);
        menu.add(help);
        menu.add(version);
        menu.add(about);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        add(verboseWidget);
        add(debugWidget);
        add(logfileSelectWidget);
        add(fileinSelectWidget);
        add(fileinFormatWidget);
        add(fileoutSelectWidget);
        add(fileoutFormatWidget);
        add(namespacesSelectWidget);
        add(titleWidget);
        add(layoutSelectWidget);
        add(bindingsSelectWidget);
        add(bindingsFormatWidget);
        add(bindingsVersionTextWidget);
        add(generatorTextWidget);
        add(genorderWidget);
        add(allexpandedWidget);
        add(indexWidget);
        add(templateFileSelectWidget);
        add(mergeIndexFileSelectWidget);
        add(flattenWidget);
        add(packageTextWidget);
        add(locationFileSelectWidget);
        add(compareFileSelectWidget);
        add(compareOutFileSelectWidget);

        JButton execute = new JButton("Execute");
        execute.addActionListener(this);
        add(execute);

        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(1024, 768);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("Help")) {
            JOptionPane.showMessageDialog(this,
                    "Help is on the way.", "Help", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getActionCommand().equals("About")) {
            JOptionPane.showMessageDialog(this,
                    StringsAndStuff.getAbout(), "About", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getActionCommand().equals("Version")) {
            JOptionPane.showMessageDialog(this,
                    "ProvToolbox version: 0.7.3\nProvToolbox GUI version: 1.00", "Version", JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getActionCommand().equals("Execute")) {
            executeInterop();
        }
    }

    private void executeInterop() {
        verbose = verboseWidget.getReturnString().equals("On") ? "verbose" : null;
        debug = debugWidget.getReturnString().equals("On") ? "debug" : null;
        logfile = logfileSelectWidget.getReturnString();
        infile = fileinSelectWidget.getReturnString();
        informat = fileinFormatWidget.getReturnString();
        outfile = fileoutSelectWidget.getReturnString();
        outformat = fileoutFormatWidget.getReturnString();
        namespaces = namespacesSelectWidget.getReturnString();
        title = titleWidget.getReturnString();
        layout = layoutSelectWidget.getReturnString();
        bindings = bindingsSelectWidget.getReturnString();
        bindingformat = bindingsFormatWidget.getReturnString();
        bindingsVersion = Integer.valueOf(bindingsVersionTextWidget.getReturnString());
        addOrderp = genorderWidget.getReturnString().equals("On") ? true : false;
        allexpanded = allexpandedWidget.getReturnString().equals("On") ? true : false;
        template = templateFileSelectWidget.getReturnString();
        bpackage = packageTextWidget.getReturnString();
        location = locationFileSelectWidget.getReturnString();
        generator = generatorTextWidget.getReturnString();
        merge = mergeIndexFileSelectWidget.getReturnString();
        flatten = flattenWidget.getReturnString().equals("On") ? "flatten" : null;
        index = indexWidget.getReturnString().equals("On") ? "index" : null;
        compare = compareFileSelectWidget.getReturnString();
        compareOut = compareOutFileSelectWidget.getReturnString();
        System.out.println("-verbose: " + verbose +
                "\n" + "-debug:" + debug +
                "\n" + "-logfile: " + logfile +
                "\n" + "-flatten: " + flatten +
                "\n" + "-index: " + index +
                "\n" + "-merge " + merge +
                "\n" + "-informat: " + informat +
                "\n" + "-infile: " + infile +
                "\n" + "-outformat: " + outformat +
                "\n" + "-outfile: " + outfile +
                "\n" + "-namespaces: " + namespaces +
                "\n" + "-title: " + title +
                "\n" + "-layout " + layout +
                "\n" + "-bindings " + bindings +
                "\n" + "-bindformat " + bindingformat +
                "\n" + "-generator " + (generator.equals("")?null:generator) +
                "\n" + "-genorder " + null + //(generator.equals("On") ? "ordered" : "unordered") +
                "\n" + "-allexpanded " + allexpanded +
                "\n" + "-bindversion " + bindingsVersion +
                "\n" + "-template " + template +
                "\n" + "-package " + (bpackage.equals("")?null:bpackage) +
                "\n" + "-location " + location +
                "\n" + "-compare " + compare +
                "\n" + "-compareOut " + compareOut
        );


        InteropFramework interop = new InteropFramework(verbose,
                debug,
                logfile,
                infile,
                informat,
                outfile,
                outformat,
                namespaces,
                title,
                layout,
                bindings,
                bindingformat,
                bindingsVersion,
                addOrderp,
                allexpanded,
                template,
                bpackage,
                location,
                generator,
                index,
                merge,
                flatten,
                compare,
                compareOut,
                org.openprovenance.prov.xml.ProvFactory.getFactory());
        try {
            interop.run();
            resetVariables();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error(e);
            JOptionPane.showMessageDialog(this,
                    "You can't do that!",
                    "IllegalArgumentException.",
                    JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedOperationException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(this,
                    "You can't do that!",
                    "Unsupported Operation",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            logger.error(e);
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred. Check your log file.",
                    "ERROR:",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetVariables() {
        verbose = null;
        debug = null;
        logfile = null;
        infile = null;
        informat = null;
        outfile = null;
        outformat = null;
        namespaces = null;
        title = null;
        layout = null;
        bindings = null;
        bindingformat = null;
        bindingsVersion = 3;
        addOrderp = false;
        allexpanded = false;
        template = null;
        bpackage = null;
        location = null;
        generator = null;
        index = null;
        merge = null;
        flatten = null;
        compare = null;
        compareOut = null;
    }

    private String[] getFileTypes(String type) {
        String[] returnString = null;
        InteropFramework interopFramework = new InteropFramework(org.openprovenance.prov.xml.ProvFactory.getFactory());
        interopFramework.run();
        List<Map<String, String>> filetypes = interopFramework.getSupportedFormats();
        List<String> formats = new ArrayList<String>();
        filetypes.forEach(f -> {
            if (f.get("type").equals(type))
                formats.add(f.get("extension"));
        });
        returnString = formats.toArray(new String[formats.size()]);
        return returnString;
    }
}
