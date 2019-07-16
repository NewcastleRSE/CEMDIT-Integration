package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.UUID;

public class Used implements Relation {

    private String id;
    private Activity activity;
    private Entity entity;
    private Attributes attributes;
    private String attString = "";


    public Used(Activity activity, Entity entity) {
        this.activity = activity;
        this.entity = entity;
        id = UUID.randomUUID().toString();
    }

    public Used(String ID, Activity activity, Entity entity) {
        this.id = ID;
        this.activity = activity;
        this.entity = entity;
    }

    private Used() {

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public RelationType getType() {
        return RelationType.USED;
    }

    @Override
    public String getElement1() {
        return activity.getID();
    }

    @Override
    public String getElement2() {
        return getEntity().getID();
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
        activity = (Activity) element1;
    }

    @Override
    public void setElement2(Element element2) {
        entity = (Entity) element2;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getPROVN() {
        return "used"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",-,[" + getAttString() + "])";
    }

    @Override
    public Object clone() {
        Used newUsed = new Used();
        try {
            newUsed.setActivity((Activity)getActivity().clone());
            newUsed.setId(getId());
            newUsed.setEntity((Entity)getEntity().clone());
            newUsed.setAttributes(getAttributes());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newUsed;
    }

}
