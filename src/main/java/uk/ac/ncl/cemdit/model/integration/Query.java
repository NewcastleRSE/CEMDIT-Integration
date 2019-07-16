package uk.ac.ncl.cemdit.model.integration;

import java.util.ArrayList;

public class Query {
    private ArrayList<QueryResults> queryResults = new ArrayList<>();

    public Query() {

    }

    public ArrayList<QueryResults> getQueryResults() {
        return queryResults;
    }

    public void setQueryResults(ArrayList<QueryResults> queryResults) {
        this.queryResults = queryResults;
    }
}
