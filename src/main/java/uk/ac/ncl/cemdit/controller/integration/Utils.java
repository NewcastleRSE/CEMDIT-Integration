package uk.ac.ncl.cemdit.controller.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.controller.ComponentPointers;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.model.integration.QueryResults;
import uk.ac.ncl.cemdit.model.integration.lookupDB.LookupDB;
import uk.ac.ncl.cemdit.view.integration.QueryType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

    static private Logger logger = Logger.getLogger(Utils.class);


    static public void populateIntegrationModel(String query, IntegrationModel integrationModel, IntegrationDataModel integrationDataModel, QueryType queryType) {
        ArrayList<String> data = new ArrayList<>();
        ArrayList<ArrayList<Object>> data1 = new ArrayList<>();
        ArrayList<Object> row = new ArrayList<>();
        ArrayList<Object> row1 = new ArrayList<>();
        String[] columnNames;
        Class[] columnClasses;

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
                ArrayList<QueryResults> queryResults = new ArrayList<>();
                for (int i = 0; i < sourceLabel.length; i++) {
                    QueryResults queryResult = new QueryResults(sourceLabel[i], operators[i], targetLabel[i]);
                    queryResults.add(queryResult);
                }
                integrationModel.setQueryResults(queryResults);
                integrationModel.setProvNFilename("/home/campus.ncl.ac.uk/njss3/Dropbox/CEM-DIT/CHAIn/Mockups/count_prov.svg");
//        integrationDataModel.fire();
                break;
            case REST:
                //Do REST query
                try {
                    URL url = new URL(query);
                    HttpURLConnection con = null;
                    con = (HttpURLConnection) url.openConnection();

                    ArrayList<String> rawData = readStream2Array(con.getInputStream());
                    data.add(query);
                    integrationModel.setTopRankedQuery(query);
                    String headers = rawData.get(0);
                    integrationDataModel.setColumnNames(headers.split(","));
                    for (int i = 1; i < rawData.size(); i++) {
                        String[] info = rawData.get(i).split(",");
                        ArrayList<Object> al_info = new ArrayList();
                        for (int j = 0; j < info.length; j++) {
                            al_info.add(info[j]);
                        }
                        data1.add(al_info);
                    }
                    integrationDataModel.setData(data1);

                    //integrationDataModel.setClasses(columnClasses);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Populate

                break;
            default:
                logger.info("No query specified");
                break;
        }
    }

    static public void lookupProvenance(LookupType lookupType, IntegrationModel integrationModel) {
        // LOOK UP PROVENANCE
        // Get lookup database url from system.properties

        // Get query provenance template address for png file
        // e.g. https://openprovenance.org/store/documents/497.png

        //MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        //MongoCollection<Document> collection = database.getCollection("COLLECTION");

        // For now let's create a json file with the info that should go in the lookup database

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
                            integrationModel.getProvenancePanel().loadGraph();
                        }
                    });


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            // The lookup database telling us where to find the provenance is in a MongoDB database
            case MONGODB:
                System.out.println("not yet implemented");
                break;
        }


    }

    /**
     * Method to convert an input stream from the API into raw string format.
     *
     * @param in Input stream to be converted.
     * @return Raw data string.
     */
    public static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine).append(System.getProperty("line.separator"));
            }
        } catch (IOException ignored) {

        }
        return sb.toString();
    }


    /**
     * Method to convert an input stream from the API into raw string format.
     *
     * @param in Input stream to be converted.
     * @return Raw data string.
     */
    public static ArrayList<String> readStream2Array(InputStream in) {
        ArrayList<String> returns = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                returns.add(nextLine);
            }
        } catch (IOException ignored) {

        }
        return returns;
    }

}
