package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;
import uk.ac.ncl.cemdit.view.StringsAndStuff;

import java.util.UUID;

public class HadMember implements Relation {
    private String id;
    private Entity entity1;
    private Entity entity2;
    private Attributes attributes;
    private String attString;

    /**
     * Default empty constructor
     */
    private HadMember() {

    }

    /**
     * Constructor with two entities provided
     *
     * @param entity1
     * @param entity2
     */
    public HadMember(Entity entity1, Entity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        id = UUID.randomUUID().toString();
    }

    /**
     * Get entity 1
     *
     * @return
     */
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
        return RelationType.HADMEMBER;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
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
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public void setElement1(Element element1) {
        entity1 = (Entity) element1;
    }

    @Override
    public void setElement2(Element element2) {
        entity2 = (Entity) element2;

    }

    public String getPROVN() {
        return StringsAndStuff.camelCase(getType().toString()) + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",[" + getAttString() + "])";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        HadMember newHadMember = new HadMember();
        try {
            newHadMember.setEntity1((Entity) getEntity1().clone());
            newHadMember.setEntity2((Entity) getEntity2().clone());
            newHadMember.setId(getId());
            newHadMember.setAttributes(getAttributes());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newHadMember;
    }
}
