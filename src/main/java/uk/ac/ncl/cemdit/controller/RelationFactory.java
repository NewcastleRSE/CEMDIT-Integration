package uk.ac.ncl.cemdit.controller;

import uk.ac.ncl.cemdit.model.provenancegraph.*;
import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

public class RelationFactory {

    public static Relation getRelation(RelationType reltype, Element element1, Element element2) {
        switch (reltype) {
            case USED: return new Used((Activity)element1, (Entity)element2);
            case WASGENERATEDBY: return new WasGeneratedBy((Entity)element1, (Activity)element2);
            case HADMEMBER: return new HadMember((Entity)element1, (Entity)element2);
            case WASINFORMEDBY: return new WasInformedBy((Activity)element1, (Activity)element2);
            case WASDERIVEDFROM: return new WasDerivedFrom((Entity)element1, (Entity)element2);
            case ACTEDONBEHALFOF: return new ActedOnBehalfOf((Agent)element1, (Agent)element2);
            case PARTGENERATEDBY: return new PartGeneratedBy((Entity)element1, (Activity)element2);
            case WASATTRIBUTEDTO: return new WasAttributedTo((Entity)element1, (Agent)element2);
            case WASASSOCIATEDWITH: return new WasAssociatedWith((Activity)element1, (Agent)element2);
        }
        return null;
    }
}
