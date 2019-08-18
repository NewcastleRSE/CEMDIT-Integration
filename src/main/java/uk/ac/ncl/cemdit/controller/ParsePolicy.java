/**
 * 2018/10/01 Add % to comment line in policy
 */
package uk.ac.ncl.cemdit.controller;

import org.apache.log4j.Logger;
import org.graphstream.graph.Graph;
import uk.ac.ncl.cemdit.controller.Exceptions.ElementDoesntExistException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidCollapseElementsException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidPolicySyntaxException;
import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;
import uk.ac.ncl.cemdit.model.provenancegraph.Relation;

import java.util.*;

public class ParsePolicy {

    private static Logger logger = Logger.getLogger(Operators.class);

    /**
     * Parse multiple lines of policy. Return parsed lines as a vector of String arrays. The
     * first element, [0], in each array contains the operator and the rest are parameters.
     *
     * @param policy
     * @return
     * @throws InvalidPolicySyntaxException
     */
    static public Vector<String[]> parseLines(String policy) throws InvalidPolicySyntaxException {
        Vector<String[]> returns = new Vector<>();
        String operator = null;
        Vector<String> ret = null;
        if (policy != null || !policy.equals("")) {
            String[] lines = policy.split("\n");
            for (String line : lines) {
                if (!line.trim().equals("") || !(line.trim().startsWith("%"))) {
                    try {
                        operator = line.substring(0, line.indexOf('('));
                        String[] tokens = line.substring(line.indexOf("(") + 1, line.indexOf(")")).split(",");
                        ret = new Vector<String>(Arrays.asList(tokens));
                        ret.add(0, operator);
                    } catch (StringIndexOutOfBoundsException e) {
                        throw new InvalidPolicySyntaxException();

                    }
                    returns.add(ret.toArray(new String[ret.size()]));
                }
            }
        }
        return returns;
    }


    /**
     * @param provGraph
     * @param policies
     * @return
     * @throws InvalidPolicySyntaxException
     * @throws InvalidBlurLevel
     */
    static public ProvGraph applyPolicy(Graph gsGraph, ProvGraph provGraph, Vector<String[]> policies)
            throws InvalidPolicySyntaxException, InvalidBlurLevel, CloneNotSupportedException, ElementDoesntExistException,
            InvalidCollapseElementsException {
        ProvGraph blurredProvGraph = null;
        if (policies != null && policies.size() > 0) {
            ProvGraph tmpProvGraph = (ProvGraph) provGraph.clone();
            for (String[] policy : policies) {
                if (policy.length < 2) {
                    logger.debug("Invalid Policy length: " + policy.length);
                    throw new InvalidPolicySyntaxException();
                } else if (policy != null && (!policy.equals(""))) {
                    // SYMBLUR
                    //policy[0] = operator
                    //policy[1] = ID_to_blur
                    //policy[2] = level of blurring
                    //policy[3] = new_ID (for blurred node)
                    if (policy[0].toLowerCase().equals("symblur")) {
                        int blurlevel = Integer.valueOf(policy[2].trim());
                        String idToBeBlurred = policy[1];
                        String newID;
                        if (policy.length == 4) {
                            newID = policy[3];
                        } else if (policy.length == 3) {
                            newID = newID = UUID.randomUUID().toString();
                        } else throw new InvalidPolicySyntaxException();
                        if (policy.length == 4) {
                            newID = policy[3];
                        } else if (policy.length == 3) {
                            newID = newID = UUID.randomUUID().toString();
                        } else throw new InvalidPolicySyntaxException();
                        for (int level = 0; level < blurlevel; level++) {
                            ProvGraph tmpBlurredProvGraph = null;
                            if (policy[0].equals("symblur")) {
                                // TODO
                                tmpBlurredProvGraph = Operators.symmetricBlur(tmpProvGraph, newID, idToBeBlurred);
                            }
                            tmpProvGraph = (ProvGraph) tmpBlurredProvGraph.clone();
                            idToBeBlurred = newID;
                            newID += ("_" + level);
                        }
                    } else
                    // COLLAPSE
                    //policy[0] = operator
                    //policy[1] = ID_to_blur_from
                    //policy[2] = ID_to_blur_to
                    //policy[3] = new_ID (for collapsed node)
                    if (policy[0].toLowerCase().equals("collapse")) {
                        String blurFromID = null;
                        String blurToID = null;
                        String newID = null;
                        if (policy.length == 4) {
                            blurFromID = policy[1];
                            blurToID = policy[2];
                            if (policy[3].equals("")) {
                                newID = UUID.randomUUID().toString();
                            } else {
                                newID = policy[3];
                            }
                        } else if (policy.length == 2) {
                            newID = UUID.randomUUID().toString();
                        } else throw new InvalidPolicySyntaxException();
                        try {
                            tmpProvGraph = Operators.collapse(gsGraph, tmpProvGraph, blurFromID, blurToID, newID);
                        } catch (InvalidCollapseElementsException invalidCollapseElements) {
                            throw new InvalidCollapseElementsException();
                        }
                    } else
                    // COLLAPSE SHORTEST PATH
                    //policy[0] = operator
                    //policy[1] = ID_to_blur_from
                    //policy[2] = ID_to_blur_to
                    //policy[3] = new_ID (for collapsed node)
                    if (policy[0].toLowerCase().equals("collapseshort")) {
                        String blurFromID = null;
                        String blurToID = null;
                        String newID = null;
                        if (policy.length == 4) {
                            blurFromID = policy[1];
                            blurToID = policy[2];
                            if (policy[3].equals("")) {
                                newID = UUID.randomUUID().toString();
                            } else {
                                newID = policy[3];
                            }
                        } else if (policy.length == 2) {
                            newID = UUID.randomUUID().toString();
                        } else throw new InvalidPolicySyntaxException();
                        try {
                            tmpProvGraph = Operators.collapse(gsGraph, tmpProvGraph, blurFromID, blurToID, newID);
                        } catch (InvalidCollapseElementsException invalidCollapseElements) {
                            throw new InvalidCollapseElementsException();
                        }
                    } else
                    // SIBLING BLUR
                    //policy[0] = operator
                    //policy[1] = ID_to_blur_from
                    //policy[2] = new_ID (for collapsed node)
                    //policy[3] = type (used or wasGeneratedBy)
                    if (policy[0].toLowerCase().equals("sibblur")) {
                        String blurFromID = null;
                        String newID = null;
                        if (policy.length == 4) {
                            blurFromID = policy[1];
                            if (policy[2].equals("")) {
                                newID = UUID.randomUUID().toString();
                            } else {
                                newID = policy[2];
                            }
                        } else if (policy.length == 2) {
                            newID = UUID.randomUUID().toString();
                        } else throw new InvalidPolicySyntaxException();
                        tmpProvGraph = Operators.sibBlur(gsGraph, tmpProvGraph, blurFromID, newID, RelationType.valueOf(policy[3].toUpperCase()));
//                        try {
//                        } catch (InvalidCollapseElementsException invalidCollapseElements) {
//                            throw new InvalidCollapseElementsException();
//                        }
                    } else {
                        logger.debug("Operator does not exist.");
                    }
                }

            }
            blurredProvGraph = tmpProvGraph;
        } else {
            blurredProvGraph = (ProvGraph) provGraph.clone();
        }
        return blurredProvGraph;
    }

    static private Vector<String> findRelatedActivities(ProvGraph blurredProvGraph, String entityID) {
        Vector<String> activityIDs = new Vector();
        HashMap<String, Relation> relationHashMap = blurredProvGraph.getRelations();

        // Find relations attached to entity
        Iterator<Map.Entry<String, Relation>> iterator = relationHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Relation rel = iterator.next().getValue();
            if (rel.getElement2().equals(entityID) && rel.getElement1().equals(entityID)) {
                //
            } else if (rel.getElement1().equals(entityID)) {
                activityIDs.add(rel.getElement2());
            } else if (rel.getElement2().equals(entityID)) {
                activityIDs.add(rel.getElement1());
            }
        }

        // Find activities attached to entity
        return activityIDs;

    }
}
