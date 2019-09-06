package uk.ac.ncl.cemdit.view.integration;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.integration.LookupType;
import uk.ac.ncl.cemdit.controller.integration.Utils;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.model.integration.lookupDB.ProvQueryTypes;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CEMDITMainPanel extends JPanel implements ActionListener, ListSelectionListener {
    private Logger logger = Logger.getLogger(CEMDITMainPanel.class);
    /**
     * Panel containing the original query
     */
    private JPanel queryPanel = new JPanel();
    /**
     * Text area containing the original query
     */
    private JTextArea queryTextArea = new JTextArea();
    /**
     * Provenance query types - for looking up provenance
     */
    private JComboBox<String> provQueryType;
    /**
     * Button to submit query
     */
    private JButton queryButton = new JButton("Run");

    /**
     * Radio button to select REST type query
     */
    private JRadioButton rest = new JRadioButton("REST");

    /**
     * Radio button to select RDF type query
     */
    private JRadioButton rdf = new JRadioButton("RDF");

    /**
     * Panel containing the top ranked response (or else the selected response)
     */
    private ResultsPanel resultPanel;

    /**
     * Panel containing all the returned results
     */
    private ResponsePanel responsePanel;

    /**
     * Model containing data for CEMDIT integration GUI
     */
    private IntegrationModel integrationModel = new IntegrationModel();

    /**
     * Data Model for table containing results from query
     */
    private IntegrationDataModel integrationDataModel = new IntegrationDataModel();

    /**
     * Boolean to confirm that a query has been run when provenance is requested (View Provenance button is being pressed)
     */
    private boolean queryExecuted = false;

    public CEMDITMainPanel() {
        super();
        FlowLayout queryLayout = new FlowLayout();
        queryLayout.setAlignment(FlowLayout.LEADING);
        queryPanel.setLayout(new FlowLayout());
        queryTextArea.setColumns(80);
        JScrollPane sc_queryTextArea = new JScrollPane();
        sc_queryTextArea.add(queryTextArea);
        queryButton.addActionListener(this);
        queryTextArea.setRows(3);
        queryTextArea.setLineWrap(true);
        queryTextArea.setText(ComponentPointers.getProperty("query"));
        resultPanel = new ResultsPanel(integrationModel, integrationDataModel, this);
        responsePanel = new ResponsePanel(integrationModel.getOtherResponses(), this);


        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        setLayout(gridBagLayout);

        // Find the database where the query types are stored. Get the location in the system.properties file
        // For now it is just stored in a json file
        String queryTypesDB = ComponentPointers.getProperty("querydb");
        logger.trace("Where to find available query types: " + queryTypesDB);
        // Populate a list from the json file
        Gson gson = new Gson();
        try {
            ProvQueryTypes provQueryTypes = gson.fromJson(new FileReader(queryTypesDB), ProvQueryTypes.class);
            String[] types = provQueryTypes.getTypes().toArray(new String[provQueryTypes.getTypes().size()]);

            provQueryType = new JComboBox<String>(types);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //setLayout(new BorderLayout());
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("CEM-DIT Data Visualisation"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        this.getBorder()));


        queryPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Your Query"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        queryPanel.getBorder()));
        resultPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Top Ranked Response"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        resultPanel.getBorder()));
        responsePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Other Responses"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        responsePanel.getBorder()));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rest);
        buttonGroup.add(rdf);
        queryPanel.add(provQueryType);
        queryPanel.add(queryTextArea);
        queryPanel.add(queryButton);
        queryPanel.add(rest);
        queryPanel.add(rdf);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 0.20;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(queryPanel, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 0.40;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(resultPanel, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        add(responsePanel, gridBagConstraints);
        setDataPanel();
    }

    public void setProvenancePanel() {
        //resultPanel.setButtons(true, false, false);
    }

    public void setDataPanel() {
        resultPanel.setButtons(false, true, false);
    }

    public void setMatchPanel() {
        resultPanel.setButtons(false, false, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Run":
                setDataPanel();
                QueryType qtype = rdf.isSelected() ? QueryType.RDF : QueryType.REST;
                logger.debug("Populate model.");
//                "sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)";
                String query = queryTextArea.getText();
                if (query == null || query.equals("")) {
                    query = "sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)";
                    queryTextArea.setText(query);
                }
                ComponentPointers.setProperty("query", query);
                File dir = new File(System.getProperty("user.home"));

                IntegrationDataModel model = new IntegrationDataModel();
                Utils.populateIntegrationModel(query, integrationModel, model, qtype);
                resultPanel.getDataPanel().setDataModel(model);
                //queryTextArea.setText(integrationModel.getOriginalQuery());
                resultPanel.getRepairedQuery().setText(integrationModel.getTopRankedQuery());
                resultPanel.getMatchPanel().populateMatchPanel(integrationModel.getQueryResults());
                responsePanel.populateList(integrationModel.getOtherResponses());
                queryExecuted = true;
                integrationModel.getProvenancePanel().setLoaded(false);
                break;
            case "View Provenance":
                if (e.getActionCommand().equals("View Provenance")) {
                    if (queryExecuted) {
                        logger.trace("View Provenance");
                        String lookup = ComponentPointers.getProperty("lookupdb");
                        Utils.lookupProvenance(Enum.valueOf(LookupType.class, lookup), integrationModel);
                        integrationModel.getProvenancePanel().loadGraph();
                        // Switch to view Provenance panel
                        setProvenancePanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Execute a query before requesting provenance.");
                    }
                }
                break;
            case "View Data":
                setDataPanel();
                resultPanel.getDataPanel().revalidate();
                this.revalidate();
                break;
            case "View Matches":
                setMatchPanel();
                this.revalidate();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList jList = (JList) e.getSource();
        ListSelectionModel lsm = jList.getSelectionModel();

        for (int i = 0; i <= lsm.getMaxSelectionIndex(); i++) {
            if (lsm.isSelectedIndex(i)) {
                logger.debug("Selected: " + i);
            }
        }
    }
}
