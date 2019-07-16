package uk.ac.ncl.cemdit.model.provenancegraph;

import java.util.ArrayList;

/**
 * An ArrayList containing all the attributes for an element or a relation
 */
public class Attributes {

    enum ElementType {
        STRING,
        INT,
        DATE
    }
    ArrayList<Attribute> attributes = new ArrayList<>();

    public Attributes(String line) {
        String[] tokens = line.split(",");
        if (!line.equals(""))
        for (int i = 0; i < tokens.length; i++) {
            String name = tokens[i].split("=")[0].replace("\"","").trim();
            String value = tokens[i].split("=")[1].trim();

            attributes.add(new Attribute(name, value));
        }
    }

    public Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    public int size() {
        return attributes.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attributes.size(); i++) {
            if (i > 0 && i < attributes.size())
                sb.append(",");
            sb.append(attributes.get(i).getAttString());
        }
        return sb.toString();
    }
}
