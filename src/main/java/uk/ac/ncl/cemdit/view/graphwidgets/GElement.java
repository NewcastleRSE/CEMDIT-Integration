package uk.ac.ncl.cemdit.view.graphwidgets;

import uk.ac.ncl.cemdit.model.provenancegraph.Element;

import java.awt.*;

public interface GElement {

    int getXOrigin();

    void setXOrigin(int xOrigin);

    int getYOrigin();

    void setYOrigin(int yOrigin);

    int getHeight();

    void setHeight(int height);

    int getWidth();

    void setWidth(int width);

    void setElement(Element element);

    Element getElement();

    Graphics drawMe(Graphics g);

    boolean pointInsideMe(int x, int y);

}
