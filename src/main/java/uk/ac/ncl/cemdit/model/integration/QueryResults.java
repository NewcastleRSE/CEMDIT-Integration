package uk.ac.ncl.cemdit.model.integration;

/**
 * This class is used to hold the CHAIn match result operator
 */
public class QueryResults {
    private String label;
    private String operator;
    private String value;
    private String newoperator;

    public QueryResults(String label, String operator, String value) {
        this.label = label;
        this.operator = operator;
        this.value = value;
        this.newoperator = "";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNewoperator() {
        return newoperator;
    }

    public void setNewoperator(String newoperator) {
        this.newoperator = newoperator;
    }
}
