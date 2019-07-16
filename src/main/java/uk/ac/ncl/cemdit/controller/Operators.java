package uk.ac.ncl.cemdit.controller;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import uk.ac.ncl.cemdit.controller.Exceptions.ElementDoesntExistException;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidCollapseElementsException;
import uk.ac.ncl.cemdit.model.provenancegraph.*;
import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;
import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.RelationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Operators {

    private static Logger logger = Logger.getLogger(Operators.class);

    /**
     * Apply sibling blur to a graph
     * @param gsProvGraph GraphStream graph of provenance
     * @param provGraph openprovenance.org graph of provenance
     * @param blurSiblingsOfNode node who's siblings are to be blurred
     * @param newID the new id of the blurred node
     * @param reltype the relation type of the siblings to be blurred
     * @return
     */
    static public ProvGraph sibBlur(Graph gsProvGraph, ProvGraph provGraph, String blurSiblingsOfNode, String newID, RelationType reltype) {
        // The new element that will encapsulate all the blurred siblings
        Element newElement;
        // The provenance graph to which this operator should be applied
        ProvGraph newGraph = new ProvGraph();
        // Create stasis pod
        StasisPod statisPod = new StasisPod();
        // Add node to be blurred to the statisPod
        statisPod.put(provGraph.get(blurSiblingsOfNode));
        // set namespaces
        newGraph.setNamespaces(provGraph.getNamespaces());
        // set default namespace
        newGraph.setDefaultNamespace(provGraph.getDefaultNamespace());
        // Get element who's siblings are to be blurred
        Element element = provGraph.getElements().get(blurSiblingsOfNode);
        // If the element to be blurred is an entity:
        if (element.getType() == ElementType.ENTITY) {
            // The new element will also be an entity
            newElement = new Entity(newID);
            // Add the new element to the newGraph
            newGraph.put(newElement.getID(), newElement);
            // We specified in the policy that the relations should be wasGeneratedBy or used
            if (reltype == RelationType.WASGENERATEDBY || reltype == RelationType.USED) {
                // get all relations to find parent
                ArrayList<Element> parents = findParents(provGraph, provGraph.findElement(blurSiblingsOfNode), reltype);
                // for each parent found ...
                for (int p = 0; p < parents.size(); p++) {
                    Iterator<String> relationsIterator = provGraph.getRelations().keySet().iterator();
                    while (relationsIterator.hasNext()) {
                        Relation relation = provGraph.getRelations().get(relationsIterator.next());
                        //  relationType==WASGENERATEDBY, Element2==parent
                        if (reltype == RelationType.WASGENERATEDBY) {
                            if (relation.getType() == reltype && (relation.getElement2().equals(parents.get(p).getID()))) {
                                statisPod.put(provGraph.get(relation.getElement1()));
                            }
                            // relationType==USED, Element1==parent
                        } else if (reltype == RelationType.USED) {
                            if (relation.getType() == reltype && (relation.getElement1().equals(parents.get(p).getID()))) {
                                statisPod.put(provGraph.get(relation.getElement2()));
                            }

                        }
                    }
                }
            }
            // Add all elements that are not in the statisPod to the new graph
            provGraph.getElements().forEach((k, v) -> {
                if (!statisPod.isInPod(v)) {
                    newGraph.put(k, v);
                }
            });
            // Add all relations that are not in the stasisPod to the graph
            provGraph.getRelations().forEach((k, v) -> {
                // if element1 of the relation is in the stasispod, create a new relation for the
                // new graph and add the existing relation to the stasisPod
                if (statisPod.isInPod(provGraph.get(v.getElement1())) && statisPod.isInPod(provGraph.get(v.getElement2()))) {
                    //
                } else if (statisPod.isInPod(provGraph.get(v.getElement1()))) {
                    Relation newRelation = RelationFactory.getRelation(v.getType(), newElement, provGraph.get(v.getElement2()));
                    newGraph.put(newRelation.getId(), newRelation);
                } else if (statisPod.isInPod(provGraph.get(v.getElement2()))) {
                    Relation newRelation = RelationFactory.getRelation(v.getType(), newGraph.get(v.getElement1()), newElement);
                    newGraph.put(newRelation.getId(), newRelation);
                } else
                    newGraph.put(v.getId(), v);
            });
        }
        return newGraph;
    }

    static private ArrayList<Element> findParents(ProvGraph provGraph, Element child, RelationType reltype) {
        logger.debug("Find the parents of " + child.getID() + " with relation: " + reltype);
        HashMap<String, Relation> relations = provGraph.getRelations();
        // If relations is wasGeneratedBy, element1 is blurSiblingsOfNode and element2 is an Activity - then found parent
        Iterator<String> relationsIterator = relations.keySet().iterator();
        ArrayList<Element> parents = new ArrayList<>();
        // Find all the parents
        while (relationsIterator.hasNext()) {
            Relation v = provGraph.getRelations().get(relationsIterator.next());
            logger.debug("Check: " + v.getType() + "(" + v.getElement1() + "," + v.getElement2());
            Element parent = null;
            // Found a parent
            if (v.getType() == reltype && v.getElement1().equals(child.getID()) && reltype == RelationType.WASGENERATEDBY) {
                parent = provGraph.findElement(v.getElement2());
            } else if (v.getType() == reltype && v.getElement2().equals(child.getID()) && reltype == RelationType.USED) {
                parent = provGraph.findElement(v.getElement1());
            }
            if (!(parent == null)) {
                parents.add(parent);
                logger.debug("Parent is: " + parent.getID());
            }
        }
        logger.debug("Found " + parents.size() + " parents");
        return parents;
    }

    /**
     * Collapse nodes between specified nodes into one. Specified nodes must be of the same type or else an exception
     * will be thrown.
     *
     * @param gsProvGraph
     * @param collapseFrom
     * @param collapseTo
     * @param newID
     * @return
     */
    static public ProvGraph collapse(Graph gsProvGraph, ProvGraph provGraph, String collapseFrom, String collapseTo, String newID)
            throws InvalidCollapseElementsException, ElementDoesntExistException {
        StasisPod stasisPod = new StasisPod();
        final Element newNode;
        Element fromElement = provGraph.findElement(collapseFrom);
        Element toElement = provGraph.findElement(collapseTo);
        ArrayList<Relation> fromRelations = new ArrayList<>();

        if (provGraph.findElement(collapseFrom) == null || provGraph.findElement(collapseTo) == null) {
            throw new ElementDoesntExistException();
        }
        if (provGraph.findElement(collapseFrom).getClass() != provGraph.findElement(collapseTo).getClass()) {
            throw new InvalidCollapseElementsException("Elements have to be of the same type.");
        }
        if (provGraph.findElement(collapseFrom).getClass() == Agent.class) {
            throw new InvalidCollapseElementsException("Agents can't be collapsed");
        }
        // Create a new instance of ProvGraph to hold the collapsed graph
        ProvGraph newGraph = new ProvGraph();
        // set namespaces
        newGraph.setNamespaces(provGraph.getNamespaces());
        // set default namespace
        newGraph.setDefaultNamespace(provGraph.getDefaultNamespace());
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "result", "length");
        dijkstra.init(gsProvGraph);
        dijkstra.setSource(gsProvGraph.getNode(collapseFrom));
        dijkstra.compute();
        dijkstra.getPath(gsProvGraph.getNode(collapseTo)).size();
        for (Node node3 : dijkstra.getPathNodes(gsProvGraph.getNode(collapseTo))) {
            stasisPod.put(provGraph.findElement(node3.getId()));
        }
        // if collapsefrom is an entity, then the new node too will be an entity
        if (provGraph.findElement(collapseFrom) instanceof Entity) {
            newNode = new Entity(newID);
            // if collapsefrom is an activity, then the new node too will be an activity
        } else /*if (provGraph.findElement(collapseFrom) instanceof Activity)*/ {
            newNode = new Activity(newID);
        }
        // Add new node to new graph
        newGraph.put(newNode.getID(), newNode);
        // if either element attached to a relation is in the stasis pod, then add the relation to the stasis pod
        provGraph.getRelations().forEach((k, v) -> {
            if (stasisPod.isInPod(provGraph.get(v.getElement1())) || stasisPod.isInPod(provGraph.get(v.getElement2()))) {
                // add relation
                stasisPod.put(v);
                // create a new relation with pointer to element in stasis pod now pointing to the newNode
                //element 1 is not in stasis pod
                if (!stasisPod.isInPod(provGraph.get(v.getElement1()))
                        && stasisPod.isInPod(provGraph.get(v.getElement2()))) {
                    Relation newRelation = reWire(provGraph.get(v.getElement1()), newNode);
                    newGraph.put(newRelation.getId(), newRelation);
                    newGraph.put(provGraph.get(v.getElement1()).getID(), provGraph.get(v.getElement1()));
                }
                // element 2 is not in stasis pod
                if (!stasisPod.isInPod(provGraph.get(v.getElement2())) &&
                        stasisPod.isInPod(provGraph.get(v.getElement1()))) {
                    Relation newrelation = reWire(newNode, provGraph.get(v.getElement2()));
                    newGraph.put(newrelation.getId(), newrelation);
                    newGraph.put(provGraph.get(v.getElement2()).getID(), provGraph.get(v.getElement2()));
                }

            } else if (!stasisPod.isInPod(provGraph.get(v.getElement1())) &&
                    !stasisPod.isInPod(provGraph.get(v.getElement2()))) {
                newGraph.put(k, v);
            }
        });
        provGraph.getElements().forEach((k, v) -> {
            if (!stasisPod.isInPod(provGraph.get(k))) {
                newGraph.put(k, v);
            }
        });
        return newGraph;
    }


    /**
     * Symmetric blurring. This method takes three parameters, the first being graph to which the operator
     * is to be applied to, the id for the newly created (blurred) node and the id of the node to blur.
     * <p>
     * The symmetric blur collapses all elements attached, with relations, to the specified element. Thus all the
     * collapsed elements and the specified element becomes one new element
     *
     * @param provGraph  graph to apply operator to
     * @param newID      new id for the blurred element
     * @param blurfromID element to apply operator to
     * @return
     */
    static public ProvGraph symmetricBlur(ProvGraph provGraph, String newID, String blurfromID) throws InvalidBlurLevel {
        StasisPod stasisPod = new StasisPod();
        // Create a new instance of ProvGraph to hold the blurred graph
        ProvGraph newgraph = new ProvGraph();
        // Retrieve element to blur from
        Element blurfrom = provGraph.get(blurfromID);
        // set namespaces
        newgraph.setNamespaces(provGraph.getNamespaces());
        // set default namespace
        newgraph.setDefaultNamespace(provGraph.getDefaultNamespace());
        // The new element
        Element newElement;
        // An Entity blurs into an Activity and an Activity blurs into an Entity
        if (blurfrom instanceof Entity) {
            newElement = new Activity(newID);
        } else {
            newElement = new Entity(newID);
        }
        // Insert the new element into the new graph
        newgraph.put(newElement.getID(), newElement);
        // insert blur from element into statis pod
        stasisPod.put(blurfrom);
        // Insert first level of relations and elements into stasis pod
        provGraph.getRelations().forEach((k, v) -> {
            if (v.getElement1().equals(blurfromID)) {
                stasisPod.put(v);
                stasisPod.put(provGraph.get(v.getElement2()));
            }
            if (v.getElement2().equals(blurfromID)) {
                // insert relation into stasis pod
                stasisPod.put(v);
                // insert element attached to relation into stasis pod
                stasisPod.put(provGraph.get(v.getElement1()));
            }
        });

        // outreach level of relations to be rewired
        // outreach relations are relations with one element in the stasis pod and one outside of the stasis pod
        provGraph.getRelations().forEach((k, v) -> {
            Element element1 = null;
            Element element2 = null;
            Relation newRelation = null;
            if (!stasisPod.isInPod(provGraph.findElement(v.getElement1()))
                    && stasisPod.isInPod(provGraph.findElement(v.getElement2()))) {
                element1 = provGraph.findElement(v.getElement1());
                element2 = newElement;
                stasisPod.put(v);
            } else if (stasisPod.isInPod(provGraph.findElement(v.getElement1()))
                    && !stasisPod.isInPod(provGraph.findElement(v.getElement2()))) {
                element1 = newElement;
                element2 = provGraph.findElement(v.getElement2());
                stasisPod.put(v);
            }

            if (!(element1 == null) && !(element2 == null)) {
                newRelation = reWire(element1, element2);
//                if (element1.getType() == ElementType.AGENT
//                        && element2.getType() == ElementType.AGENT) {
//                    newRelation = new ActedOnBehalfOf((Agent) element1,
//                            (Agent) provGraph.findElement(v.getElement2()));
//                }
//                if (element1.getType() == ElementType.ENTITY
//                        && element2.getType() == ElementType.AGENT) {
//                    newRelation = new WasAttributedTo((Entity) element1, (Agent) element2);
//                }
//                if (element1.getType() == ElementType.ENTITY
//                        && element2.getType() == ElementType.ENTITY) {
//                    newRelation = new WasDerivedFrom((Entity) element1, (Entity) element2);
//                }
//                if (element1.getType() == ElementType.ENTITY
//                        && element2.getType() == ElementType.ACTIVITY) {
//                    newRelation = new WasGeneratedBy((Entity) element1, (Activity) element2);
//                }
//                if (element1.getType() == ElementType.ACTIVITY
//                        && element2.getType() == ElementType.ACTIVITY) {
//                    newRelation = new WasInformedBy((Activity) element1,
//                            (Activity) element2);
//                }
//                if (element1.getType() == ElementType.ACTIVITY
//                        && element2.getType() == ElementType.ENTITY) {
//                    newRelation = new Used((Activity) element1,
//                            (Entity) element2);
//                }
                newgraph.getRelations().put(newRelation.getId(), newRelation);
            }

        });

        // Add elements to new graph
        provGraph.getElements().forEach((k, v) -> {
            if (!stasisPod.isInPod(v)) {
                newgraph.put(k, v);
            }
        });

        // Add relations to new graph
        provGraph.getRelations().forEach((k, v) -> {
            if (!stasisPod.isInPod(v)) {
                newgraph.put(k, v);
            }
        });

        return newgraph;
    }

    private static Relation reWire(Element element1, Element element2) {
        Relation newRelation = null;
        if (element1.getType() == ElementType.AGENT
                && element2.getType() == ElementType.AGENT) {
            newRelation = new ActedOnBehalfOf((Agent) element1, (Agent) element2);
        }
        if (element1.getType() == ElementType.ENTITY
                && element2.getType() == ElementType.AGENT) {
            newRelation = new WasAttributedTo((Entity) element1, (Agent) element2);
        }
        if (element1.getType() == ElementType.ENTITY
                && element2.getType() == ElementType.ENTITY) {
            newRelation = new WasDerivedFrom((Entity) element1, (Entity) element2);
        }
        if (element1.getType() == ElementType.ENTITY
                && element2.getType() == ElementType.ACTIVITY) {
            newRelation = new WasGeneratedBy((Entity) element1, (Activity) element2);
        }
        if (element1.getType() == ElementType.ACTIVITY
                && element2.getType() == ElementType.ACTIVITY) {
            newRelation = new WasInformedBy((Activity) element1,
                    (Activity) element2);
        }
        if (element1.getType() == ElementType.ACTIVITY
                && element2.getType() == ElementType.ENTITY) {
            newRelation = new Used((Activity) element1,
                    (Entity) element2);
        }

        return newRelation;
    }


}