package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class WasDerivedFrom implements Relation {
    private String id;
    private Entity entity1;
    private Entity entity2;
    private Attributes attributes;
    private String attString;


    public WasDerivedFrom(Entity entity1, Entity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        id = UUID.randomUUID().toString();
    }

    public WasDerivedFrom(String id, Entity entity1, Entity entity2) {
        this.id = id;
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    private WasDerivedFrom() {

    }

    public Entity getEntity1() {
        return entity1;
    }

    public void setEntity1(Entity entity1) {
        this.entity1 = entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public void setEntity2(Entity entity2) {
        this.entity2 = entity2;
    }

    @Override
    public RelationType getType() {
        return RelationType.WASDERIVEDFROM;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getElement1() {
        return entity1.getID();
    }

    @Override
    public String getElement2() {
        return entity2.getID();
    }

    @Override
    public void setAttString(String attributes) {
        attString = attributes;
    }

    @Override
    public String getAttString() {
        return attString;
    }

    @Override
    public void setElement1(Element element1) {
        entity1 = (Entity) element1;
    }

    @Override
    public void setElement2(Element element2) {
        entity2 = (Entity) element2;

    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getPROVN() {
        return "wasDerivedFrom"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",[" + (getAttString()==null?"":getAttString()) + "])";
    }

    @Override
    public Object clone() {
        WasDerivedFrom newWasDerivedFrom = new WasDerivedFrom();
        try {
            newWasDerivedFrom.setEntity1((Entity) getEntity1().clone());
            newWasDerivedFrom.setEntity2((Entity) getEntity2().clone());
            newWasDerivedFrom.setId(getId());
            newWasDerivedFrom.setAttributes(getAttributes());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newWasDerivedFrom;
    }
}
