package uk.ac.ncl.cemdit.view.graphwidgets;

import uk.ac.ncl.cemdit.model.provenancegraph.Element;
import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;

import java.util.HashMap;

public class GProvGraph {

    ProvGraph provGraph;

    /**
     * A HashMap holding the provenance elements with the key being the unique element id
     */
    private HashMap<String, GElement> gElements = new HashMap<>();

    public GProvGraph(ProvGraph provGraph) {
        this.provGraph = provGraph;
        // create GElements
        HashMap<String, Element> elements = provGraph.getElements();
        elements.forEach((k,v)->{
            if (v.getType()== ElementType.ACTIVITY){

            };
        });
    }

//    public void setGraph(ProvGraph provGraph) {
//        this.provGraph = provGraph;
//    }

    public ProvGraph getProvGraph() {
        return provGraph;
    }

    public GElement putGElement(GElement gElement) {
        return gElements.put(gElement.getElement().getID(),gElement);
    }

    public HashMap<String, GElement> getgElements() {
        return gElements;
    }

}
