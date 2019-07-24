package uk.ac.ncl.cemdit.view;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.integration.Utils;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.view.integration.CEMDITMainPanel;
import uk.ac.ncl.cemdit.model.integration.QueryResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CEMDIT extends JFrame  implements ActionListener {
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * The main panel containing all the other panels
     */
    private CEMDITMainPanel mainPanel ;
    /**
     * Model for data table
     */
    static private IntegrationDataModel integrationDataModel = new IntegrationDataModel( );

    /**
     * Class containing pointers to everything (all components of GUI etc)
     */
    static private IntegrationModel integrationModel = new IntegrationModel();


    CEMDIT() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //Image icon = toolkit.getImage(ClassLoader.getSystemResource("Logo.png"));
        //setIconImage(icon);

        Utils.populateIntegrationModel("", integrationModel, integrationDataModel);


        mainPanel = new CEMDITMainPanel(integrationModel, integrationDataModel);
        mainPanel.setIntegrationDataModel(integrationDataModel);

        getContentPane().add(mainPanel);
        pack();
        setVisible(true);
        setSize(1024, 768);
    }

    static public void main(String[] args) {
        CEMDIT mockup = new CEMDIT();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
     }
}

