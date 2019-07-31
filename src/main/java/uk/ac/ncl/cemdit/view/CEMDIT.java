package uk.ac.ncl.cemdit.view;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.view.integration.CEMDITMainPanel;

import javax.swing.*;
import java.awt.*;

public class CEMDIT extends JFrame  {
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
        try {
            Image icon = toolkit.getImage(ClassLoader.getSystemResource("Logo.png"));

            setIconImage(icon);
        } catch (NullPointerException e) {
            logger.error("Logo.png not found.");
        }

//        Utils.populateIntegrationModel("sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)",integrationModel,integrationDataModel);
        mainPanel = new CEMDITMainPanel();

        getContentPane().add(mainPanel);
        pack();
        setVisible(true);
        setSize(1024, 768);
    }

    static public void main(String[] args) {
        CEMDIT mockup = new CEMDIT();
    }

}

