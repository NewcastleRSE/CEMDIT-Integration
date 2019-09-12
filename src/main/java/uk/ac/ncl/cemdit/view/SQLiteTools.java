package uk.ac.ncl.cemdit.view;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.dao.sqlite.Connector;

import java.sql.*;

public class SQLiteTools {
    final static Logger logger = Logger.getLogger(SQLiteTools.class);

    static public void main(String[] args) {
        /**
         * Tables to create for CEMDIT GUI purposes
         */
        String[] tables = {"lookup", "querytypes", "samplequeries"};

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option i = Option.builder("c").longOpt("create").required().desc("Create database and tables").build();
        options.addOption(i);

        org.apache.commons.cli.CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("c")) {
                createDatabase();
                String[] queries = createTableSQLqueries(tables);
                createNewTables(queries);
//                insertTypes();
                insertQueries();
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
    public static void insertInitRecords() {

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

    public static void insertTypes() {
        String[] types = {"Vehicle Count", "Temperature", "humidity"};
        Connection conn = Connector.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO querytypes (type) VALUES(?)");
            conn.setAutoCommit(false);
            logger.debug("Starting insertion ...");
            for (int i = 0; i < types.length; i++) {
                preparedStatement.setString(1, types[i]);
                System.out.println(types[i]);
                preparedStatement.addBatch();
            }
            int[] result = preparedStatement.executeBatch();
            System.out.println(result.length);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
    }


    public static void insertQueries() {
        String[] types = {"Vehicle Count", "Temperature", "humidity"};
        String querytype = "Vehicle Count";
        String query = "http://localhost:8086/query/?sensor=PER_TRF_CNT_TT2NORTH4&theme=Vehicles&type=Vehicle%20Count&starttime=201901010000&endtime=20190105235959";

        Connection conn = Connector.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO samplequeries (querytype, query) VALUES(?,?)");
            conn.setAutoCommit(false);
            logger.debug("Starting insertion ...");
            for (int i = 0; i < types.length; i++) {
                preparedStatement.setString(1, querytype);
                preparedStatement.setString(2, query);
                System.out.println(types[i]);
                preparedStatement.addBatch();
            }
            int[] result = preparedStatement.executeBatch();
            System.out.println(result.length);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
    }

    public static void createDatabase() {
        Connector.connect();
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
                Connection conn = Connector.connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            for (int i = 0; i < tables.length; i++) {
                stmt.execute(tables[i]);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static public void initTables() {

    }
}
