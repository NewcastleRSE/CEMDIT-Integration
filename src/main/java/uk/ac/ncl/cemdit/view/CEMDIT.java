package uk.ac.ncl.cemdit.view;
//http://localhost:8086/query/?theme=Vehicles&type=Vehicle count&sensor=PER_TRF_CNT_SL_A690D1&starttime=20190101000000&endtime=2019013123595959

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.integration.LookupType;
import uk.ac.ncl.cemdit.controller.integration.Utils;
import uk.ac.ncl.cemdit.model.InteropParameters;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.view.integration.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

public class CEMDIT extends JFrame implements ActionListener, ListSelectionListener {
    private Logger logger = Logger.getLogger(this.getClass());
    ComponentPointers cp = ComponentPointers.getInstance();

    /**
     * The main panel containing all the other panels
     */
    private CEMDITMainPanel mainPanel;

    /**
     * A tabbed pane for all the subpanels
     */
    private JTabbedPane tabbedpane = new JTabbedPane();

    /**
     * Panel containing the top ranked response (or else the selected response)
     */
    private ResultsPanel resultPanel;

    /**
     * Panel containing the text area for the query, radio buttons for the query type and
     * a combobox for selecting the provenance template to be used to bind the results to.
     */
    private QueryPanel queryPanel;

    /**
     * Panel containing all the returned results
     */
    private DataPanel dataPanel;

    /**
     * Panel containing all the matches
     */
    MatchPanelNew matchPanel = new MatchPanelNew();

    /**
     * Model for data table
     */
    static private IntegrationDataModel integrationDataModel = new IntegrationDataModel();

    /**
     * Class containing pointers to everything (all components of GUI etc)
     */
    static private IntegrationModel integrationModel = new IntegrationModel();


    /**
     * Constructor
     */
    private CEMDIT() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String logo = "CEMDITLogo3.png";
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            Image icon = toolkit.getImage(ClassLoader.getSystemResource(logo));

            setIconImage(icon);
        } catch (NullPointerException e) {
            logger.error(logo + " not found.");
        }

        mainPanel = new CEMDITMainPanel();
        dataPanel = new DataPanel(this);
        resultPanel = new ResultsPanel(integrationModel, integrationDataModel, this);
        queryPanel = new QueryPanel(integrationModel, integrationDataModel, this);
        dataPanel.getDataTable().setDataModel(integrationDataModel);
        tabbedpane.add("Query", queryPanel);
        tabbedpane.add("Query Results", dataPanel);
        tabbedpane.add("Matches", matchPanel);
        getContentPane().add(tabbedpane);
        pack();
        setTitle("Communication in Emergency Management through Data Integration and Trust");
        setVisible(true);
        //setSize(1024, 768);
        setSize(getWidth(), getHeight());
    }

    static public void main(String[] args) {
        CEMDIT mockup = new CEMDIT();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug("Action: " + e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Run":
                if (queryPanel.getProvQueryType().getSelectedIndex() > 0) {
                    QueryType qtype = QueryType.SQL;
                    if (queryPanel.getRdf().isSelected()) qtype = QueryType.RDF;
                    if (queryPanel.getRest().isSelected()) qtype = QueryType.REST;
                    if (queryPanel.getSql().isSelected()) qtype = QueryType.SQL;
                    if (queryPanel.getChain().isSelected()) qtype = QueryType.CHAIn;
                    logger.debug("Populate model.");
                    String query = queryPanel.getQueryTextArea().getText();
                    if (query == null || query.equals("")) {
                        JOptionPane.showMessageDialog(this, "Select the type of query before running the query.");

                    } else {
                        // Save the query to the properties file to use as default for next startup
                        ComponentPointers.setProperty("query", query);
                        File dir = new File(System.getProperty("user.home"));
                        // populate the Integration model with the results from the query
                        Utils.populateIntegrationModel(query, integrationModel, integrationDataModel, qtype, this);
                        matchPanel.populateMatchPanel(integrationModel.getQueryResults());
                        matchPanel.getSimilarityScore().setText("Similarity Score: " + integrationModel.getSimilarityScore());
                        integrationModel.getProvenancePanel().setLoaded(false);
                        tabbedpane.setSelectedIndex(1);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Select the provenance template before running the query.");
                }
                break;
            case "Bind Data to Template":
                final JFileChooser fc2 = new JFileChooser(ComponentPointers.getLastDir());
                fc2.setCurrentDirectory(new java.io.File(ComponentPointers.getLastDir()));
                int returnVal2 = fc2.showOpenDialog(this);
                if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                    File file = fc2.getSelectedFile();
                    int writeFile = 0;
                    if (file.exists()) {
                        writeFile = JOptionPane.showConfirmDialog(
                                this,
                                file.getName() + " exists. Do you want to overwrite it?",
                                "File exists.",
                                JOptionPane.YES_NO_OPTION);
                    }
                    if (writeFile == 0) {
                        // Lookup template
                        // Check in properties file where lookup database is stored
                        ComponentPointers componentPointers = ComponentPointers.getInstance();
                        String lookup = ComponentPointers.getProperty("lookupdb");
                        // What lookup type (i.e. sensor) has been selected from the combo box?
                        Utils.lookupProvenance(Enum.valueOf(LookupType.class, lookup), integrationModel, queryPanel.getProvQueryType().getSelectedItem().toString());
                        integrationModel.getProvenancePanel().loadGraph(false);
                        // Get provstore location
                        try {
                            String indexFileName = file.getAbsolutePath() + "_MergeIndex.txt";
                            PrintWriter indexfile = new PrintWriter(indexFileName);
                            InputStream in = new URL(integrationModel.getProvNFilename()).openStream();
                            componentPointers.setProvnfile(integrationModel.getProvNFilename());
                            logger.trace("provn filename: " + integrationModel.getProvNFilename());
                            // Read the provn from the provStore
                            String loadPROVN = Utils.orderFile(new Scanner(in, "UTF-8").useDelimiter("\\A").next());

                            String filename = file.getAbsolutePath();
                            try {
                                InteropParameters interopParameters = new InteropParameters();
                                String directory = file.getParent();
                                // Save data
                                int rows = integrationDataModel.getRowCount();
                                for (int row = 0; row < rows; row++) {
                                    File bindingdatafile = new File(file.getAbsolutePath() + "_" + row + ".json");
                                    PrintWriter pw = new PrintWriter(bindingdatafile);
                                    pw.println("{\"var\": {");
                                    LinkedHashMap<String, Object> hashMap = integrationDataModel.getRowAsHashMap(row);
                                    //Populate dynamically from HashMap
                                    //Give elements names starting with a capital and attributes a name start with lower case
                                    // Use this feature to distinguish between id and value
                                    Set keys = hashMap.keySet();
                                    for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                                        String K = (String) iterator.next();
                                        Object V = hashMap.get(K);
                                        pw.format("\"%s\": [{\"" + ((int) K.toString().charAt(0) < 90
                                                ? "@id\":\"uo:%s\""
                                                : "@value\":\"%s\", \"@type\": \"xsd:string\"")
                                                + "}]" + (iterator.hasNext() ? "," : "") + "\n", K, V);
                                    }
                                    pw.format("},\n");
                                    pw.println("\"context\":{\"ex\": \"http://example.org/\",\"uo\": \"http://urbanobservatory.ac.uk/\"}");
                                    pw.println("\n}\"");
                                    pw.close();
                                }
                                for (int row = 0; row < rows; row++) {
                                    File bindingdatafile = new File(file.getAbsolutePath() + "_" + row + ".json");
                                    File outputbounddata = new File(file.getAbsolutePath() + "_bound_" + row + ".provn");
                                    // Bind data to template
                                    interopParameters.setInfile(ComponentPointers.getProperty("tempdir") + "\\temporary.provn");
                                    interopParameters.setInformat("provn");
                                    interopParameters.setOutfile(outputbounddata.getAbsolutePath());
                                    interopParameters.setOutformat("provn");
                                    interopParameters.setBindings(bindingdatafile.getAbsolutePath());
                                    interopParameters.setBindingformat("");
                                    interopParameters.setBindingsVersion(3);
                                    Utils.bindDataToTemplate(interopParameters);
                                    if (row == rows - 1) indexfile.format("file, %s, provn\n", outputbounddata);
                                    else indexfile.format("file, %s, provn,\n", outputbounddata);
                                }
                                indexfile.close();
                                // Merge files
                                interopParameters.resetValues();
                                interopParameters.setMerge(indexFileName);
                                interopParameters.setOutfile(file.getAbsolutePath() + "_mergedFile.provn");
                                logger.trace(interopParameters.toString());
                                Utils.bindDataToTemplate(interopParameters);
                                interopParameters.setFlatten("flatten");
                                interopParameters.setOutfile(file.getAbsolutePath() + "_MergedFile_Flattened.provn");
                                logger.trace(interopParameters.toString());
                                Utils.bindDataToTemplate(interopParameters);

                                // Run ProvenanceExplorer
                                ProvenanceExplorer provenanceExplorer = new ProvenanceExplorer("Provenance Explorer");
                                provenanceExplorer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                                // load provn into text pane
                                componentPointers.getTextPanes().getProvN().setText(loadPROVN);
                                // load graphs into graphic panes
                                componentPointers.loadProvnFile(new File(ComponentPointers.getProperty("tempdir") + "/temporary.provn"));
                            } catch (FileNotFoundException e1) {
                                logger.trace("File not found: " + e1.getMessage());
                            }
                        } catch (IOException e1) {
                            logger.trace("IOException: " + e1.getMessage());
                        }
                    }
                }

                break;
            case "View Prov Template":
                if (queryPanel.getProvQueryType().getSelectedIndex() > 0) {
                    ComponentPointers componentPointers = ComponentPointers.getInstance();
                    try {
                        // Check in properties file where lookup database is stored
                        String lookup = ComponentPointers.getProperty("lookupdb");
                        // What lookup type (i.e. sensor) has been selected from the combo box?
                        logger.debug("Prov query type: " + queryPanel.getProvQueryType().getSelectedItem().toString());
                        Utils.lookupProvenance(Enum.valueOf(LookupType.class, lookup), integrationModel, queryPanel.getProvQueryType().getSelectedItem().toString());
                        integrationModel.getProvenancePanel().loadGraph(false);
                        // Get provstore location
                        InputStream in = new URL(integrationModel.getProvNFilename()).openStream();
                        componentPointers.setProvnfile(integrationModel.getProvNFilename());
                        // Read the provn from the provStore
                        String loadPROVN = Utils.orderFile(new Scanner(in, "UTF-8").useDelimiter("\\A").next());
                        // Run ProvenanceExplorer
                        ProvenanceExplorer provenanceExplorer = new ProvenanceExplorer("Provenance Explorer");
                        provenanceExplorer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        // load provn into text pane
                        componentPointers.getTextPanes().getProvN().setText(loadPROVN);
                        // load graphs into graphic panes
                        componentPointers.loadProvnFile(new File(ComponentPointers.getProperty("tempdir") + "/temporary.provn"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Select the provenance template before running the query.");
                }
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}


