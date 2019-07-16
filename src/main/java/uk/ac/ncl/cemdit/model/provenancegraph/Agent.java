package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;

/**
 * This class defines a PROV W3C agent
 */
public class Agent implements Element {

    /**
     * Unique ID
     */
    private String ID;
    /**
     * Attributes as a string
     */
    private String attString;
    private Attributes attributes;


    /**
     * Constructor using unique ID
     * @param ID
     */
    public Agent(String ID) {
        this.ID = ID;
    }

    /**
     * Empty constructor
     */
    private Agent() {

    }

    @Override
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String getAttString() {
        return attString;
    }

    public void setAttString(String attString) {
        if (attString != null) {
            this.attString = attString;
        }

    }

    /**
     * Return attributes as type Attributes which is basically an ArrayList
     * @return
     */
    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * Create a new Attributes object passing the attributes as a comma separated string
     * @param attributes
     */
    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public ElementType getType() {
        return ElementType.AGENT;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Agent newAgent = new Agent();
        newAgent.setID(getID());
        newAgent.setAttString(getAttString());
        return newAgent;
    }

    //agent(cemdit:ag1,[cemdit:firstname="John", cemdit:lastname="Smith"])
    @Override
    public String getPROVN() {
        return "agent(" + getID() + ",["+getAttString()+"])";
    }

}
