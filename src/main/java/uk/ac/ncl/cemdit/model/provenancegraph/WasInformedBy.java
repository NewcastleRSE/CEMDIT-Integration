package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class WasInformedBy implements Relation {
    private String id;
    private Activity activity1;
    private Activity activity2;
    private Attributes attributes;
    private String attString;

    public WasInformedBy(Activity activity1, Activity activity2) {
        id = UUID.randomUUID().toString();
        this.activity1 = activity1;
        this.activity2 = activity2;
    }

    public WasInformedBy(String id, Activity activity1, Activity activity2) {
        this.id = id;
        this.activity1 = activity1;
        this.activity2 = activity2;
    }

    public WasInformedBy() {
    }

    @Override
    public RelationType getType() {
        return RelationType.WASINFORMEDBY;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getElement1() {
        return activity1.getID();
    }

    @Override
    public String getElement2() {
        return activity2.getID();
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
    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }


    public void setId(String id) {
        this.id = id;
    }

    public Activity getActivity1() {
        return activity1;
    }

    public Activity getActivity2() {
        return activity2;
    }

    @Override
    public void setElement1(Element element1) {
        activity1 = (Activity) element1;
    }

    @Override
    public void setElement2(Element element2) {
        activity2 = (Activity) element2;
    }

    public String getPROVN() {
        return "wasInformedBy" + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",[" + getAttString() + "])";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        WasInformedBy newWasInformedBy = new WasInformedBy();
        try {
            newWasInformedBy.setId(getId());
            newWasInformedBy.setAttributes(getAttributes());
            newWasInformedBy.setElement1((Activity) getActivity1().clone());
            newWasInformedBy.setElement2((Activity) getActivity2().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newWasInformedBy;
    }

}
