package uk.ac.ncl.cemdit.view;
//http://localhost:8086/query/?theme=Vehicles&type=Vehicle count&sensor=PER_TRF_CNT_SL_A690D1&starttime=20190101000000&endtime=2019013123595959
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.view.integration.CEMDITMainPanel;

import javax.swing.*;
import java.awt.*;

public class CEMDIT extends JFrame  {
    private Logger logger = Logger.getLogger(this.getClass());
    ComponentPointers cp = ComponentPointers.getInstance();

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
        String logo = "CEMDITLogo3.png";
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            Image icon = toolkit.getImage(ClassLoader.getSystemResource(logo));

            setIconImage(icon);
        } catch (NullPointerException e) {
            logger.error(logo + " not found.");
        }

//        Utils.populateIntegrationModel("sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)",integrationModel,integrationDataModel);
        mainPanel = new CEMDITMainPanel();

        getContentPane().add(mainPanel);
        pack();
        setVisible(true);
        //setSize(1024, 768);
        setSize(getWidth(), getHeight());
    }

    static public void main(String[] args) {
        CEMDIT mockup = new CEMDIT();
    }

}

