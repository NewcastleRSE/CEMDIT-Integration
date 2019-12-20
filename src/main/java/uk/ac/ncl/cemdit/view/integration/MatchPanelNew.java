package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.MatchTableModel;
import uk.ac.ncl.cemdit.model.integration.QueryResults;

import javax.swing.*;
import java.util.ArrayList;

public class MatchPanelNew extends JPanel {
    private Logger logger = Logger.getLogger(this.getClass());

    JLabel similarityScore = new JLabel("Similarity Score: " );

    /**
     * A table for displaying the matches returned from CHAIn
     */
    MatchTable matchTable = new MatchTable();
    JScrollPane sp_matchTable = new JScrollPane(matchTable);

    /**
     * Constructor
     */
    public MatchPanelNew() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(similarityScore);
        add(sp_matchTable);
    }

    /**
     * @param queryResults
     */
    public void populateMatchPanel(ArrayList<QueryResults> queryResults) {
        MatchTableModel matchTableModel = new MatchTableModel();
        matchTableModel.setData(queryResults);
        matchTable.setDataModel(matchTableModel);
        matchTableModel.fireTableStructureChanged();
        matchTable.updateUI();
    }

    public JLabel getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(JLabel similarityScore) {
        this.similarityScore = similarityScore;
    }
}
