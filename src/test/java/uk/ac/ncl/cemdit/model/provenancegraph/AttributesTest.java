package uk.ac.ncl.cemdit.model.provenancegraph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributesTest {
    Attributes attributes = new Attributes(" ex:name = \"A name\",var:value= \"12345\"");

    @Test
    void getAttribute() {
        assertEquals("A name", attributes.getAttribute(0).getValue());
        assertEquals("ex:name", attributes.getAttribute(0).getName());
        assertEquals("12345", attributes.getAttribute(1).getValue());
        assertEquals("var:value", attributes.getAttribute(1).getName());
        assertEquals("ex:name=\"A name\",var:value=\"12345\"", attributes.toString());
    }
}