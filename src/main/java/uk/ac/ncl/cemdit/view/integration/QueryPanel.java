package uk.ac.ncl.cemdit.view.integration;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.integration.LookupType;
import uk.ac.ncl.cemdit.dao.sqlite.Connector;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.model.integration.lookupDB.ProvQueryTypes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class QueryPanel extends JPanel implements ActionListener {
    private Logger logger = Logger.getLogger(CEMDITMainPanel.class);

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
     * Radio button to select RDF type query
     */
    private JRadioButton sql = new JRadioButton("SQL");

    /**
     * Grouping of button
     */
    private ButtonGroup buttonGroup = new ButtonGroup();

    public QueryPanel(IntegrationModel integrationModel, IntegrationDataModel integrationDataModel, Object eventsListener) {

//        FlowLayout queryLayout = new FlowLayout();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        queryButton.addActionListener(this);
        queryTextArea.setLineWrap(true);
        queryTextArea.setText(ComponentPointers.getProperty("query"));
        queryTextArea.setColumns(80);
        queryTextArea.setRows(40);
        JScrollPane sc_queryTextArea = new JScrollPane();
        sc_queryTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sc_queryTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Find the database where the query types are stored. Get the location in the system.properties file
        // For now it is just stored in a json file
        String queryTypesDB = ComponentPointers.getProperty("querydb");
        logger.trace("Where to find available query types: " + queryTypesDB);
        LookupType templateTypes = Enum.valueOf(LookupType.class, ComponentPointers.getProperty("templateLocationType").trim().toUpperCase());
        System.out.println(templateTypes);
        switch (templateTypes) {
            case SQLITE:
                provQueryType = new JComboBox<>(Connector.provenanceTemplates(ComponentPointers.getProperty("sqlitedb")));
                break;
            case JSON:
                // Populate a list from the json file
                Gson gson = new Gson();
                try {
                    ProvQueryTypes provQueryTypes = gson.fromJson(new FileReader(queryTypesDB), ProvQueryTypes.class);
                    String[] types = provQueryTypes.getTypes().toArray(new String[provQueryTypes.getTypes().size()]);

                    provQueryType = new JComboBox<>(types);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        buttonGroup.add(rest);
        buttonGroup.add(rdf);
        buttonGroup.add(sql);
        add(provQueryType);
        add(queryButton);
        add(rest);
        add(rdf);
        add(sql);

        add(queryButton);
        sc_queryTextArea.add(queryTextArea);
        add(queryTextArea);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
