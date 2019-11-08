package uk.ac.ncl.cemdit.view.integration;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.log4j.Logger;
import org.openrdf.query.algebra.Str;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.ProvConvertItems;
import uk.ac.ncl.cemdit.controller.integration.Utils;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class ProvenancePanel extends JPanel {
    private Logger logger = Logger.getLogger(this.getClass());

    private JSVGScrollPane sp_prov;
    private JSVGCanvas svgCanvas = new JSVGCanvas();
    private JFrame newFrame = new JFrame();
    private boolean loaded = false;

    public ProvenancePanel() {
        setLayout(new FlowLayout());
        IntegrationModel integrationModel = new IntegrationModel();
        File svgFile = new File(integrationModel.getProvNFilename());
        try {
            //svgCanvas.setURI(svgFile.toURI().toURL().toString());
//            AffineTransform at = new AffineTransform();
//            at.scale(0.75, 0.75);
//            svgCanvas.setRenderingTransform(at, true);
//            //integrationModel.getProvenancePanel().add(svgCanvas);
            sp_prov = new JSVGScrollPane(svgCanvas);
            svgCanvas.setPreferredSize(new Dimension(800, 800));
            //add(svgCanvas);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        sp_prov = new JSVGScrollPane(svgCanvas);
//        sp_prov.add(svgCanvas);
//        logger.debug("Viewbox: " + svgCanvas.getViewBoxTransform().getScaleX());

        svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                logger.trace("Document Loading...");
            }

            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                logger.trace("Document Loaded.");
            }
        });

        svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildStarted(GVTTreeBuilderEvent e) {
                logger.trace("Build Started...");
            }

            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                logger.trace("Build Done.");
            }
        });

        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
                logger.trace("Rendering Started...");
            }

            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                logger.trace("");
            }
        });
    }

    /**
     * Retrieve
     */
    public void loadGraph(boolean newFrame) {
        IntegrationModel integrationModel = new IntegrationModel();
        String svgFile = integrationModel.getProvNFilename();
        String tmpFile = "temporary.provn";
        try {
            logger.trace("Retrieving file " + svgFile);
            InputStream in = new URL(svgFile).openStream();
            logger.trace("Saving file as " + Paths.get(ComponentPointers.getProperty("tempdir") + "/" + tmpFile));
            Files.copy(in, Paths.get(ComponentPointers.getProperty("tempdir") + "/" + tmpFile), StandardCopyOption.REPLACE_EXISTING);
            // fix file order
            StringBuilder sb = new StringBuilder();
            File file = new File(ComponentPointers.getProperty("tempdir") + "/" + tmpFile);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine() + "\n");
            }
            sc.close();
            String newContent = Utils.orderFile(sb.toString());
            System.out.println();
            PrintWriter pw = new PrintWriter(file);
            pw.print(newContent);
            pw.close();
            // If svg file then convert to svg
            if (svgFile.endsWith(".provn"))
                ProvConvertItems.convertProvN(ComponentPointers.getProperty("tempdir") + "/" + tmpFile,
                        ComponentPointers.getProperty("tempdir") + "/" + tmpFile.replace(".provn", ".svg"));
            logger.trace("Load " + Paths.get(ComponentPointers.getProperty("tempdir") + "/" + tmpFile) + " to SVGCanvas");
            svgCanvas.setURI(ComponentPointers.getProperty("tempdir") + "/" + tmpFile.replace(".provn", ".svg"));
            integrationModel.setProvNFilename(svgFile);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (newFrame)
            spawnNewFrame();
    }

    private void spawnNewFrame() {
        // Spawn new frame for displaying graph
        if (!loaded) {
            newFrame.add(sp_prov);
            newFrame.pack();
            newFrame.setVisible(true);
            newFrame.setSize(1024, 768);
        } else {
            newFrame.setVisible(true);
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
