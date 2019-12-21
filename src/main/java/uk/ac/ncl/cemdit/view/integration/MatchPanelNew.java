package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.MatchTableModel;
import uk.ac.ncl.cemdit.model.integration.QueryResults;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MatchPanelNew extends JPanel {
    private Logger logger = Logger.getLogger(this.getClass());
    private JButton reSubmit = new JButton("Re-submit");
    private JLabel similarityScore = new JLabel("Similarity Score: ");

    /**
     * A table for displaying the matches returned from CHAIn
     */
    private MatchTable matchTable = new MatchTable();
    private JScrollPane sp_matchTable = new JScrollPane(matchTable);

    /**
     * Constructor
     */
    public MatchPanelNew(ActionListener actionListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(similarityScore);
        reSubmit.addActionListener(actionListener);
        add(reSubmit);
        add(sp_matchTable);
    }

    /**
     * @param queryResults An ArrayList of type QueryResults
     */
    public void populateMatchPanel(ArrayList<QueryResults> queryResults) {
        String[] relations = {" ", "=", "<", ">", "!"};
        JComboBox comboBox = new JComboBox(relations);
        MatchTableModel matchTableModel = new MatchTableModel();
        matchTableModel.setData(queryResults);
        matchTable.setDataModel(matchTableModel);
        TableColumn matchColumn = matchTable.getColumnModel().getColumn(3);
        matchColumn.setCellEditor(new MatchTableCBCellEditor(relations));
        matchColumn.setCellRenderer(new MatchTableCBCellRenderer(relations));
    }

    public JLabel getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(JLabel similarityScore) {
        this.similarityScore = similarityScore;
    }
}
