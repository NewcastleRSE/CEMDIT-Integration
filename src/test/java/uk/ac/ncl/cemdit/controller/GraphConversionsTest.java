package uk.ac.ncl.cemdit.controller;

import org.graphstream.graph.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ncl.cemdit.model.provenancegraph.Attribute;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphConversionsTest {
    ProvGraph provGraph = new ProvGraph();

    @BeforeEach
    void setUp() {
            provGraph = ParsePROVN.parseFile(new File("src/test/resources/test.provn"));
    }

    @Test
    void PE2GS() {
        String css = null;
        try {
            css = new String(Files.readAllBytes(Paths.get(getClass().getResource("/stylesheet.css").toURI())));
     Graph gsgraph = GraphConversions.PE2GS(provGraph, css, "Testing");
        assertEquals("uo:TFIV2",gsgraph.getNode("uo:TFIV2").getId());
            assertEquals(2, gsgraph.getNode("pre_0:counting").getOutDegree());
            assertEquals(1, gsgraph.getNode("pre_0:counting").getInDegree());
            assertEquals(3, gsgraph.getNode("pre_0:counting").getDegree());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    void attsToString() {
        Attribute attribute0 = provGraph.getElements().get("uo:TFIV2").getAttributes().getAttribute(0);
        Attribute attribute1 = provGraph.getElements().get("uo:TFIV2").getAttributes().getAttribute(1);
        // attribute name (key)
        assertEquals("pre_0:value", attribute0.getName());
        // attribute value
        assertEquals("TF_I_V2_ca3bf30a-2f7d-11e9-b210-d663bd873d93", attribute0.getValue());
        // attribute name (key)
        assertEquals("uo:date", attribute1.getName());
        // attribute value
        assertEquals("2019-03-27", attribute1.getValue());
        // The static method in GraphConversions should generate the same string as the individual
        // element and relations classes
        assertEquals(provGraph.getElements().get("uo:TFIV2").getAttString(),
                GraphConversions.attsToString(provGraph.getElements().get("uo:TFIV2").getAttributes()));
    }
}