package uk.ac.ncl.cemdit.controller.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;
import uk.ac.ncl.cemdit.model.integration.QueryResults;

import java.util.ArrayList;

public class Utils {

    static private Logger logger = Logger.getLogger(Utils.class);

    static public void populateIntegrationModel(String query, IntegrationModel integrationModel, IntegrationDataModel integrationDataModel) {
        // read data for other results
        ArrayList<String> data = new ArrayList<>();
        data.add("1. sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)");
        data.add("2. sensor(sensor_type(traffic_flow), location_WKT, timestamp, units, measure)");
        data.add("3. sensor(sensor_type(traffic_flow), location_WKT, timestamp, units, measure)");

        ArrayList<ArrayList<Object>> data1 = new ArrayList<>();
        integrationModel.setTopRankedQuery("sensor(theme(Vehicles),Sensor_name, sensor_centroid_latitude, sensor_centroid_longitude, timestamp, units, count)");

        ArrayList<Object> row = new ArrayList<>();
        row.add(0, "Vehicle");
        row.add(1, "VEHICLE_COUNT_01");
        row.add(2, "43.8499984741");
        row.add(3, "-1.6118754505");
        row.add(4, "1546301013000");
        row.add(5, "Vehicles");
        row.add(6, "11");
        data1.add(0, row);

        ArrayList<Object> row1 = new ArrayList<>();
        row1.add(0, "Vehicle");
        row1.add(1, "VEHICLE_COUNT_01");
        row1.add(2, "43.8488884741");
        row1.add(3, "-1.6118754505");
        row1.add(4, "1546301013000");
        row1.add(5, "Vehicles");
        row1.add(6, "6");
        data1.add(1, row1);

        integrationDataModel.setData(data1);
        integrationDataModel.setColumName(0, "Theme");
        integrationDataModel.setColumName(1, "Sensor Name");
        integrationDataModel.setColumName(2, "Sensor centroid latitude");
        integrationDataModel.setColumName(3, "Sensor centroid longitude");
        integrationDataModel.setColumName(4, "Timestamp");
        integrationDataModel.setColumName(5, "Units");
        integrationDataModel.setColumName(6, "Count");


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
        integrationDataModel.fire();
    }
}
