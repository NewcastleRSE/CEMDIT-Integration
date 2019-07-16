package uk.ac.ncl.cemdit.view.graphwidgets;

import uk.ac.ncl.cemdit.model.provenancegraph.Activity;
import uk.ac.ncl.cemdit.model.provenancegraph.Element;
import uk.ac.ncl.cemdit.model.provenancegraph.Enumerations.ElementType;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GActivity extends Activity implements GElement {

    int xorigin = 0;
    int yorigin = 0;
    int width = 10;
    int height = 5;
    Activity activity;

    public GActivity(Activity activity, int xorigin, int yorigin) {
        super();
        this.xorigin = xorigin;
        this.yorigin = yorigin;
        this.activity = activity;
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
        activity = (Activity) element;
    }

    public Element getElement() {return activity; }

    @Override
    public ElementType getType() {
        return null;
    }

    @Override
    public Graphics drawMe(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.blue);
        //g.draw((activities * 5) + (xoffset + (xoffset * activities)) ,25,5,5);
        g2.draw(new Ellipse2D.Double(xorigin ,yorigin, width, height));

        return g;
    }

    @Override
    public boolean pointInsideMe(int x, int y) {
        boolean ret = false;
        if ((x >= xorigin && x <= (xorigin + width)) && (y >= yorigin && y<= (yorigin + height))) {
            return true;
        } else
        return ret;
    }

}