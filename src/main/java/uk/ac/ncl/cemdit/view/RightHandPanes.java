package uk.ac.ncl.cemdit.view;

import uk.ac.ncl.cemdit.model.provenancegraph.CSVTableModel;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import javax.swing.*;
import java.awt.*;

/**
 * +---------------------------------+----------+----------------------------------+
 * | TAB 1    | TAB 2     | TAB 3    | TAB 3    |                                  |
 * +---------------------------------+----------+----------------------------------+
 * |                                                             | INFO PANEL      |
 * |                                                             +-----------------+
 * |                                                             |                 |
 * |                                                             +-----------------+
 * |                                                             |                 |
 * |                                                             +-----------------+
 * |  GRAPHSTREAM PANEL                                          |                 |
 * |                                                             +-----------------+
 * |                                                             |                 |
 * |                                                             +-----------------+
 * |                                                             |                 |
 * |                                                             +-----------------+
 * |                                                             |                 |
 * |                                                             |                 |
 * |                                                             |                 |
 * |                                                             |                 |
 * +-------------------------------------------------------------------------------+
 */
public class RightHandPanes extends JTabbedPane {

    private ComponentPointers componentPointers = ComponentPointers.getInstance();
    /**
     * SVGCanvas for provenance diagram
     */
    private SVGCanvas provSVGCanvas = new SVGCanvas();
    /**
     * SVGCanvas for provenance diagram after policy has been applied
     */
    private SVGCanvas polAppSVGCanvas = new SVGCanvas();
    private JTable csvTable;
    private JScrollPane sp_prov;
    private JScrollPane sp_policyApplied;
    private JScrollPane sp_csvTable;
    private JScrollPane sp_statsTextArea;
    private CSVTableModel csvTableModel = new CSVTableModel();
    private JTextArea statsTextArea = componentPointers.getStatsTextArea();
    private InfoPanel infoPanel = new InfoPanel();
    private GraphStreamPanel graphStreamPanel = new GraphStreamPanel(infoPanel);
    private GraphStreamPanel gs_policyApplied = new GraphStreamPanel(infoPanel);
    private JPanel graphAndInfoPanel = new JPanel();

    public RightHandPanes() {
        componentPointers.setGsProvGraph(graphStreamPanel.getGraph());
        componentPointers.setGsPolGraph(gs_policyApplied.getGraph());
        csvTable = new JTable(csvTableModel);
        csvTable.setFillsViewportHeight(true);
        sp_prov = new JScrollPane(provSVGCanvas);
        sp_policyApplied = new JScrollPane(polAppSVGCanvas);
        sp_csvTable = new JScrollPane(csvTable);
        sp_prov.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_prov.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_policyApplied.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_policyApplied.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp_statsTextArea = new JScrollPane(statsTextArea);
        sp_statsTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_statsTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        graphAndInfoPanel.setLayout(new FlowLayout());
        //graphAndInfoPanel.add(graphStreamPanel.getViewPanel());
        graphAndInfoPanel.add(infoPanel);

        addTab("Prov ProvGraph (gs)", graphStreamPanel.getViewPanel());
        addTab("Policy Applied ProvGraph (gs)", gs_policyApplied.getViewPanel());
        //addTab("CSV", sp_csvTable);
        addTab( "Stats", sp_statsTextArea);
        //addTab("Zoom ProvGraph Panel", graphStreamPanel.getViewPanel());
        addTab("Information", graphAndInfoPanel);
        addTab("Prov ProvGraph (provSVGCanvas)", sp_prov);
        addTab("Policy Applied ProvGraph (provSVGCanvas)", sp_policyApplied);
    }

    public SVGCanvas getProvSVGCanvas() {
        return provSVGCanvas;
    }

    public SVGCanvas getPolAppSVGCanvas() {
        return polAppSVGCanvas;
    }

    public JTable getCSV() {
        return csvTable;
    }

    public JScrollPane getSp_prov() {
        return sp_prov;
    }

    public JScrollPane getSp_policyApplied() {
        return sp_policyApplied;
    }

    public JScrollPane getSp_csvTable() {
        return sp_csvTable;
    }

    public CSVTableModel getCsvTableModel() {
        return csvTableModel;
    }

    public void setCsvTableModel(CSVTableModel csvTableModel) {
        this.csvTableModel = csvTableModel;
    }

    public GraphStreamPanel getGraphStreamPanel() {
        return graphStreamPanel;
    }

    public GraphStreamPanel getGs_policyApplied() {
        return gs_policyApplied;
    }

    public JTextArea getStatsTextArea() {
        return statsTextArea;
    }
}
