package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;

import java.util.HashMap;

/**
 * This class defines a PROV W3C activity
 */
public class Activity implements Element, Cloneable{

    private String ID;
    private String attString;
    private Attributes attributes;

    /**
     * Constructor
     * @param ID String that uniquely identifies activity - should be complete with namespace eg. uo:acquire
     */
    public Activity(String ID) {
        this.ID = ID;
    }

    public Activity(String ID, HashMap<String,Entity> entities, HashMap<String,Agent> agents) {
        this.ID = ID;
    }

    /**
     * Empty Constructor
     */

    public Activity() {

    }

    /**
     * Get the ID of the activity
     * @return
     */
    @Override
    public String getID() {
        return ID;
    }

    /**
     * Set the ID of the activity
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Get the attributes of the activity as a string
     * @return String containing attributes as comma separated
     */
    @Override
    public String getAttString() {
        return attString;
    }

    /**
     * Set the attributes of the activity providing them as a comma separated string (format see PROV W3C)
     * @param attString
     */
    public void setAttString(String attString) {
            this.attString = attString;
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

    /**
     * Returns the type of this element as ACTIVITY
     * @return
     */
    @Override
    public ElementType getType() {
        return ElementType.ACTIVITY;
    }

    /**
     * Creat a clone of this attribute
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Activity newAcitivity = new Activity();
        newAcitivity.setID(getID());
        newAcitivity.setAttString(getAttString());

        return newAcitivity;
    }

    @Override
    public String getPROVN() {
        //"agent"
        return "agent(" + getID() + ",[" + getAttString() + "])";
    }

}
