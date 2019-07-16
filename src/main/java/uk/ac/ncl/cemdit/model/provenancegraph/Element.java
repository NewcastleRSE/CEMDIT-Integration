package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;

/**
 * Interface from which all PROV W3C standard elements should be extended
 */
public interface Element {

    /**
     * Return ID of element
     * @return
     */
    String getID();

    /**
     * Return attributes as a comma separated string, with the key and value separated by a colon and the key and strings
     * enclosed in quotes
     * @return
     */
    String getAttString();

    /**
     * Set the attributes as a comma separated string with the key and value separated by a colon and the key and strings
     * enclosed in quotes
     * @param atts
     */
    void setAttString(String atts);

    /**
     * Set the attributes of the element
     * @param attributes
     */
    void setAttributes(Attributes attributes);

    /**
     * Retrieve all attributes (class Attributes extends ArrayList)
     * @return
     */
    Attributes getAttributes();

    /**
     * Returns the type of the element
     * @return
     */
    ElementType getType();

    /**
     * Required for deep cloning
     * @return
     * @throws CloneNotSupportedException
     */
    Object clone()throws CloneNotSupportedException;

    /**
     * Return provn statement for creating this element
     * @return provn statement as string
     */
    String getPROVN();

}
