package uk.ac.ncl.cemdit.model.provenancegraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * I could not find a class in the openprovence.org API that would hold a graph
 * representation of the provenance. This class can be replaced if such a class
 * is discovered in the API.
 * Class to create a graph of relations and elements or captured provenance
 */
public class ProvGraph implements Cloneable {

    /**
     * Namespaces used in the provenance
     */
    private Vector<String> namespaces = new Vector<>();
    /**
     * A HashMap holding the provenance relations with the key being the unique element id
     */
    private HashMap<String, Relation> relations = new HashMap<>();
    /**
     * A HashMap holding the provenance elements with the key being the unique element id
     */
    private HashMap<String, Element> elements = new HashMap<>();
    /**
     * The default namespace
     */
    private String defaultNamespace = " default <http://cemdit.org/>:";

    /**
     * Find and return an element in the elements HashMap
     *
     * @param ID Element ID to search for
     * @return null if not found or an element object if found
     */
    public Element findElement(String ID) {
        if (elements.containsKey(ID)) {
            return elements.get(ID);
        } else {
            return null;
        }
    }

    /**
     * Return namespace provn lines as Strings
     *
     * @return a vector of strings
     */
    public Vector<String> getNamespaces() {
        return namespaces;
    }

    /**
     * Store namespace provn lines as Strings
     *
     * @param namespaces a vector of strings containing the namespaces as provn statements
     */
    public void setNamespaces(Vector<String> namespaces) {
        this.namespaces = namespaces;
    }

    /**
     * Return the HashMap containing the relations
     *
     * @return a HashMap of relation objects
     */
    public HashMap<String, Relation> getRelations() {
        return relations;
    }

    /**
     * Set a HashMap containing all relations. This method is probably not
     * needed.
     *
     * @param relations a HashMap of relation objects
     */
    public void setRelations(HashMap<String, Relation> relations) {
        this.relations = relations;
    }

    /**
     * Retrieve an element from the elements HashMap with the given id as the key
     *
     * @param id the key of the element to find
     * @return an Element object with the given key
     */
    public Element get(String id) {
        return elements.get(id);
    }

    /**
     * Put an element into the elements HashMap with the given id as the key
     *
     * @param id      String containing the ID of the element
     * @param element The element object to be inserted into the HashMap
     * @return
     */
    public Element put(String id, Element element) {
        return elements.put(id, element);
    }

    /**
     * Returns elements HashMap as a set
     *
     * @return A Set of element objects retrieved from the elements HashMap
     */
    public Set entrySet() {
        return elements.entrySet();
    }

    /**
     * Returns true if the elements HashMap contains the given key
     *
     * @param id key as a String of the element required
     * @return boolean which is true if element is found, false if not found
     */
    public boolean containsKey(String id) {
        return elements.containsKey(id);
    }

    /**
     * Enter a relation into the relations HashMap with the given id as key
     *
     * @param id       key of relation to add to HashMap
     * @param relation relation object to add to HashMap
     * @return
     */
    public Relation put(String id, Relation relation) {
        relations.put(id, relation);
        return relation;
    }

    /**
     * Return the default namespace as a String containing the provn statement
     *
     * @return String containing the default namespace as provn statement
     */
    public String getDefaultNamespace() {
        return defaultNamespace;
    }

    /**
     * Set the default namespace as a String containing the provn statement
     *
     * @param defaultNamespace String containing the default namespace as a string
     */
    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    /**
     * Return the HashMap containing the elements
     *
     * @return HashMap containing element objects
     */
    public HashMap<String, Element> getElements() {
        return this.elements;

    }

    /**
     * Set the HashMap containing the elements. (Probably not needed)
     *
     * @param elements HashMap to set elements HashMap to
     */
    public void setElements(HashMap<String, Element> elements) {
        this.elements = elements;
    }

    /**
     * Deep cloning is required to obtain a complete copy of the graph to ensure that all elements
     *
     * @return Returns a clone of the graph
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        // Create a brand new graph
        ProvGraph newProvGraph = new ProvGraph();
        newProvGraph.setRelations(new HashMap<>());
        newProvGraph.setElements(new HashMap<>());
        newProvGraph.setNamespaces(new Vector<>());
        newProvGraph.setDefaultNamespace("");

        newProvGraph.setDefaultNamespace(getDefaultNamespace());
        for (int i = 0; i < namespaces.size(); i++) {
            newProvGraph.getNamespaces().add(namespaces.get(i));
        }
        Iterator<String> it = relations.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            newProvGraph.getRelations().put(key, (Relation) relations.get(key).clone());
        }
        Iterator<String> it2 = elements.keySet().iterator();
        while (it2.hasNext()) {
            String key = it2.next();
            newProvGraph.getElements().put(key, (Element) elements.get(key).clone());
        }


        return newProvGraph;
    }

    /**
     * Write the graph to a file as provn statements
     *
     * @param g       ProvGraph to write to file
     * @param outfile Name of file to write to as a string
     */
    static public void writeGraphToFile(ProvGraph g, String outfile) {
        Vector<String> namespaces = g.getNamespaces();


        Iterator graphIterator = g.entrySet().iterator();
        try {
            PrintWriter pw = new PrintWriter(new File(outfile));
            pw.println("document\n");
            pw.println(g.getDefaultNamespace() + "\n");
            g.getNamespaces().forEach((value) -> {
                pw.println(value);
            });
            pw.println();
            while (graphIterator.hasNext()) {
                Map.Entry pair = (Map.Entry) graphIterator.next();
                if (pair.getValue() instanceof Entity) {
                    Entity entity = (Entity) pair.getValue();
                    pw.println("entity(" + entity.getID() + (entity.getAttString() == null ? ")" : ",[" + (entity.getAttString()==null?"":entity.getAttString()) + "])"));
                } else if (pair.getValue() instanceof Activity) {
                    Activity activity = (Activity) pair.getValue();
                    pw.println("activity(" + activity.getID() + (activity.getAttString() == null ? ")" : ",[" + (activity.getAttString()==null?"":activity.getAttString()) + "])"));
                } else if (pair.getValue() instanceof Agent) {
                    Agent agent = (Agent) pair.getValue();
                    pw.println("agent(" + agent.getID() + (agent.getAttString() == null ? ")" : ",[" + (agent.getAttString()==null?"":agent.getAttString()) + "])"));
                }
            }
            pw.println();
//            HashMap<String, Relation> relations = g.getRelations();
//            for (Map.Entry<String, Relation> entry : relations.entrySet()) {
//                String key = entry.getKey();
//                Relation value = entry.getValue();
//                pw.println(value.getPROVN());
//            }
            g.getRelations().forEach((k,v) -> {
                pw.println(v.getPROVN());
            });
            pw.println("\nendDocument");
            pw.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the graph to a file as provn statements
     *
     * @param g ProvGraph to write to file
     */
    static public String graphToString(ProvGraph g) {
        Vector<String> namespaces = g.getNamespaces();
        String ret = null;
        Iterator graphIterator = g.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder("document\n");
        stringBuilder.append(g.getDefaultNamespace() + "\n");
        g.getNamespaces().forEach((value) -> {
            stringBuilder.append(value + "\n");
        });
        stringBuilder.append("\n");
        while (graphIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) graphIterator.next();
            if (pair.getValue() instanceof Entity) {
                Entity entity = (Entity) pair.getValue();
                stringBuilder.append("entity(" + entity.getID()
                        + (entity.getAttString() == null ? ")\n" : ",[" + entity.getAttString() + "])\n"));
            } else if (pair.getValue() instanceof Activity) {
                Activity activity = (Activity) pair.getValue();
                stringBuilder.append("activity(" + activity.getID()
                        + (activity.getAttString() == null ? ")\n" : ",[" + activity.getAttString() + "])\n"));
            } else if (pair.getValue() instanceof Agent) {
                Agent agent = (Agent) pair.getValue();
                stringBuilder.append("agent(" + agent.getID()
                        + (agent.getAttString() == null ? ")\n" : ",[" + agent.getAttString() + "])\n"));
            }
        }
        stringBuilder.append("\n");
        HashMap<String, Relation> relations = g.getRelations();
        for (Map.Entry<String, Relation> entry : relations.entrySet()) {
            String key = entry.getKey();
            Relation value = entry.getValue();
            stringBuilder.append(value.getPROVN() + "\n");
        }
        stringBuilder.append("\nendDocument\n");
        ret = stringBuilder.toString();
        return ret;
    }
}
