package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class ActedOnBehalfOf implements Relation {
    private String id;
    private Agent agent1;
    private Agent agent2;
    private Attributes attributes;
    private String attString;

    public ActedOnBehalfOf() {
   }

    public ActedOnBehalfOf(Agent agent1, Agent agent2) {
        this.agent1 = agent1;
        this.agent2 = agent2;
        id = UUID.randomUUID().toString();
    }

    public ActedOnBehalfOf(String id, Agent agent1, Agent agent2) {
        this.agent1 = agent1;
        this.agent2 = agent2;
        this.id = id;
    }

    @Override
    public RelationType getType() {
        return RelationType.ACTEDONBEHALFOF;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getElement1() {
        return agent1.getID();
    }

    @Override
    public String getElement2() {
        return agent2.getID();
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
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public void setElement1(Element element1) {
        agent1 = (Agent) element1;
    }

    @Override
    public void setElement2(Element element2) {
        agent2 = (Agent) element2;
    }

    @Override
    public String getPROVN() {
        return "actedOnBehalfOf"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",[" + getAttString() + "])";
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


    private Agent getAgent1() {
        return agent1;
    }

    private Agent getAgent2() {
        return agent2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ActedOnBehalfOf actedOnBehalfOf = new ActedOnBehalfOf();
        try {
            actedOnBehalfOf.setElement1((Agent)getAgent1().clone());
            actedOnBehalfOf.setElement2((Agent)getAgent2().clone());
            actedOnBehalfOf.setId(getId());
            actedOnBehalfOf.setAttributes(getAttributes());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return actedOnBehalfOf;
    }
}
