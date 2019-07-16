package uk.ac.ncl.cemdit.view.graphwidgets;

import uk.ac.ncl.cemdit.model.provenancegraph.Agent;
import uk.ac.ncl.cemdit.model.provenancegraph.Element;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GAgent implements GElement {

    int xorigin = 0;
    int yorigin = 0;
    int width = 10;
    int height = 5;
    Agent agent;

    public GAgent(Agent agent, int xorigin, int yorigin) {
        super();
        this.xorigin = xorigin;
        this.yorigin = yorigin;
        this.agent = agent;
    }
    @Override
    public int getXOrigin() {
        return xorigin;
    }

    @Override
    public void setXOrigin(int xOrigin) {
        xorigin = xOrigin;
    }

    @Override
    public int getYOrigin() {
        return yorigin;
    }

    @Override
    public void setYOrigin(int yOrigin) {
        yorigin = yOrigin;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setElement(Element element) {
        agent = (Agent) element;
    }

    @Override
    public Element getElement() { return agent; }

    @Override
    public Graphics drawMe(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.orange);
        //g.draw((activities * 5) + (xoffset + (xoffset * activities)) ,25,5,5);
        g2.draw(new Rectangle2D.Double(xorigin, yorigin, width, height));

        return g;
    }

    @Override
    public boolean pointInsideMe(int x, int y) {
        return false;
    }

}
