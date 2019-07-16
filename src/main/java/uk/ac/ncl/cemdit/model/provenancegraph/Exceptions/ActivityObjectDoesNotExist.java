package uk.ac.ncl.cemdit.model.provenancegraph.Exceptions;

/**
 * Error to be thrown if an activity type requested does not exist
 */
public class ActivityObjectDoesNotExist extends Exception {

    public ActivityObjectDoesNotExist(String activityID) {
        super(activityID);
    }
}
