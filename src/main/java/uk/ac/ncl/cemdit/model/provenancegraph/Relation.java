package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

public interface Relation {

    RelationType getType();
    String getId();
    void setId(String id);
    String getElement1();
    String getElement2();

    /**
     * Set attributes as a comma separated list with a colon separating the attribute id and value and the key and
     * string values enclosed in quotes
     * @param attributes
     */
    void setAttString(String attributes);

    /**
     * Set attributes as a comma separated list with a colon separating the attribute id and value and the key and
     * string values enclosed in quotes
     * @return attributes as a string
     */
    String getAttString();

    /**
     * Set attributes
     * @param attributes
     */
    void setAttributes(Attributes attributes);

    /**
     * Get attributes
     * @return attributes
     */
    Attributes getAttributes();
    void setElement1(Element element1);
    void setElement2(Element element2);
    String getPROVN();
    Object clone()throws CloneNotSupportedException;

}
