package uk.ac.ncl.cemdit.view;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.*;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.model.provenancegraph.Element;
import uk.ac.ncl.cemdit.model.provenancegraph.ProvGraph;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel holds the graphstream viewpanel
 */
public class GraphStreamPanel implements ViewerListener, MouseInputListener, MouseWheelListener, MouseMotionListener {
    private Logger logger = Logger.getLogger(this.getClass());

    private ComponentPointers componentPointers = ComponentPointers.getInstance();
    private ViewPanel viewPanel;
    private Graph graph = new SingleGraph("Graph");
    private Viewer viewer;
    private ViewerPipe viewerPipe;
    private View view;
    private JTextArea statsTextArea = componentPointers.getStatsTextArea();
    private InfoPanel infoPanel;
    private ProvGraph provGraph;
    private int nodeClicked = 0;
    private String firstNodeClicked = "";
    private String secondNodeClicked = "";
    private int zoomr = 1;
    private boolean drag = false;

    public GraphStreamPanel(InfoPanel infoPanel) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        this.infoPanel = infoPanel;
//        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
        viewerPipe = viewer.newViewerPipe();
        viewerPipe.addViewerListener(this);
        viewerPipe.addSink(graph);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        viewPanel = viewer.addDefaultView(false);
        view = null;
        view = viewer.getDefaultView();
        view.addMouseListener(this);
        ((Component) view).addMouseWheelListener(this);
        ((Component) view).addMouseMotionListener(this);
    }

    public ViewPanel getViewPanel() {
        return viewPanel;
    }

    public Graph setGGraph(ProvGraph provGraph, String css, String frameTitle) {
        this.provGraph = provGraph;
        graph.clear();
        graph.setStrict(true);
        provGraph.getElements().forEach((k, v) -> {
            graph.addAttribute("ui.stylesheet", css);
            graph.addAttribute("ui.title", frameTitle);
            if (v.getID().contains(":"))
                graph.addNode(v.getID()).addAttribute("label", v.getID().split(":")[1]);
            else
                graph.addNode(v.getID()).addAttribute("label", v.getID());
            graph.getNode(v.getID()).addAttribute("ui.class", StringsAndStuff.camelCase(v.getType().toString()));
        });
        provGraph.getRelations().forEach((k, v) -> {
            try {
//                logger.debug("Adding edges: " + v.getElement1() + ", " + v.getElement2());
                graph.addEdge(v.getId(), v.getElement1(), v.getElement2(), true);
//                graph.getEdge(v.getId()).addAttribute("id", v.getId());
                graph.getEdge(v.getId()).addAttribute("label", v.getType());
                graph.getEdge(v.getId()).addAttribute("length", 1);
            } catch (EdgeRejectedException e) {
                logger.debug("Edge " + v.getId() + " rejected: " + v.getElement1() + " to " + v.getElement2());
            } catch (ElementNotFoundException e) {
                logger.debug("Cannot create edge " + v.getId() + "[" + v.getElement2()+"->"+v.getElement1() + "]. Node '" + v.getElement2() + "' does not exist.");
            }
        });
        return graph;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public View getView() {
        return view; //viewer.getDefaultView();
    }

    @Override
    public void viewClosed(String s) {
    }

    public void resetSelection() {
        nodeClicked = 0;
        firstNodeClicked = "";
        secondNodeClicked = "";
        statsTextArea.append("\nReset" + "\n\n");
    }

    @Override
    public void buttonPushed(String s) {
//        logger.debug("ButtonPushed");
        if (!drag) {
            nodeClicked++;
            if (nodeClicked == 3) {
                resetSelection();
             } else {
                if (nodeClicked == 1) {
                    firstNodeClicked = s;
                    statsTextArea.append("First node clicked: " + firstNodeClicked + "\n");
                    componentPointers.getButtonPanel().getFirstSelected().setForeground(Color.red);
                    componentPointers.getButtonPanel().getFirstSelected().setText("From: " + firstNodeClicked);
                } else if (nodeClicked == 2) {
                    secondNodeClicked = s;
                    statsTextArea.append("\nSecond node clicked: " + secondNodeClicked + "\n");
                    componentPointers.getButtonPanel().getSecondSelected().setForeground(Color.red);
                    componentPointers.getButtonPanel().getSecondSelected().setText("To: " + secondNodeClicked);
                }

                Node node = graph.getNode(s);
                Element element = provGraph.get(s);
                if (!(infoPanel == null)) {
                    infoPanel.getTfEntityID().setText(element.getID());
                    infoPanel.getTfEntityType().setText(element.getType().toString());
                    infoPanel.getTaAttributes().setText("");
                    if (!(element.getAttString() == null))
                        infoPanel.getTaAttributes().setText(element.getAttString().replace(",", "\n"));
                }
                // calculate
                int id = node.getInDegree();
                int od = node.getOutDegree();
                //statsTextArea.setText("");
                statsTextArea.append("In degree: " + id + "\n");
                statsTextArea.append("Out degree: " + od + "\n");

                Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "result", "length");
                dijkstra.init(graph);
                dijkstra.setSource(graph.getNode(firstNodeClicked));
                dijkstra.compute();

                // Print the lengths of all the shortest paths
                for (Node node1 : graph)
                    statsTextArea.append(String.format("%s->%s:%6.2f%n", dijkstra.getSource(), node1, dijkstra.getPathLength(node1)));
                if (!secondNodeClicked.equals("")) {
                    List<Node> list1 = new ArrayList<Node>();
                    for (Node node2 : dijkstra.getPathNodes(graph.getNode(secondNodeClicked)))
                        list1.add(0, node2);
                    statsTextArea.append("Shortest path: " + dijkstra.getPath(graph.getNode(secondNodeClicked)).size() + "\n");
                    for (Node node3 : dijkstra.getPathNodes(graph.getNode(secondNodeClicked)))
                        node.addAttribute("ui.style", "fill-color: blue;");

                    for (Node node3 : dijkstra.getPathNodes(graph.getNode(secondNodeClicked))) {
                        statsTextArea.append("Node in path: " + node3.getId() + "\n");
                    }

                }
            }
        }

    }


    @Override
    public void buttonReleased(String s) {
        drag = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        viewerPipe.pump();
        //Zoom out
        if (e.getClickCount()==2) {
            zoomr++;
            double factor = Math.pow(1.25, zoomr);
            Camera cam = view.getCamera();
            double zoom = cam.getViewPercent() * factor;
            Point2 pxCenter  = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
            Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
            double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu/factor;
            double x = guClicked.x + (pxCenter.x - e.getX())/newRatioPx2Gu;
            double y = guClicked.y - (pxCenter.y - e.getY())/newRatioPx2Gu;
            cam.setViewCenter(x, y, 0);
            cam.setViewPercent(zoom);
            if (zoomr == 5) zoomr = 1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        viewerPipe.pump();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drag = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /**
         * https://docs.oracle.com/javase/tutorial/uiswing/events/mousemotionlistener.html
         */
        // TODO: Implement panning left and right

        drag = true;

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /**
         * https://stackoverflow.com/questions/44675827/how-to-zoom-into-a-graphstream-view
         */
        e.consume();
        int i = e.getWheelRotation();
        double factor = Math.pow(1.25, i);
        Camera cam = view.getCamera();
        double zoom = cam.getViewPercent() * factor;
        Point2 pxCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
        Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
        double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu / factor;
        double x = guClicked.x + (pxCenter.x - e.getX()) / newRatioPx2Gu;
        double y = guClicked.y - (pxCenter.y - e.getY()) / newRatioPx2Gu;
        cam.setViewCenter(x, y, 0);
        cam.setViewPercent(zoom);
    }
}
