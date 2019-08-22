package uk.ac.ncl.cemdit.controller;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class MainUtils {
    static private Logger logger = Logger.getLogger(MainUtils.class);


    static public void setProperty(String property, String value) {
        Properties properties = new Properties();
        logger.debug("Setting property: " + property + " = " + value);
        properties.setProperty(property, value);
        File f = new File("server.properties");
        try {
            OutputStream out = new FileOutputStream(f);
            logger.debug("Storing properties");
            properties.store(out, "This is an optional header comment string");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
