package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class WasAttributedTo implements Relation {

    private String id;
    private Entity entity;
    private Agent agent;
    private Attributes attributes;
    private String attString="";


    public WasAttributedTo(Entity entity, Agent agent) {
        this.entity = entity;
        this.agent = agent;
        id = UUID.randomUUID().toString();
    }

    public WasAttributedTo(String id, Entity entity, Agent agent) {
        this.entity = entity;
        this.agent = agent;
        this.id = id;
    }

    private WasAttributedTo() {
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    public RelationType getType() {
        return RelationType.WASATTRIBUTEDTO;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getElement1() {
        return entity.getID();
    }

    @Override
    public String getElement2() {
        return agent.getID();
    }

    @Override
    public void setElement1(Element element1) {
        entity = (Entity) element1;
    }

    @Override
    public void setElement2(Element element2) {
        agent = (Agent) element2;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getAttString() {
        return attString;
    }

    @Override
    public void setAttString(String attString) {
        this.attString = attString;
    }

    public String getPROVN() {
        return "wasAttributedTo"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",[" + getAttString() + "])";
    }

    @Override
    public Object clone() {
        WasAttributedTo newWasAttributedTo = new WasAttributedTo();
        try {
            newWasAttributedTo.setEntity((Entity) getEntity().clone());
            newWasAttributedTo.setId(getId());
            newWasAttributedTo.setAttributes(getAttributes());
            newWasAttributedTo.setAgent((Agent)getAgent().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newWasAttributedTo;
    }

}
