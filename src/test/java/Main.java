import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/*from w ww .  j a  va  2s .  com*/

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new TestPane());
        frame.pack();
        frame.setVisible(true);
    }
}
class TestPane extends JPanel {
    TriangleShape triangleShape;
    Polygon poly;
    public TestPane() {
        triangleShape = new TriangleShape(new Point2D.Double(50, 0),
                new Point2D.Double(100, 100), new Point2D.Double(0, 100));
        poly = new Polygon(new int[] { 50, 100, 0 }, new int[] { 0, 100, 100 }, 3);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.RED);
        g2d.fill(poly);

        g2d.setColor(Color.GREEN);
        g2d.translate(50, 100);
        g2d.fill(triangleShape);
        g2d.dispose();
    }
}
