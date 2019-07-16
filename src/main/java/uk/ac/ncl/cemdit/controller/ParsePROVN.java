package uk.ac.ncl.cemdit.controller;

import uk.ac.ncl.cemdit.model.provenancegraph.*;
import uk.ac.ncl.cemdit.model.provenancegraph.Exceptions.ActivityObjectDoesNotExist;
import uk.ac.ncl.cemdit.model.provenancegraph.Exceptions.AgentObjectDoesNotExist;
import uk.ac.ncl.cemdit.model.provenancegraph.Exceptions.EntityObjectDoesNotExist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Parse a PROVN file
 */
public class ParsePROVN {

    /**
     * Read a PROVN file and parse it into a ProvGraph object
     *
     * @param infile
     * @return
     */
    static public ProvGraph parseFile(File infile) {
        ProvGraph newgraph = new ProvGraph();
        Vector<String> namespaces = new Vector<>();
        try {
            Scanner scanner = new Scanner(infile);
            AtomicInteger linenumber = new AtomicInteger();
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                if (!line.startsWith("%")) {
                    linenumber.getAndIncrement();
                    if (line.startsWith("prefix")) {
                        namespaces.add(line);
                        newgraph.getNamespaces().add(line);
                    }
                    if (line.trim().startsWith("default")) {
                        namespaces.add(line);
                        newgraph.setDefaultNamespace(line);
                    }
                    if (line.startsWith("entity")) {
                        Entity entity = parseEntity(line.substring(7, line.length() - 1));
                        newgraph.put(entity.getID(), entity);
                    }
                    if (line.startsWith("activity")) {
                        Activity activity = parseActivity(line.substring(9, line.length() - 1));
                        newgraph.put(activity.getID(), activity);
                    }
                    if (line.startsWith("agent")) {
                        Agent agent = parseAgent(line.substring(6, line.length() - 1));
                        newgraph.put(agent.getID(), agent);
                    }
                    if (line.startsWith("wasGeneratedBy")) {
                        parseWasGeneratedBy(newgraph, line.substring(15, line.length() - 1), linenumber.get(), false);
                    }
                    if (line.startsWith("partGeneratedBy")) {
                        parseWasGeneratedBy(newgraph, line.substring(16, line.length() - 1), linenumber.get(), true);
                    }
                    if (line.startsWith("actedOnBehalfOf")) {
                        try {
                            parseActedOnBehalfOf(newgraph, line.substring(16, line.length() - 1));
                        } catch (AgentObjectDoesNotExist agentObjectDoesNotExist) {
                            agentObjectDoesNotExist.printStackTrace();
                        }

                    }
                    if (line.startsWith("wasInformedBy")) {
                        try {
                            parseWasInformedBy(newgraph, line.substring(14, line.length() - 1));
                        } catch (ActivityObjectDoesNotExist activityObjectDoesNotExist) {
                            activityObjectDoesNotExist.printStackTrace();
                        }
                    }
                    if (line.startsWith("wasAttributedTo")) {
                        try {
                            parseWasAttributedTo(newgraph, line.substring(16, line.length() - 1));
                        } catch (EntityObjectDoesNotExist entityObjectDoesNotExist) {
                            entityObjectDoesNotExist.printStackTrace();
                        } catch (AgentObjectDoesNotExist agentObjectDoesNotExist) {
                            agentObjectDoesNotExist.printStackTrace();
                        }

                    }
                    if (line.startsWith("wasDerivedFrom")) {
                        try {
                            parseWasDerivedFrom(newgraph, line.substring(15, line.length() - 1));
                        } catch (EntityObjectDoesNotExist entityObjectDoesNotExist) {
                            entityObjectDoesNotExist.printStackTrace();
                        }
                    }
                    if (line.startsWith("wasAssociatedWith")) {
                        try {
                            parseWasAssociatedWith(newgraph, line.substring(18, line.length() - 1));
                        } catch (ActivityObjectDoesNotExist activityObjectDoesNotExist) {
                            activityObjectDoesNotExist.printStackTrace();
                        } catch (AgentObjectDoesNotExist agentObjectDoesNotExist) {
                            agentObjectDoesNotExist.printStackTrace();
                        }
                    }
                    if (line.startsWith("used")) {
                        parseUsed(newgraph, line.substring(5, line.length() - 1));
                    }
                    if (line.startsWith("hadMember")) {
                        try {
                            parseHadMember(newgraph, line.substring(10, line.length() - 1));
                        } catch (EntityObjectDoesNotExist entityObjectDoesNotExist) {
                            entityObjectDoesNotExist.printStackTrace();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EntityObjectDoesNotExist entityObjectDoesNotExist) {
            entityObjectDoesNotExist.printStackTrace();
        } catch (ActivityObjectDoesNotExist activityObjectDoesNotExist) {
            activityObjectDoesNotExist.printStackTrace();
        }
        return newgraph;
    }

    /**
     * Parse a string into an Entity and return Entity
     *
     * @param line
     * @return
     */
    static Entity parseEntity(String line) {
        String[] tokens = line.split(",");
        Entity entity = new Entity(tokens[0]);
        if (tokens.length > 1) {
            try {
                if (line.indexOf("[") > -1 && line.indexOf("]") > 1) {
                    String atts = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                    entity.setAttString(atts);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return entity;
    }

    /**
     * Parse a string into an Activity and return Activity
     *
     * @param line
     * @return
     */
    static Activity parseActivity(String line) {
        String[] tokens = line.split(",");
        Activity activity = new Activity(tokens[0]);
        if (tokens.length > 1) {
            if (line.indexOf("[") > -1 && line.indexOf("]") > 1) {
                String atts = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                activity.setAttString(atts);
            }
        }
        return activity;
    }

    /**
     * Parse a string into and Agent and return Agent
     *
     * @param line
     * @return
     */
    static Agent parseAgent(String line) {
        String[] tokens = line.split(",");
        Agent agent = new Agent((tokens[0]));
        if (tokens.length > 1) {
            if (line.indexOf("[") > -1 && line.indexOf("]") > 1) {
                String atts = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                agent.setAttString(atts);
            }
        }
        return agent;
    }

    static void parseWasInformedBy(ProvGraph g, String line) throws ActivityObjectDoesNotExist {
        String[] tokens = line.split(",");
        if (tokens[0].contains(";")) {
            String[] tokens2 = tokens[0].split(";");
            tokens[0] = tokens2[1];
        }
        String activityID1 = tokens[0].trim();
        String activityID2 = tokens[1].trim();
        if (g.containsKey(activityID1)) {
            Activity activity1 = (Activity) g.findElement(activityID1);
            if (g.containsKey(activityID2)) {
                Activity activity2 = (Activity) g.findElement(activityID2);
                Relation newRelation = new WasInformedBy(activity1, activity2);
                g.put(newRelation.getId(), newRelation);
            } else {
                throw new ActivityObjectDoesNotExist(activityID2);
            }
        } else {
            throw new ActivityObjectDoesNotExist(activityID1);
        }
    }

    static void parseActedOnBehalfOf(ProvGraph g, String line) throws AgentObjectDoesNotExist {
        String[] tokens = line.split(",");
        if (tokens[0].contains(";")) {
            String[] tokens2 = tokens[0].split(";");
            tokens[0] = tokens2[1];
        }
        String agentID1 = tokens[0].trim();
        String agentID2 = tokens[1].trim();
        if (g.containsKey(agentID1)) {
            Agent agent1 = (Agent) g.findElement(agentID1);
            if (g.containsKey(agentID2)) {
                Agent agent2 = (Agent) g.findElement(agentID2);
                Relation newRelation = new ActedOnBehalfOf(agent1, agent2);
                g.put(newRelation.getId(), newRelation);
            } else {
                throw new AgentObjectDoesNotExist(agentID2);
            }
        } else {
            throw new AgentObjectDoesNotExist(agentID1);
        }
    }


    /**
     * Parse a string into a wasDerivedFrom relationship
     * and add to graph
     *
     * @param line
     */
    static void parseWasDerivedFrom(ProvGraph g, String line) throws EntityObjectDoesNotExist {
        String[] tokens = line.split(",");
        if (tokens[0].contains(";")) {
            String[] tokens2 = tokens[0].split(";");
            tokens[0] = tokens2[1];
        }
        String entityID1 = tokens[0].trim();
        String entityID2 = tokens[1].trim();
        if (g.containsKey(entityID1)) {
            Entity entity1 = (Entity) g.findElement(entityID1);
            if (g.containsKey(entityID2)) {
                Entity entity2 = (Entity) g.findElement(entityID2);
                Relation newRelation = new WasDerivedFrom(entity1, entity2);
                g.put(newRelation.getId(), newRelation);
            } else {
                throw new EntityObjectDoesNotExist(entityID2);
            }
        } else {
            throw new EntityObjectDoesNotExist(entityID1);
        }
    }

    /**
     * Parse a string into a wasAttributedTo relationship
     * and add to graph
     *
     * @param line
     */
    static void parseWasAttributedTo(ProvGraph g, String line) throws EntityObjectDoesNotExist, AgentObjectDoesNotExist {
        String[] tokens = line.split(",");
        if (tokens[0].contains(";")) {
            String[] tokens2 = tokens[0].split(";");
            tokens[0] = tokens2[1];
        }
        String entityID = tokens[0].trim();
        String agentID = tokens[1].trim();
        if (g.containsKey(entityID)) {
            Entity entity = (Entity) g.findElement(entityID);
            if (g.containsKey(agentID)) {
                Agent agent = (Agent) g.findElement(agentID);
                Relation newRelation = new WasAttributedTo(entity, agent);
                g.put(newRelation.getId(), newRelation);
            } else {
                throw new AgentObjectDoesNotExist(agentID);
            }
        } else {
            throw new EntityObjectDoesNotExist(entityID);
        }
    }

    static void parseWasAssociatedWith(ProvGraph g, String line) throws ActivityObjectDoesNotExist, AgentObjectDoesNotExist {
        String[] tokens = line.split(",");
        String activityID = tokens[0].trim();
        String agentID = tokens[1].trim();
        if (g.containsKey(activityID)) {
            Activity activity = (Activity) g.findElement(activityID);
            if (g.containsKey(agentID)) {
                Agent agent = (Agent) g.findElement(agentID);
                Relation newRelation = new WasAssociatedWith(activity, agent);
                g.put(((WasAssociatedWith) newRelation).getId(), newRelation);
            } else {
                throw new AgentObjectDoesNotExist(agentID);
            }
        } else {
            throw new ActivityObjectDoesNotExist(activityID);
        }
    }

    static void parseUsed(ProvGraph g, String line) {
        String[] tokens = line.split(",");
        String relid = null;
        if (tokens[0].contains(";")) {
            String[] tokens2 = tokens[0].split(";");
            relid = tokens2[0].trim();
            tokens[0] = tokens2[1];
        }
        String activityID = tokens[0].trim();
        String entityID = tokens[1].trim();
        if (g.containsKey(activityID)) {
            Activity activity = (Activity) g.findElement(activityID);
            Entity entity = (Entity) g.findElement(entityID);
            Relation newRelation;
            if (relid == null) {
                newRelation = new Used(activity, entity);
            } else {
                newRelation = new Used(relid, activity, entity);
            }
            g.put((newRelation).getId(), newRelation);
        } else {
            try {
                throw new ActivityObjectDoesNotExist(activityID);
            } catch (ActivityObjectDoesNotExist activityObjectDoesNotExist) {
                activityObjectDoesNotExist.printStackTrace();
            }
        }
    }

    /**
     * Parse a string into a wasGeneratedBy relationship
     * and add to graph
     *
     * @param line
     */
    static void parseWasGeneratedBy(ProvGraph g, String line, int linenumber, boolean part) throws EntityObjectDoesNotExist, ActivityObjectDoesNotExist {
        String[] tokens = line.split(",");
        String relid = null;
        if (tokens[0].contains(";")) {
            // split id
            String[] tokens2 = tokens[0].split(";");
            // relation id
            relid = tokens2[0].trim();
            tokens[0] = tokens2[1].trim();
        }
        String entityID = tokens[0].trim();
        String activityID = tokens[1].trim();
        if (g.containsKey(entityID)) {
            Entity entity = (Entity) g.findElement(entityID);
            if (g.containsKey(activityID)) {
                Activity activity = (Activity) g.findElement(activityID);
                if (part) {
                    PartGeneratedBy partGeneratedBy;
                    if (relid == null)
                        partGeneratedBy = new PartGeneratedBy(entity, activity);
                    else
                        partGeneratedBy = new PartGeneratedBy(relid, entity, activity);
                    g.put(partGeneratedBy.getId(), partGeneratedBy);

                } else {
                    WasGeneratedBy wasGeneratedBy;
                    if (relid == null)
                        wasGeneratedBy = new WasGeneratedBy(entity, activity);
                    else
                        wasGeneratedBy = new WasGeneratedBy(relid, entity, activity);
                    g.put(wasGeneratedBy.getId(), wasGeneratedBy);
                }
            } else {
                throw new ActivityObjectDoesNotExist(activityID);
            }
        } else {
            throw new EntityObjectDoesNotExist(entityID);
        }
    }

    /**
     * @param g
     * @param line
     * @throws EntityObjectDoesNotExist
     */
    static void parseHadMember(ProvGraph g, String line) throws EntityObjectDoesNotExist {
        String[] tokens = line.split(",");
        if (tokens[0].contains(";")) {
            String[] tokens2 = tokens[0].split(";");
            tokens[0] = tokens2[1];
        }
        String entityID1 = tokens[0].trim();
        String entityID2 = tokens[1].trim();
        if (g.containsKey(entityID1)) {
            Entity entity1 = (Entity) g.findElement(entityID1);
            if (g.containsKey(entityID2)) {
                Entity entity2 = (Entity) g.findElement(entityID2);
                Relation newRelation = new HadMember(entity1, entity2);
                g.put((newRelation).getId(), newRelation);
            } else {
                throw new EntityObjectDoesNotExist(entityID2);
            }
        } else {
            throw new EntityObjectDoesNotExist(entityID1);
        }
    }


}

