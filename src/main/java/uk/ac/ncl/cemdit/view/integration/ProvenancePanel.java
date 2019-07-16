package uk.ac.ncl.cemdit.view.integration;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.net.MalformedURLException;

public class ProvenancePanel extends JPanel {
    private Logger logger = Logger.getLogger(this.getClass());

    private JSVGScrollPane sp_prov;


    public ProvenancePanel() {
        setLayout(new FlowLayout());
        IntegrationModel integrationModel = new IntegrationModel();
        File svgFile = new File(integrationModel.getProvNFilename());
        JSVGCanvas svgCanvas = new JSVGCanvas();
        try {
            logger.debug("Filename: " + svgFile.toURI().toURL().toString());
            svgCanvas.setURI(svgFile.toURI().toURL().toString());
            AffineTransform at = new AffineTransform();
            at.scale(0.75, 0.75);
            svgCanvas.setRenderingTransform(at, true);
            integrationModel.getProvenancePanel().add(svgCanvas);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
//        sp_prov = new JSVGScrollPane(svgCanvas);
//        sp_prov.add(svgCanvas);
        logger.debug("Viewbox: " + svgCanvas.getViewBoxTransform().getScaleX());
        add(svgCanvas);
    }

}
