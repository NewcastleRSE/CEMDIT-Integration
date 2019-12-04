package uk.ac.ncl.cemdit.controller.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.controller.Exceptions.InvalidDocumentFormatException;
import uk.ac.ncl.cemdit.dao.sqlite.Connector;
import uk.ac.ncl.cemdit.model.InteropParameters;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.model.integration.QueryResults;
import uk.ac.ncl.cemdit.model.integration.lookupDB.LookupDB;
import uk.ac.ncl.cemdit.view.integration.QueryType;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static uk.ac.ncl.cemdit.dao.sqlite.Connector.getSensorReadingsHeadings;

public class Utils {

    static private Logger logger = Logger.getLogger(Utils.class);


    static public void bindDataToTemplate(InteropParameters interopParameters) {
        logger.trace(interopParameters.getInfile());
        logger.trace(interopParameters.getOutfile());
        logger.trace(interopParameters.getBindings());
        logger.trace(interopParameters.toString());
        InteropFramework interop = new InteropFramework(interopParameters.getVerbose(),
                interopParameters.getDebug(),
                interopParameters.getLogfile(),
                interopParameters.getInfile(),
                interopParameters.getInformat(),
                interopParameters.getOutfile(),
                interopParameters.getOutformat(),
                interopParameters.getNamespaces(),
                interopParameters.getTitle(),
                interopParameters.getLayout(),
                interopParameters.getBindings(),
                interopParameters.getBindingformat(),
                interopParameters.getBindingsVersion(),
                interopParameters.isAddOrderp(),
                interopParameters.isAllexpanded(),
                interopParameters.getTemplate(),
                interopParameters.getBpackage(),
                interopParameters.getLocation(),
                interopParameters.getGenerator(),
                interopParameters.getIndex(),
                interopParameters.getMerge(),
                interopParameters.getFlatten(),
                interopParameters.getCompare(),
                interopParameters.getCompareOut(),
                org.openprovenance.prov.xml.ProvFactory.getFactory());
        try {
            interop.run();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error(e);

        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

    }

    /**
     * The provn file seems to get disordered when save to the provnstore. This methods saves the file with entities,
     * agents and activities being declared before relationships.
     * It does not yet handle more than one bundle and prefixes declared in a bundle.
     * @param fileContents The contents of the provn file as a string
     * @return The contents of the provn file in ordered fashion.
     */
    static public String orderFile(String fileContents) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> elements = new ArrayList<>();
        ArrayList<String> relations = new ArrayList<>();
        ArrayList<String> prefixes = new ArrayList<>();
        ArrayList<String> bundles = new ArrayList<>();
        String defaultLine = "";
        String[] lines = fileContents.split("\n");

        // Add bit to get rid of empty lines.

        // Read first line
        if (lines[0].toLowerCase().equals("document")) {
            sb.append(lines[0]);
            sb.append("\n");
        } else {
            try {
                throw new InvalidDocumentFormatException("Invalid first line");
            } catch (InvalidDocumentFormatException e) {
                e.printStackTrace();
            }
        }

        // Read rest of the lines
        for (int l = 1; l < lines.length - 1; l++) {
            if (lines[l].trim().equals("") || lines[l] == null) {
                //logger.trace("Omit empty line");
            }
            if (lines[l].trim().toLowerCase().startsWith("entity") ||
                    lines[l].trim().toLowerCase().startsWith("agent") ||
                    lines[l].trim().toLowerCase().startsWith("activity")) {
                elements.add(lines[l] + "\n");
            } else if (lines[l].trim().toLowerCase().startsWith("default")) {
                defaultLine = lines[l] + "\n";
            } else if (lines[l].trim().toLowerCase().startsWith("prefix")) {
                prefixes.add(lines[l] + "\n");
            } else if (lines[l].trim().toLowerCase().startsWith("bundle") || lines[l].trim().toLowerCase().startsWith("endbundle")) {
                bundles.add(lines[l] + "\n");
            } else {
                relations.add(lines[l] + "\n");
            }
        }

        // Read last line
        if (lines[lines.length - 1].trim().toLowerCase().startsWith("enddocument")) {

        } else {
            try {
                throw new InvalidDocumentFormatException("Invalid last line");
            } catch (InvalidDocumentFormatException e) {
                e.printStackTrace();
            }

        }

        sb.append(defaultLine);
        for (int i = 0; i < prefixes.size(); i++) {
            sb.append(prefixes.get(i));
        }
        if (bundles.size() > 0) {
            sb.append(bundles.get(0));
        }
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i));
        }
        for (int i = 0; i < relations.size(); i++) {
            sb.append(relations.get(i));
        }
        if (bundles.size() > 1) {
            sb.append(bundles.get(1));
        }
        sb.append(lines[lines.length - 1]);
        return sb.toString();
    }

    /**
     * Populating the integration model
     * @param query
     * @param integrationModel
     * @param integrationDataModel
     * @param queryType
     * @param container
     */
    static public void populateIntegrationModel(String query, IntegrationModel integrationModel, IntegrationDataModel integrationDataModel, QueryType queryType, Container container) {
        ArrayList<String> data = new ArrayList<>();
        ArrayList<ArrayList<Object>> data1 = new ArrayList<>();
        ArrayList<Object> row = new ArrayList<>();
        ArrayList<Object> row1 = new ArrayList<>();
        String[] columnNames;
        Class[] columnClasses;
        ArrayList<QueryResults> queryResults = new ArrayList<>();

        switch (queryType) {
            case RDF:

                // read data for other results
                data.add("1. sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)");
                data.add("2. sensor(sensor_type(traffic_flow), location_WKT, timestamp, units, measure)");
                data.add("3. sensor(sensor_type(traffic_flow), location_WKT, timestamp, units, measure)");

                integrationModel.setTopRankedQuery("sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)");

                Date date = new Date();
                long t = 1546301013;
                date.setTime((long) t * 1000);
                row.add(0, "Vehicle");
                row.add(1, "VEHICLE_COUNT_01");
                row.add(2, "43.8499984741");
                row.add(3, "-1.6118754505");
                row.add(4, date);
                row.add(5, "Vehicles");
                row.add(6, "11");
                data1.add(0, row);

                row1.add(0, "Vehicle");
                row1.add(1, "VEHICLE_COUNT_01");
                row1.add(2, "43.8488884741");
                row1.add(3, "-1.6118754505");
                row1.add(4, date);
                row1.add(5, "Vehicles");
                row1.add(6, "6");
                data1.add(1, row1);

                String[] tmp = {"Theme", "Sensor Name", "Sensor centroid latitud", "Sensor centroid longitude", "Timestamp", "Units", "Count"};
                columnNames = tmp;
                Class[] tmp2 = {String.class, String.class, Float.class, Float.class, Date.class, String.class, Integer.class};
                columnClasses = tmp2;
                integrationDataModel.setColumnNames(columnNames);
                integrationDataModel.setData(data1);
                integrationDataModel.setClasses(columnClasses);


                System.out.println("Set data.");


                integrationModel.setOtherResponses(data);
                integrationModel.setOriginalQuery("sensor(type(traffic), id, time(154630), measure, location(latitude,longitude), units)");
                String[] sourceLabel = {"Sensor", "traffic", "type", "id", "location", "latitude", "longitute", "measure", "time", "units", "units", "units", "units", "units"};
                String[] operators = {"=", ">", "=", "=", "X", ">", ">", ">", "=", "=", "=", "=", "=", "="};
                String[] targetLabel = {"Sensor", "Vehicles", "theme", "sensor_names", "", "sensor_centroid_latitude", "sensor_centroid_longitude", "count", "timestamp", "untis", "untis", "untis", "untis", "untis"};
                integrationModel.setSimilarityScore(0.7);
                queryResults.clear();
                for (int i = 0; i < sourceLabel.length; i++) {
                    QueryResults queryResult = new QueryResults(sourceLabel[i], operators[i], targetLabel[i]);
                    queryResults.add(queryResult);
                }
                integrationModel.setQueryResults(queryResults);
                integrationModel.setProvNFilename("/home/campus.ncl.ac.uk/njss3/Dropbox/CEM-DIT/CHAIn/Mockups/count_prov.svg");
                break;
            case REST:
                //Do REST query
                try {
                    // Establish connection to TerminalSensor (Urban Observatory Proxy)
                    queryResults.clear();
                    URL url = new URL(query);
                    HttpURLConnection con = null;
                    con = (HttpURLConnection) url.openConnection();
                    // Retrieve data using query entered in TextField
                    ArrayList<String> rawData = readStream2Array(con.getInputStream());
                    data.add(query);
                    // Since it is REST there is only one query
                    integrationModel.setTopRankedQuery(query);
                    // The first line of the retrieved data contains the headers
                    String headers = rawData.get(0);
                    integrationDataModel.setColumnNames(headers.split(","));
                    for (int i = 1; i < rawData.size(); i++) {
                        String line = rawData.get(i);
                        if (!line.equals("")) {
                            String[] info = line.split(",");
                            ArrayList<Object> al_info = new ArrayList();
                            for (int j = 0; j < info.length; j++) {
                                al_info.add(info[j]);
                            }
                            data1.add(al_info);
                        }
                    }
                    integrationDataModel.setData(data1);
                    // It's a REST query it will only have one response. Use that query as
                    // first entry in Other Responses JList
                    ArrayList<String> onlyOneResponse = new ArrayList<>();
                    onlyOneResponse.add(query);
                    integrationModel.setOtherResponses(onlyOneResponse);
                    // Query has to be exact so similarity score will be 100%
                    integrationModel.setSimilarityScore(0.9);
                    // Since it is REST the source and the target will be the same and the operator will be = for all
                    sourceLabel = headers.split(",");
                    targetLabel = headers.split(",");
                    //ArrayList<QueryResults> queryResults1 = new ArrayList<>();
                    for (int i = 0; i < sourceLabel.length; i++) {
                        QueryResults queryResult = new QueryResults(sourceLabel[i], "=", targetLabel[i]);
                        queryResults.add(queryResult);
                    }
                    // add the retrieved data to the model
                    integrationModel.setQueryResults(queryResults);
                    // Now read the lookup table in the database to see where the provenance graph template is stored
                    //integrationModel.setProvNFilename();

                    //integrationDataModel.setClasses(columnClasses);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    if (e.getMessage().equals("Connection refused: connect")) {
                        JOptionPane.showMessageDialog(container, "Connection refused. Please make sure the REST service \n" +
                                "is running and try running the query again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                //Populate

                break;
            case SQL:


                String connectionString = ComponentPointers.getProperty("datadb"); //"jdbc:sqlite:../UrbanObservatoryBasics/UrbanObservatory.db";
                // Set headings/column names
                //String headers = getSensorReadingsHeadings(connectionString);
                //integrationDataModel.setColumnNames(headers.split(","));
                queryResults.clear();
                // get sensor data from database
                ArrayList<ArrayList<Object>> results = Connector.readSensorData(connectionString,query);
                // the first row should be the column names
                integrationDataModel.setColumnNames(results.get(0));
                results.remove(0);
                results.forEach((line) -> {
                    data1.add(line);
                });
                integrationDataModel.setData(data1);
                break;
            default:
                logger.info("No query specified");
                break;
        }
        integrationDataModel.fireTableDataChanged();
        integrationDataModel.fireTableStructureChanged();
    }

    static public String arrayList2CSVString(ArrayList<Object> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            if (i != 0) sb.append(",");
            sb.append((String)arrayList.get(i));
        }

        return sb.toString();
    }

    /**
     * Determines where the lookup table is stored, i.e. in a JSON file, an SQLITE database or whatever
     *
     * @param lookupType       select from the LookupType Enumeration - JSON, SQLITE, MONGODB
     * @param integrationModel the model of the GUI
     * @param querytype        the type of the query which is the key in the database to find the location of the template in the provstore, eg. Vehicle Count, Radar
     */
    static public void lookupProvenance(LookupType lookupType, IntegrationModel integrationModel, String querytype) {
        switch (lookupType) {
            // The lookup database telling us where to find the provenance is in a json file
            case JSON:
                String dblocation = ComponentPointers.getProperty("dblocation");
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                String filename = ComponentPointers.getProperty("dblocation");
                try {
                    LookupDB lookupDB = gson.fromJson(new FileReader(new File(filename)), LookupDB.class);
                    lookupDB.getCollection().forEach((document) -> {
                        if (document.getType().equals("ProvTemplate")) {
                            logger.debug("Image file to load: " + document.getUri());
                            integrationModel.setProvNFilename(document.getUri());
                            integrationModel.getProvenancePanel().loadGraph(true);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            // The lookup database telling us where to find the provenance is in a MongoDB database
            case SQLITE:
                String sqlitedb = ComponentPointers.getProperty("sqlitedb");
                // Find the entry in the lookup database that contains the ProvStore entry for the provenance graph
                logger.trace("Query type: " + querytype);
                integrationModel.setProvNFilename(Connector.retrieveTemplateFromProvStore(querytype, sqlitedb));
                break;
        }


    }

    /**
     * Method to convert an input stream from the API into raw string format.
     *
     * @param in Input stream to be converted.
     * @return Raw data string.
     */
    public static ArrayList<String> readStream2Array(InputStream in) {
        ArrayList<String> returns = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                returns.add(nextLine);
            }
        } catch (IOException ignored) {

        }
        return returns;
    }

}
