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
    private JPanel viewButtonsHorizontalPanel = new JPanel();
    private JPanel repairedQueryPanel = new JPanel();
    private JTextArea repairedQuery = new JTextArea(4, 80);
    private MatchPanel matchPanel = new MatchPanel();
    private ProvenancePanel provenancePanel = new ProvenancePanel();
    //private GraphStreamPanel graphStreamPanel = new GraphStreamPanel(null);
    private DataPanel dataPanel = new DataPanel();;
    private ActionListener actionListener = this;
    private JScrollPane sp_matchPanel;
    private JScrollPane sp_dataPanel;
    private JPanel holdAll = new JPanel();

    public ResultsPanel(IntegrationModel integrationModel, IntegrationDataModel integrationDataModel, Object eventsListener) {
        super();
        dataPanel.setDataModel(integrationDataModel);
        actionListener = (ActionListener) eventsListener;
        setLayout(new BorderLayout());

        btn_viewProvenance.addActionListener(actionListener);
        btn_viewData.addActionListener(actionListener);
        btn_viewMatches.addActionListener(actionListener);

        viewButtonsHorizontalPanel.setLayout(new FlowLayout());
        viewButtonsHorizontalPanel.add(btn_viewProvenance);
        viewButtonsHorizontalPanel.add(btn_viewData);
        viewButtonsHorizontalPanel.add(btn_viewMatches);
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

        sp_dataPanel = new JScrollPane(dataPanel);
        sp_dataPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_dataPanel.setPreferredSize(new Dimension(900, 250));
        logger.debug("Length of query results: " + integrationModel.getQueryResults().size());

       holdAll.setLayout(new VerticalFlowLayout());
        holdAll.add(sp_matchPanel,0);
        holdAll.add(provenancePanel, 1);
        holdAll.add(sp_dataPanel, 2);
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

    public DataPanel getDataPanel() {
        return dataPanel;
    }
}
