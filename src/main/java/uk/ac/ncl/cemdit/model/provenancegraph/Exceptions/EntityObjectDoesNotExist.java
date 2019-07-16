package uk.ac.ncl.cemdit.model.provenancegraph.Exceptions;

/**
 * Exception to throw if an entity with the given ID does not exist
 */
public class EntityObjectDoesNotExist extends Exception {

    public EntityObjectDoesNotExist(String entityID) {
        super(entityID);
    }
}
