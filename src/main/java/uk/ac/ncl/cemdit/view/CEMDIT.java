package uk.ac.ncl.cemdit.view;
//http://localhost:8086/query/?theme=Vehicles&type=Vehicle count&sensor=PER_TRF_CNT_SL_A690D1&starttime=20190101000000&endtime=2019013123595959

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.integration.Utils;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.view.integration.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private QueryPanel queryPanel;
    /**
     * Panel containing all the returned results
     */
    private JScrollPane sp_dataPanel;
    private DataPanel dataPanel = new DataPanel();

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
        resultPanel = new ResultsPanel(integrationModel, integrationDataModel, this);
        queryPanel = new QueryPanel(integrationModel, integrationDataModel, this);
        dataPanel.setDataModel(integrationDataModel);
        sp_dataPanel = new JScrollPane(dataPanel);
        sp_dataPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setPreferredSize(new Dimension(900, 250));
        tabbedpane.add("Query", queryPanel);
        tabbedpane.add("Query Results", sp_dataPanel);
        getContentPane().add(tabbedpane);
        pack();
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
                        resultPanel.getDataPanel().setDataModel(integrationDataModel);
                        //queryTextArea.setText(integrationModel.getOriginalQuery());
                        resultPanel.getRepairedQuery().setText(integrationModel.getTopRankedQuery());
                        resultPanel.getMatchPanel().populateMatchPanel(integrationModel.getQueryResults());
                        if (resultPanel.getDataPanel().getRowCount() > 0) {
                            resultPanel.getBtn_saveData().setEnabled(true);
                            resultPanel.getBtn_saveJSON().setEnabled(true);
                        } else {
                            resultPanel.getBtn_saveData().setEnabled(false);
                            resultPanel.getBtn_saveJSON().setEnabled(false);
                        }
                        integrationModel.getProvenancePanel().setLoaded(false);
                        tabbedpane.setSelectedIndex(1);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Select the type of query before running the query.");
                }
                break;
            case "comboBoxChanged":

                logger.debug("Selected item: " + queryPanel.getProvQueryType().getSelectedItem());
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}

