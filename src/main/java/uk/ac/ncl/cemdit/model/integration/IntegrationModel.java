package uk.ac.ncl.cemdit.model.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.view.integration.ProvenancePanel;
import java.util.ArrayList;

public class IntegrationModel {
    private Logger logger = Logger.getLogger(this.getClass());

    // String containing the repaired query
    private static String originalQuery = "";
    // String contining the top ranked query
    private static String topRankedQuery = "";
    // ArrayList of Query results
    private static ArrayList<QueryResults> queryResults = new ArrayList<>();
    // List of returned responses
    private static ArrayList<String> otherResponses = new ArrayList<>();
    // Similarity score of selected query
    private static double similarityScore = 0;
    // ProvN file
    private static String provNFilename = "";
    // Provenance Panel
    private static ProvenancePanel provenancePanel = new ProvenancePanel();



    /**
     * Get the String containing the original query
     * @return String contining the original query
     */
    public String getOriginalQuery() {
        return originalQuery;
    }

    /**
     * Set the String containing the original query
     * @param originalQuery
     */
    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    /**
     * Get the top ranked Query
     * @return String containing the top ranked query
     */
    public String getTopRankedQuery() {
        return topRankedQuery;
    }

    /**
     * Set the String containing the top ranked query
     * @param topRankedQuery String containing the top ranked query
     */
    public void setTopRankedQuery(String topRankedQuery) {
        this.topRankedQuery = topRankedQuery;
    }

    /**
     * Retrieve the array list containing the query results
     * @return ArraList containing the query results
     */
    public ArrayList<QueryResults> getQueryResults() {
        return queryResults;
    }

    /**
     * Set the ArrayList containing the query results
     * @param queryResults ArrayList containing the query results
     */
    public void setQueryResults(ArrayList<QueryResults> queryResults) {
        this.queryResults = queryResults;
    }

    /**
     * Retrieve the array list containing all responses
     * @return ArrayList containing all responses to original query
     */
    public ArrayList<String> getOtherResponses() {
        return otherResponses;
    }

    /**
     * Set the ArrayList containing all the responses
     * @param otherResponses ArrayList containing all responses to original query
     */
    public void setOtherResponses(ArrayList<String> otherResponses) {
        this.otherResponses = otherResponses;
    }

    /**
     * Retrieve the similarity score of the currently selected result
     * @return Double containing the similarity score of the selected query
     */
    public double getSimilarityScore() {
        return similarityScore;
    }


    /**
     * Set the similarity score of the selected query
     * @param similarityScore Double containing the similarity score of the selected query
     */
    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getProvNFilename() {
        return provNFilename;
    }

    public void setProvNFilename(String provNFilename) {
        this.provNFilename = provNFilename;
    }

    public ProvenancePanel getProvenancePanel() {
        return provenancePanel;
    }

    public void setProvenancePanel(ProvenancePanel provenancePanel) {
        this.provenancePanel = provenancePanel;
    }
}
