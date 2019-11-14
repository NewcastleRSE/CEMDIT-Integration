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

    /**
     * Return a pointer to the instance of this singleon class
     * @return instantiation of this class
     */
    static public Connector getInstance() {
        if (connector == null) {
            connector = new Connector();
        }
        return connector;
    }

    /**
     * Connect to the database defined in the properties file
     * @return The connection
     */
//    static public Connection connect() {
//        String url = "jdbc:sqlite:" + componentPointers.getProperty("sqlitedb");
//        logger.trace("Connect to database: " + url);
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(url);
//        } catch (SQLException e) {
//            logger.error(e.getMessage());
//        }
//        return conn;
//    }

    /**
     * Connect to the database specified in the connection string
     * @param connectionstring The database to connect to
     * @return The connection
     * "jdbc:sqlite:" +
     */
    static public Connection connect(String connectionstring) {
        String url = connectionstring;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return conn;
    }

    /**
     * Execute the query specified in the parameter and return the resultset
     * @param query SQL query
     * @return resultset of the query
     */
    static public ResultSet readRecord(String query, String connectionString) {

        Connection conn = connect(connectionString);
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

    /**
     *
     * @return
     */
    static public String[] provenanceTemplates(String connectionString) {
        String[] provtemplates = null;
        List<String> provtemp = new ArrayList<>();
        String query = "SELECT type FROM querytypes";
        Connection conn = connect(connectionString);
        provtemp.add("");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // loop through the result set
            while (rs.next()) {
                provtemp.add(rs.getString("type"));
            }
            conn.close();
        } catch (SQLException e) {
            logger.error("Error code: " + e.getErrorCode());
            e.printStackTrace();
        }
        provtemplates = provtemp.stream().toArray(String[]::new);
        return provtemplates;
    }

    /**
     * Retrieve the entry from the lookup table which contains the URI to the provenance template
     *
     * @param type the type of the provenance template e.g. Vehicle Count
     * @return The URI of the provenance template
     */
    static public String retrieveTemplateFromProvStore(String type, String connectionString) {
        String provtemplate = null;
        List<String> provtemp = new ArrayList<>();
        String query = "SELECT uri FROM lookup WHERE query='" + type + "' AND type='ProvTemplate'";
        Connection conn = connect(connectionString);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // loop through the result set
            while (rs.next()) {
                provtemp.add(rs.getString("uri"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        provtemplate = provtemp.get(0);
        return provtemplate;
    }

    static public String getSensorReadingsHeadings(String connectionstring) {
        String sql = "PRAGMA table_info(SensorReadings)";
        Connection conn = connect(connectionstring);
        StringBuilder sb = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(rs.getString("name"));
                } else {
                    sb.append(",");
                    sb.append(rs.getString("name"));
                }
            }
            conn.close();
        } catch (SQLException e) {
            logger.error("Error code: " + e.getErrorCode());
            logger.error(e.getMessage());
            logger.error(connectionstring);
        }
        return sb.toString();
    }

    static public ArrayList<ArrayList<Object>> readSensorData(String connectionstring, String sqlQuery) {
        ArrayList<ArrayList<Object>> returnValues = new ArrayList<>();
        Connection conn = connect(connectionstring);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<Object> columns = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columns.add(rsmd.getColumnName(i));
            }
            returnValues.add(columns);
            // loop through the result set
            while (rs.next()) {
                ArrayList<Object> reading = new ArrayList<>();
                reading.add(rs.getObject("sensorName"));
                reading.add(rs.getObject("themeName"));
                reading.add(rs.getObject("typeName"));
                reading.add(rs.getObject("suspect"));
                reading.add(rs.getObject("value"));
                reading.add(rs.getObject("units"));
                reading.add(rs.getObject("timestamp"));
                returnValues.add(reading);
            }
            conn.close();
        } catch (SQLException e) {
            logger.error("Error code: " + e.getErrorCode());
            logger.error(e.getMessage());
            logger.error(connectionstring);
        }
        return returnValues;
   }

    static public ArrayList<ArrayList<Object>> readSensorData(String connectionstring, String sensorName, String typeName, long startdate, long enddate) {
        ArrayList<ArrayList<Object>> returnValues = new ArrayList<>();
        String query = "SELECT sensorName, themeName, typeName, suspect, value, units, timestamp " +
                "FROM SensorReadings where sensorname='" + sensorName
                + "' AND typeName='" + typeName
                + "' AND timestamp>=" + startdate
                + " AND timestamp<=" + enddate;
        Connection conn = connect(connectionstring);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // loop through the result set
            while (rs.next()) {
                ArrayList<Object> reading = new ArrayList<>();
                reading.add(rs.getObject("sensorName"));
                reading.add(rs.getObject("themeName"));
                reading.add(rs.getObject("typeName"));
                reading.add(rs.getObject("suspect"));
                reading.add(rs.getObject("value"));
                reading.add(rs.getObject("units"));
                reading.add(rs.getObject("timestamp"));
                returnValues.add(reading);
            }
            conn.close();
        } catch (SQLException e) {
            logger.error("Error code: " + e.getErrorCode());
            logger.error(e.getMessage());
            logger.error(connectionstring);
        }
        return returnValues;
    }

}
