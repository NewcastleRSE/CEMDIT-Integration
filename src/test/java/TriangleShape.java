// http://www.java2s.com/Tutorials/Java/Graphics_How_to/Shape/Draw_a_triangle.htm
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

class TriangleShape extends Path2D.Double {
    public TriangleShape(Point2D... points) {
        moveTo(points[0].getX(), points[0].getY());
        lineTo(points[1].getX(), points[1].getY());
        lineTo(points[2].getX(), points[2].getY());
        closePath();
    }
}