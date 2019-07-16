package uk.ac.ncl.cemdit.model.provenancegraph;

/**
 * Class describing a PROV W3C attribute
 */
public class Attribute {


    /**
     * Attribute name
     */
    private String name;
    /**
     * Attribute value
     */
    private String value;
    /**
     * Type of Attribute eg. string, int, float
     */
    private Attributes.ElementType type;

    public Attribute(String name, String value) {
        this.name = name.trim();
        this.value = value.trim().replace("\"","");
        this.type = type;
    }

    /**
     * Return name of attribute
     * @return String with the name of the attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of attribute
     * @param name String containing the name of the attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return value of attribute
     * @return String with the value of the attribute
     */
    public String getValue() {
        return value;
    }

    /**
     * Set value of the attribute
     * @param value String containing the value of the attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Return the type of the attribute
     * @return The type of the element as a value from the enumerator ElementType
     */
    public Attributes.ElementType getType() {
        return type;
    }

    /**
     * Set the type of the attribute
     * @param type Type of the element as a value from the ElementType enumerator
     */
    public void setType(Attributes.ElementType type) {
        this.type = type;
    }

    /**
     * Get attribute as a string with a colon separating the key and the value, the type is omitted.
     * The key and string values are enclosed in quotes
     */
    public String getAttString() {
        return name.trim()  + "=" + "\"" + value.trim() + "\"";
    }

}
