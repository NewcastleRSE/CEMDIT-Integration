package uk.ac.ncl.cemdit.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.graphstream.graph.Graph;
import org.openprovenance.prov.model.exception.QualifiedNameException;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;
import uk.ac.ncl.cemdit.view.ButtonPanel;
import uk.ac.ncl.cemdit.view.RightHandPanes;
import uk.ac.ncl.cemdit.view.TextPanes;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static uk.ac.ncl.cemdit.controller.ProvConvertItems.convertProvN;

/**
 * A Singleton containing pointers to all graphic components that need updating from anywhere in the program.
 */
public class ComponentPointers {
    private Logger logger = Logger.getLogger(this.getClass());
    private JFrame frame;
    private TextPanes textPanes;
    private RightHandPanes rightHandPanes;
    private ButtonPanel buttonPanel;
    private static ComponentPointers componentPointers = null;
    //private static String theme = "/themes/provn.xml";
    static private Properties properties = new Properties();
    static private String lastDir = "";
    private String provnfile = "Untitled";
    private String polfile = "Untitled";
    private ProvGraph provGraph;
    private ProvGraph polGraph;
    private Graph gsProvGraph;
    //private Graph gsPolGraph;
    static private String css;
    private JTextArea statsTextArea = new JTextArea();


    public static String getStylesheet() {
        return css;
    }

    public static void setStylesheet(String stylesheet) {
        css = stylesheet;
    }

    /**
     * Empty constructor to avoid instantiation
     */
    private ComponentPointers() {
        try {
            css = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
            File f = new File("server.properties");
//            if (!(f.exists())) {
//                OutputStream out = new FileOutputStream( f );
//            }
            InputStream is = new FileInputStream(f);
            properties.load(is);
            lastDir = getLastDir();
            if (lastDir == null) {
                lastDir = "~";
                setLastDir(lastDir);
            }
            properties.setProperty("lastdir", lastDir);

            // Try loading properties from the file (if found)
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Return the single instance of this class
     *
     * @return instatnce of this class
     */
    public static ComponentPointers getInstance() {
        if (componentPointers == null) {
            componentPointers = new ComponentPointers();
        }
        return componentPointers;
    }

    public TextPanes getTextPanes() {
        return textPanes;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setTextPanes(TextPanes textPanes) {
        this.textPanes = textPanes;
    }

    public RightHandPanes getRightHandPanes() {
        return rightHandPanes;
    }

    public void setRightHandPanes(RightHandPanes rightHandPanes) {
        this.rightHandPanes = rightHandPanes;
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setButtonPanel(ButtonPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    /**
     * Save text to a file
     *
     * @param file file to save to
     * @param text text to save to file
     */
    public static void saveFile(File file, String text) {
        try {
            PrintWriter pw = new PrintWriter(new File(file.getAbsolutePath()));

            pw.print(text);
            pw.close();
            System.out.println("File " + file.getAbsolutePath() + " saved");

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Load PROV-N file and create a provGraph structure
     *
     */
    public void loadProvnFile(File file) {
        // set last dir to path of current file
        lastDir = file.getParent();
        // update global pointer to name of provn file
        getTextPanes().setProvnFile(file.getAbsolutePath());
        // create a provenance graph with the contents of the file
        setProvGraph(ParsePROVN.parseFile(new File(getTextPanes().getProvnFile())));
        // set the title of the provn text panel
        getTextPanes().setTitleAt(0, "PROVN: " + file.getName());
        // make panel editable
        getTextPanes().getProvN().setEditable(true);
        // set the stylesheet of the panel
        getRightHandPanes().getGraphStreamPanel().setGGraph(provGraph, getStylesheet(), "ProvN Graph");
        // create a filename with an svg extension for the svg file of the graph
        String svgfile = getTextPanes().getProvnFile().replace(".provn", ".svg");
        svgfile = FilenameUtils.separatorsToUnix(svgfile);
        try {
            FileReader reader = new FileReader(getTextPanes().getProvnFile());
            getTextPanes().getProvN().read(reader, getTextPanes().getProvnFile());
            getTextPanes().getProvN().setEditable(true);
            //componentPointers.getTextPanes().getChanged().setText(null);
            File svgFile = new File(svgfile);
            getRightHandPanes().getProvSVGCanvas().setURI(svgFile.toURI().toURL().toString());
            try {
                convertProvN(getTextPanes().getProvnFile(), svgfile);
            } catch (Exception ex) {
                logger.error("An error occured while converting prov to svg: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (QualifiedNameException e1) {
            logger.error("An error occured while loading the file: QualifiedNameException");
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
            logger.error("IOException occurred");
        }
    }

    public void loadPolicyAppliedGraph() {
        getRightHandPanes().getGs_policyApplied().setGGraph(polGraph, getStylesheet(), "Policy Applied Graph");
    }

    public String getProvnfile() {
        return provnfile;
    }

    public void setProvnfile(String provnfile) {
        this.provnfile = provnfile;
    }

    public ProvGraph getProvGraph() {
        return provGraph;
    }

    public void setProvGraph(ProvGraph provGraph) {
        this.provGraph = provGraph;
    }

    public void setProperty(String property, String value) {
        logger.debug("Setting property: " + property + " = " + value);
        properties.setProperty(property, value);
        File f = new File("server.properties");
        try {
            OutputStream out = new FileOutputStream(f);
            logger.debug("Storing properties");
            properties.store(out, "This is an optional header comment string");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static String getLastDir() {
        return properties.getProperty("lastdir");
    }

    public static void setLastDir(String lastDir) {
        properties.setProperty("lastdir", lastDir);
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        ComponentPointers.css = css;
    }

    public String getPolfile() {
        return polfile;
    }

    public void setPolfile(String polfile) {
        this.polfile = polfile;
    }

    public void setPolGraph(ProvGraph polGraph) {
        this.polGraph = polGraph;

    }

    public JTextArea getStatsTextArea() {
        return statsTextArea;
    }

    public Graph getGsProvGraph() {
        return gsProvGraph;
    }

    public void setGsProvGraph(Graph gsProvGraph) {
        this.gsProvGraph = gsProvGraph;
    }

    public void setGsPolGraph(Graph graph) {
    }

//    public void setGsPolGraph(Graph gsPolGraph) {
//        this.gsPolGraph = gsPolGraph;
//    }
}

