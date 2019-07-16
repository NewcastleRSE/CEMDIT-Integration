package uk.ac.ncl.cemdit.model.provenancegraph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {
    Attribute attribute = new Attribute("key","tester");

    @Test
    void getName() {
        assertEquals("key", attribute.getName());
    }

    @Test
    void getValue() {
        assertEquals("tester", attribute.getValue());
    }

    @Test
    void getType() {
        attribute.setType(Attributes.ElementType.STRING);
        assertEquals(Attributes.ElementType.STRING, attribute.getType());
    }

    @Test
    void getAttString() {
        assertEquals("key=\"tester\"", attribute.getAttString());
    }
}