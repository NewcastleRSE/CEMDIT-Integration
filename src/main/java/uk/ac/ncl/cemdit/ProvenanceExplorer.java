package uk.ac.ncl.cemdit;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.view.MainPanel;
import uk.ac.ncl.cemdit.view.MenuBar;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.io.InputStream;

public class ProvenanceExplorer extends JFrame {
    private Logger logger = Logger.getLogger(this.getClass());
    private MainPanel mainPanel;
    private MenuBar menuBar;
    private InputStream is = null;
    private ComponentPointers componentPointers = ComponentPointers.getInstance();


    private ProvenanceExplorer(String title) {
        super(title);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            Image icon = toolkit.getImage(ClassLoader.getSystemResource("ProvExplorerIcon.png"));

            setIconImage(icon);
        } catch (NullPointerException e) {
            logger.error("Logo.png not found.");
        }
        menuBar = new MenuBar();
        mainPanel = new MainPanel(this);
        setJMenuBar(menuBar);
        frameLayout();
    }

    private void frameLayout() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            getContentPane().add(mainPanel);

            this.pack();
            this.setVisible(true);
            setSize(1024, 768);
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

    }

    static public void main(String[] args) {

        ProvenanceExplorer testLayout = new ProvenanceExplorer("Provenance Explorer");
    }

}


