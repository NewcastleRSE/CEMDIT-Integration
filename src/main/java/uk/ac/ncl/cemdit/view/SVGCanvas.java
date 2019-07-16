package uk.ac.ncl.cemdit.view;


import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;

public class SVGCanvas extends JSVGCanvas implements SVGDocumentLoaderListener, GVTTreeRendererListener, GVTTreeBuilderListener {
    private Logger logger = Logger.getLogger(this.getClass());

    ComponentPointers componentPointers = ComponentPointers.getInstance();

    public SVGCanvas() {

    }

    @Override
    public void documentLoadingStarted(SVGDocumentLoaderEvent svgDocumentLoaderEvent) {
        System.out.println("Document loading ...");
    }

    @Override
    public void documentLoadingCompleted(SVGDocumentLoaderEvent svgDocumentLoaderEvent) {
        System.out.println("Document loaded.");
    }

    @Override
    public void documentLoadingCancelled(SVGDocumentLoaderEvent svgDocumentLoaderEvent) {
        System.out.println("Document loading cancelled.");
    }

    @Override
    public void documentLoadingFailed(SVGDocumentLoaderEvent svgDocumentLoaderEvent) {
        System.out.println("Document loading failed.");
    }

    @Override
    public void gvtRenderingPrepare(GVTTreeRendererEvent gvtTreeRendererEvent) {
        System.out.println("Rendering started ...");
    }

    @Override
    public void gvtRenderingStarted(GVTTreeRendererEvent gvtTreeRendererEvent) {

    }

    @Override
    public void gvtRenderingCompleted(GVTTreeRendererEvent gvtTreeRendererEvent) {
        System.out.println("Rendering completed.");
    }

    @Override
    public void gvtRenderingCancelled(GVTTreeRendererEvent gvtTreeRendererEvent) {

    }

    @Override
    public void gvtRenderingFailed(GVTTreeRendererEvent gvtTreeRendererEvent) {

    }

    @Override
    public void gvtBuildStarted(GVTTreeBuilderEvent gvtTreeBuilderEvent) {

    }

    @Override
    public void gvtBuildCompleted(GVTTreeBuilderEvent gvtTreeBuilderEvent) {
        componentPointers.getFrame().pack();
    }

    @Override
    public void gvtBuildCancelled(GVTTreeBuilderEvent gvtTreeBuilderEvent) {

    }

    @Override
    public void gvtBuildFailed(GVTTreeBuilderEvent gvtTreeBuilderEvent) {

    }


}
