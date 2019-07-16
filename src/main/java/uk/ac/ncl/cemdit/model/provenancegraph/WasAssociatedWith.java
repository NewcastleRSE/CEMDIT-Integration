package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class WasAssociatedWith implements Relation {

    private String id;
    private Activity activity;
    private Agent agent;
    private Attributes attributes;
    private String attString;


    public WasAssociatedWith(Activity activity, Agent agent) {
        id = UUID.randomUUID().toString();
        this.activity = activity;
        this.agent = agent;
    }

    public WasAssociatedWith(String id, Activity activity, Agent agent) {
        this.id = id;
        this.activity = activity;
        this.agent = agent;
    }

    private WasAssociatedWith() {

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public RelationType getType() {
        return RelationType.WASASSOCIATEDWITH;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getElement1() {
        return activity.getID();
    }

    @Override
    public String getElement2() {
        return agent.getID();
    }

    @Override
    public void setAttString(String attributes) {
        attString = attributes;
    }

    @Override
    public void setElement1(Element element1) {
        activity = (Activity) element1;

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

    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getAttString() {
        return attString;
    }

    public String getPROVN() {
        return "wasAssociatedWith"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",[" + getAttString() + "])";
    }

    @Override
    public Object clone() {
        WasAssociatedWith newWasAssociatedWith = new WasAssociatedWith();
        try {
            newWasAssociatedWith.setActivity((Activity)getActivity().clone());
            newWasAssociatedWith.setId(getId());
            newWasAssociatedWith.setAttributes(getAttributes());
            newWasAssociatedWith.setAgent((Agent)getAgent().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newWasAssociatedWith;
    }

}
