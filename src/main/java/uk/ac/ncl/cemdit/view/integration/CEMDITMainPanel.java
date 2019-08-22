package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.integration.Utils;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
     * Button to submit query
     */
    private JButton queryButton = new JButton("Run");

    private JRadioButton rest = new JRadioButton("REST");
    private JRadioButton rdf = new JRadioButton("RDF");

    /**
     * Panel containing the top ranked response (or else the selected response)
     */
    private ResultsPanel resultPanel;

    /**
     * Panel containing all the returned results
     */
    private ResponsePanel responsePanel;

    private IntegrationModel integrationModel = new IntegrationModel();
    private IntegrationDataModel integrationDataModel = new IntegrationDataModel();

    public CEMDITMainPanel() {
        super();
        FlowLayout queryLayout = new FlowLayout();
        queryLayout.setAlignment(FlowLayout.LEADING);
        queryPanel.setLayout(queryLayout);
        queryTextArea.setColumns(70);
        queryButton.addActionListener(this);
        queryTextArea.setText(ComponentPointers.getProperty("query"));
        resultPanel = new ResultsPanel(integrationModel, integrationDataModel, this);
        responsePanel = new ResponsePanel(integrationModel.getOtherResponses(), this);

        setLayout(new BorderLayout());
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
        queryPanel.add(queryTextArea);
        queryPanel.add(queryButton);
        queryPanel.add(rest);
        queryPanel.add(rdf);
        add(queryPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(responsePanel, BorderLayout.PAGE_END);
        setDataPanel();
    }

    public void setProvenancePanel() {
        resultPanel.setButtons(true, false, false);
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
                QueryType qtype = rdf.isSelected()?QueryType.RDF:QueryType.REST;
                logger.debug("Populate model.");
//                "sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)";
                String query = queryTextArea.getText();
                if (query == null || query.equals("")) {
                    query = "sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)";
                    queryTextArea.setText(query);
                }
                ComponentPointers.setProperty( "query", query);
                File dir = new File(System.getProperty("user.home"));

// Create a TableModel object to represent the contents of the directory
                IntegrationDataModel model = new IntegrationDataModel();
                Utils.populateIntegrationModel(query, integrationModel, model, qtype);
                resultPanel.getDataPanel().setDataModel(model);
                //queryTextArea.setText(integrationModel.getOriginalQuery());
                resultPanel.getRepairedQuery().setText(integrationModel.getTopRankedQuery());
                resultPanel.getMatchPanel().populateMatchPanel(integrationModel.getQueryResults());
                responsePanel.populateList(integrationModel.getOtherResponses());
                break;
            case "View Provenance":
                if (e.getActionCommand().equals("View Provenance")) {

                    setProvenancePanel();
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
