package uk.ac.ncl.cemdit.model.provenancegraph;

import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

public class PartGeneratedBy extends WasGeneratedBy {

    public PartGeneratedBy(Entity entity, Activity activity) {
        super(entity, activity);
    }
    public PartGeneratedBy(String id, Entity entity, Activity activity) {
        super(id, entity, activity);
    }

    @Override
    public RelationType getType() {
        return RelationType.PARTGENERATEDBY;
    }

    @Override
    public String getPROVN() {
        return "partGeneratedBy"  + "(" + getId() + ";" + getElement1() + "," + getElement2() + ",-,[" + getAttString() + "])";
    }

}
