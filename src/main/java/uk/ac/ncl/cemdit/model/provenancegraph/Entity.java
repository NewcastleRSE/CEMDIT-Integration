package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;

public class Entity implements Element, Cloneable {

    private String ID;
    private Attributes attributes;

    public Entity(String ID) {
        this.ID = ID;
    }

    private Entity() {

    }

    @Override
    public String getAttString() {
        if (attributes == null)
            return "";
        else
            return attributes.toString();
    }

    public void setAttString(String attString) {
        attributes = new Attributes(attString);

    }

    @Override
    public void setAttributes(Attributes atts) {
        attributes = atts;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }


    @Override
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public ElementType getType() {
        return ElementType.ENTITY;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Entity newEntity = new Entity();
        newEntity.setID(getID());
        newEntity.setAttString(getAttString());
        return newEntity;
    }

    @Override
    public String getPROVN() {
        return "entity(" + getID() + ",["+getAttString()+"])";
    }


}
