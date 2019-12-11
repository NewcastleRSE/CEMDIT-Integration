package uk.ac.ncl.cemdit.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.dao.sqlite.Connector;
import uk.ac.ncl.cemdit.model.integration.lookupDB.LookupDB;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTools {
    final static Logger logger = Logger.getLogger(SQLiteTools.class);
    static String filename = "data/lookup.json";
    static String connectionString = "jdbc:sqlite:sqlite/sqlite.db";
    static String[] types = {"PM2.5"};

    static public void main(String[] args) {
        /**
         * Tables to create for CEMDIT GUI purposes
         */
        String[] tables = {"lookup", "querytypes", "samplequeries"};

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option d = Option.builder("d").longOpt("database").desc("Create database").build();
        options.addOption(d);
        Option t = Option.builder("t").longOpt("tables").desc("Create tables").build();
        options.addOption(t);
        Option i1 = Option.builder("i1").longOpt("initialise 1").desc("Initialise table 1").build();
        options.addOption(i1);
        Option i2 = Option.builder("i2").longOpt("initialise 2").desc("Initialise table 2").build();
        options.addOption(i2);
        Option i3 = Option.builder("i3").longOpt("initialise 3").desc("Initialise table 3").build();
        options.addOption(i3);

        org.apache.commons.cli.CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("d")) {
                logger.trace("Creating database ...");
                createDatabase();
            }
            if (cmd.hasOption("t")) {
                logger.trace("Creating tables ...");
                String[] queries = createTableSQLqueries(tables);
                createNewTables(queries);
            }
            if (cmd.hasOption("i1")) {
                logger.trace("insert types ...");
                insertTypes();
            }
            if (cmd.hasOption("i2")) {
                logger.trace("insert queries ...");
                insertQueries();
            }
            if (cmd.hasOption("i3")) {

                logger.trace("insert lookup items ...");
                insertLookups();
            }

        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar VCFCombine.jar\n" + "Version: 1\n"
                    + "Program for combining a VCF and its Annovar annotation (.avinput) file.", options);
        }
    }

    /**
     * Insert some records into the tables just to get things going
     */
    public static void insertLookups() {
        LookupDB lookupDB = lookupItems();
        try {
            Connection conn = Connector.connect(connectionString);
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO lookup (query, type, uri, description) VALUES(?,?,?,?)");
            lookupDB.getCollection().forEach((item) -> {
                try {
                    preparedStatement.setString(1, item.getQuery());
                    preparedStatement.setString(2, item.getType());
                    preparedStatement.setString(3, item.getUri());
                    preparedStatement.setString(4, item.getDescription());
                    preparedStatement.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });
            int[] result = preparedStatement.executeBatch();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String[] createTableSQLqueries(String[] tables) {
        String[] queries = new String[tables.length];
        queries[0] = "CREATE TABLE IF NOT EXISTS lookup (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    query text NOT NULL,\n"
                + "    type text NOT NULL,\n"
                + "    uri text NOT NULL,\n"
                + "    description text NOT NULL\n"
                + ");";
        queries[1] = "CREATE TABLE IF NOT EXISTS querytypes (\n"
                + "    type text PRIMARY KEY\n"
                + ");";
        queries[2] = "CREATE TABLE IF NOT EXISTS samplequeries (\n"
                + "    querytype text NOT NULL,\n"
                + "    query text NOT NULL,\n"
                + "    FOREIGN KEY(querytype) REFERENCES lookup(type)\n"
                + ");";
        return queries;
    }

    public static LookupDB lookupItems() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson;
        gson = builder.setPrettyPrinting().create(); // Add pretty printing for easy reading
        try {
            LookupDB lookupDB = gson.fromJson(new FileReader(filename), LookupDB.class);
            return lookupDB;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertTypes() {
        Connection conn = null;
        try {
            conn = Connector.connect(connectionString);
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO querytypes (type) VALUES(?)");
            conn.setAutoCommit(false);
            logger.debug("Starting type insertion ...");
            for (int i = 0; i < types.length; i++) {
                preparedStatement.setString(1, types[i]);
                logger.trace("Type: " + types[i]);
                preparedStatement.addBatch();
            }
            int[] result = preparedStatement.executeBatch();
            conn.commit();
            logger.trace("Close database");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            logger.trace("Close database");
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {

            }
        }
    }


    public static void insertQueries() {
        String querytype = "Vehicle Count";
        String query = "http://localhost:8086/query/?sensor=PER_TRF_CNT_TT2NORTH4&theme=Vehicles&type=Vehicle%20Count&starttime=201901010000&endtime=20190105235959";
        Connection conn = null;
        try {
            conn = Connector.connect(connectionString);
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO samplequeries (querytype, query) VALUES(?,?)");
            conn.setAutoCommit(false);
            logger.trace("Starting insertion ...");
            for (int i = 0; i < types.length; i++) {
                preparedStatement.setString(1, querytype);
                preparedStatement.setString(2, query);
                logger.trace(types[i]);
                preparedStatement.addBatch();
            }
            int[] result = preparedStatement.executeBatch();
            System.out.println(result.length);
            conn.commit();
            logger.trace("Close database.");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {

            }
        }
    }

    public static void createDatabase() {
//        Connector.connect(connectionString);
//        String url = "jdbc:sqlite:C:/sqlite/db/provenance.db";
//
//        try (Connection conn = DriverManager.getConnection(url)) {
//            if (conn != null) {
//                System.out.println("Creating database ...");
//                DatabaseMetaData meta = conn.getMetaData();
//                System.out.println("provenance.db database has been created.");
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

    }

    /**
     * Create a new tables in the database
     */
    public static void createNewTables(String[] tables) {

        // SQL statement for creating a new table

        try (
                Connection conn = Connector.connect(connectionString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            for (int i = 0; i < tables.length; i++) {
                stmt.execute(tables[i]);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static public void initTables() {

    }
}
