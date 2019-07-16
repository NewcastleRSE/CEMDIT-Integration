package uk.ac.ncl.cemdit.controller;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import uk.ac.ncl.cemdit.model.provenancegraph.Attributes;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;
import uk.ac.ncl.cemdit.view.StringsAndStuff;

public class GraphConversions {

    public static Graph PE2GS(ProvGraph provGraph, String css, String frameTitle) {
        Graph graph = new SingleGraph("Singlegraph");

        // Add elements to graphstream graph


        provGraph.getElements().forEach((k, v) -> {
            graph.addAttribute("ui.stylesheet", css);
            graph.addAttribute("ui.title", frameTitle);
            if (v.getID().contains(":"))
                graph.addNode(v.getID()).addAttribute("label", v.getID().split(":")[1]);
            else
                graph.addNode(v.getID()).addAttribute("label", v.getID());
            graph.getNode(v.getID()).addAttribute("ui.class", StringsAndStuff.camelCase(v.getType().toString()));
        });
        provGraph.getRelations().forEach((k, v) -> {
            graph.addEdge(v.getId(), v.getElement1(), v.getElement2(), true);
            graph.getEdge(v.getId()).addAttribute("label", v.getType());
            graph.getEdge(v.getId()).addAttribute("length", 1);
        });
        return graph;
    }

    public static String attsToString(Attributes attributes) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < attributes.size(); i++) {
            if (i > 0 && i < attributes.size()) {
                sb.append(",");
            }
            sb.append(attributes.getAttribute(i).getAttString());
        }
        return sb.toString();
    }
}
