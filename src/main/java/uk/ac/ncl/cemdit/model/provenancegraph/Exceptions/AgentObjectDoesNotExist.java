package uk.ac.ncl.cemdit.model.provenancegraph.Exceptions;

/**
 * Error to be thrown if the agent requested does not exist
 */
public class AgentObjectDoesNotExist extends Exception {

    AgentObjectDoesNotExist() {
        super();
    }

    public AgentObjectDoesNotExist(String agentID) {
        super(agentID);
    }
}
