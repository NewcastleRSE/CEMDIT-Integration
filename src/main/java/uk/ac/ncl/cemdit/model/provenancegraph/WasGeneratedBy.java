package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class WasGeneratedBy implements Relation {

    private String id;
    private Entity entity;
    private Activity activity;
    private Attributes attributes;
    private String attString="";

    public WasGeneratedBy(Entity entity, Activity activity) {
        id = UUID.randomUUID().toString();
        this.entity = entity;
        this.activity = activity;
    }

    public WasGeneratedBy(String id, Entity entity, Activity activity) {
        this.id = id;
        this.entity = entity;
        this.activity = activity;
    }

    private WasGeneratedBy() {

    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RelationType getType() {
        return RelationType.WASGENERATEDBY;
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
        return entity.getID();
    }

    @Override
    public String getElement2() {
        return activity.getID();
    }

    @Override
    public void setElement1(Element element1) {
        entity = (Entity) element1;
    }

    @Override
    public void setElement2(Element element2) {
        activity = (Activity) element2;

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
        return "wasGeneratedBy"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",-,[" + getAttString() + "])";
    }

    @Override
    public Object clone() {
        WasGeneratedBy newWasGeneratedBy = new WasGeneratedBy();
        try {
            newWasGeneratedBy.setEntity((Entity) getEntity().clone());
            newWasGeneratedBy.setId(getId());
            newWasGeneratedBy.setAttributes(getAttributes());
            newWasGeneratedBy.setActivity((Activity) getActivity().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newWasGeneratedBy;
    }
}
