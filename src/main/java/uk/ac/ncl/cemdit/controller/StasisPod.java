package uk.ac.ncl.cemdit.controller;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.provenancegraph.Element;
import uk.ac.ncl.cemdit.model.provenancegraph.Relation;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class StasisPod {
    private org.apache.log4j.Logger logger = Logger.getLogger(this.getClass());

    private HashMap<String, Element> elementHashMap = new HashMap<>();
    private HashMap<String, Relation> relationHashMap = new HashMap<>();

    public StasisPod() {}

    public boolean isInPod(Element element) {
        return elementHashMap.containsKey(element.getID());
    }

    public boolean isInPod(Relation relation) {
        return relationHashMap.containsKey(relation.getId());
    }

    public boolean isOutreach(Relation relation) {
        // Is outreach if one of its elements is not in the stasispod
        AtomicBoolean inside = new AtomicBoolean(false);
        elementHashMap.forEach((k, v) -> {
            if (!elementHashMap.containsKey(relation.getElement1()) ||
                    !elementHashMap.containsKey(relation.getElement2()))
                inside.set(true);
        });
        return inside.get();
    }

    public Element put(Element element) {
        logger.debug("Element added: " + element.getID());
        return elementHashMap.put(element.getID(), element);
    }

    public Relation put (Relation relation) {
        return relationHashMap.put(relation.getId(), relation);
    }

    public HashMap<String, Element> getElements() {
        return elementHashMap;
    }

    public void setElements(HashMap<String, Element> elementHashMap) {
        this.elementHashMap = elementHashMap;
    }

    public HashMap<String, Relation> getRelations() {
        return relationHashMap;
    }

    public void setRelations(HashMap<String, Relation> relationHashMap) {
        this.relationHashMap = relationHashMap;
    }
}
