package uk.ac.ncl.cemdit.view.integration;

import com.borland.jbcl.layout.VerticalFlowLayout;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsPanel extends JPanel implements ActionListener {
    private Logger logger = Logger.getLogger(this.getClass());

    private JButton btn_viewProvenance = new JButton("View Provenance");
    private JButton btn_viewData = new JButton("View Data");
    private JButton btn_viewMatches = new JButton("View Matches");
    private JButton btn_saveData = new JButton("Save Results");
    private JButton btn_saveJSON = new JButton("Save and View Binding Data");
    private JPanel viewButtonsHorizontalPanel = new JPanel();
    private JPanel repairedQueryPanel = new JPanel();
    private JTextArea repairedQuery = new JTextArea(4, 80);
    private MatchPanel matchPanel = new MatchPanel();
    private ProvenancePanel provenancePanel = new ProvenancePanel();
    //private GraphStreamPanel graphStreamPanel = new GraphStreamPanel(null);
    private DataTable dataTable = new DataTable();
    ;
    private ActionListener actionListener = this;
    private JScrollPane sp_matchPanel;
    private JScrollPane sp_dataPanel;
    private JPanel holdAll = new JPanel();

    public ResultsPanel(IntegrationModel integrationModel, IntegrationDataModel integrationDataModel, Object eventsListener) {
        super();
        dataTable.setDataModel(integrationDataModel);
        actionListener = (ActionListener) eventsListener;
        setLayout(new BorderLayout());

        btn_viewProvenance.addActionListener(actionListener);
        btn_viewData.addActionListener(actionListener);
        btn_viewMatches.addActionListener(actionListener);
        btn_saveData.addActionListener(actionListener);
        btn_saveJSON.addActionListener(actionListener);
        btn_saveJSON.setActionCommand("Save Binding Data");

        viewButtonsHorizontalPanel.setLayout(new FlowLayout());
        viewButtonsHorizontalPanel.add(btn_viewProvenance);
        viewButtonsHorizontalPanel.add(btn_viewData);
        viewButtonsHorizontalPanel.add(btn_viewMatches);
        viewButtonsHorizontalPanel.add(btn_saveData);
        viewButtonsHorizontalPanel.add(btn_saveJSON);
        viewButtonsHorizontalPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Views"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        viewButtonsHorizontalPanel.getBorder()));


        repairedQuery.setText(integrationModel.getTopRankedQuery());
        repairedQuery.setLayout(new BorderLayout());
        repairedQueryPanel.add(repairedQuery);
        repairedQuery.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Repaired Query"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        repairedQuery.getBorder()));

        matchPanel = (MatchPanel) matchPanel.createMatchPanel(matchPanel, integrationModel.getQueryResults(), actionListener);
        matchPanel.setSimilarity(integrationModel.getSimilarityScore());

        sp_matchPanel = new JScrollPane(matchPanel);
        sp_matchPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_matchPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_matchPanel.setPreferredSize(new Dimension(900, 250));

        sp_dataPanel = new JScrollPane(dataTable);
        sp_dataPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setPreferredSize(new Dimension(900, 250));
        logger.debug("Length of query results: " + integrationModel.getQueryResults().size());

        holdAll.setLayout(new VerticalFlowLayout());
        holdAll.add(sp_matchPanel, 0);
        holdAll.add(provenancePanel, 1);
        holdAll.add(sp_dataPanel, 2);
        btn_saveData.setEnabled(false);
        btn_saveJSON.setEnabled(false);
        add(viewButtonsHorizontalPanel, BorderLayout.NORTH);
        add(repairedQueryPanel, BorderLayout.CENTER);
        add(holdAll, BorderLayout.SOUTH);
        setButtons(false, false, true);
        //setButtons(true, false, false);
    }

    public void setButtons(boolean prov, boolean data, boolean matches) {
        btn_viewProvenance.setEnabled(!prov);
        btn_viewMatches.setEnabled(!matches);
        btn_viewData.setEnabled(!data);
        btn_saveJSON.setEnabled(!data);
        holdAll.getComponent(0).setVisible(matches);
        holdAll.getComponent(1).setVisible(prov);
        holdAll.getComponent(2).setVisible(data);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public MatchPanel getMatchPanel() {
        return matchPanel;
    }

    public JTextArea getRepairedQuery() {
        return repairedQuery;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public JButton getBtn_saveData() {
        return btn_saveData;
    }

    public void setBtn_saveData(JButton btn_saveData) {
        this.btn_saveData = btn_saveData;
    }

    public JButton getBtn_saveJSON() {
        return btn_saveJSON;
    }

    public void setBtn_saveJSON(JButton btn_saveJSON) {
        this.btn_saveJSON = btn_saveJSON;
    }
}
