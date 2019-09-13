// https://www.tutorialspoint.com/sqlite/sqlite_java.htm
package uk.ac.ncl.cemdit.dao.sqlite;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD handler
 * Singleton used to get a connection to the database. Once instantiated the connection should exist so
 * that multiple connections do not have to be made
 */
public class Connector {
    static private ComponentPointers componentPointers = ComponentPointers.getInstance();
    static private Logger logger = Logger.getLogger(Connector.class);
    private static Connector connector = null;

    private Connector() {
        // Prevent instantiation
    }

    static public Connector getInstance() {
        if (connector == null) {
            connector = new Connector();
        }
        return connector;
    }

    static public Connection connect() {
        String url = "jdbc:sqlite:" + componentPointers.getProperty("sqlitedb");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return conn;
    }

    static public ResultSet readRecord(String query) {

        Connection conn = connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            //preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String[] provenanceTemplates() {
        String[] provtemplates = null;
        List<String> provtemp = new ArrayList<>();
        String query = "SELECT type FROM querytypes";
        Connection conn = connect();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // loop through the result set
            while (rs.next()) {
                provtemp.add(rs.getString("type"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        provtemplates = provtemp.stream().toArray(String[]::new);
        return provtemplates;
    }


}
