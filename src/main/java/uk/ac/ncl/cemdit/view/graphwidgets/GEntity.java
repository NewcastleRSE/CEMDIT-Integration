package uk.ac.ncl.cemdit.view.graphwidgets;

import uk.ac.ncl.cemdit.model.provenancegraph.Element;
import uk.ac.ncl.cemdit.model.provenancegraph.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GEntity extends JPanel implements GElement{

    int xorigin = 0;
    int yorigin = 0;
    int width = 10;
    int height = 5;
    Entity entity;

    public GEntity(Entity entity, int xorigin, int yorigin) {
        super();
        this.xorigin = xorigin;
        this.yorigin = yorigin;
        this.entity = entity;
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
        entity = (Entity) element;
    }

    @Override
    public Element getElement() { return entity; }

    @Override
    public Graphics drawMe(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.yellow);
        //g.draw((activities * 5) + (xoffset + (xoffset * activities)) ,25,5,5);
        g2.draw(new RoundRectangle2D.Double(xorigin, yorigin, width, height, 10, 10));

        return g;
    }

    @Override
    public boolean pointInsideMe(int x, int y) {
        return false;
    }

}
