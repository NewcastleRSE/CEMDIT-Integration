package uk.ac.ncl.cemdit.view.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * TODO
 * a. Move magic values to properties file
 *
 * 1. Request record from mongoDB database using a query id eg. "VehicleCounts"
 * {
 * 	"_id": {
 * 		"$oid": "5c5a37801c9d4400002570d1"
 *        },
 * 	"QueryID": "VehicleCounts",
 * 	"ProvStoreID": "496",
 * 	"RESTQuery": "http://uoweb3.ncl.ac.uk/api/v1.1/sensors/PER_PEOPLE_NORTHUMERLAND_LINE_MID_DISTANCE_HEAD_0/data/json/?last_n_records=10"
 * 	"ModelID": "Tensorflow_inceptionv2_carcount",
 * 	"TrainingSet": "all_cameras_2018march_picked_images_every_90_110",
 * 	"Annotator_name": "Tom Kumar",
 * 	"Architecture": "Inception v2",
 * 	"HyperParameters": {
 * 		"epochs": "4",
 * 		"weight_decays": ".2",
 * 		"dropout": "0",
 * 		"L2_weight": ".2"
 *    }
 * }
 * 2. Retrieve template from ProvStore using id obtain from mongoDB record, eg. "496"
 * 3. Retrieve UO data using REST query, eg. "http://uoweb3.ncl.ac.uk/api/v1.1/sensors/PER_PEOPLE_NORTHUMERLAND_LINE_MID_DISTANCE_HEAD_0/data/json/?last_n_records=10"
 * 4. Create data records from retrieved data for binding with template
 * 5. Bind with template
 * 6. Create and display graph
 *
 */
public class TestREST {

    static public void main(String[] args) {

        try {
            String userName = "jannetta";
            String docID = "496";
            String fileformat = ".provn";
            String password = "d02ceaf180943230961e1f9ece668316d1fb8583";
            String baseURL = "https://openprovenance.org/store/api/v0/documents/";
            URL url = new URL(baseURL + docID + fileformat);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization",
                    "ApiKey " + userName + ":" + password);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            InputStream is = conn.getInputStream();
            StringBuffer buf = new StringBuffer();
            int c;
            while ((c = is.read()) != -1) {
                buf.append((char) c);
            }
            conn.disconnect();
            String buff;
            if (fileformat.equals(".json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(buf.toString());
                buff = gson.toJson(je);
            } else {
                buff = buf.toString();
            }
            System.out.println(buff);


//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    (conn.getInputStream())));
//            StringBuilder result = new StringBuilder();
//            String tmp;
//            tmp = result.toString() + "\n";
//            while ((tmp = br.readLine()) != null) {
//                result.append(tmp);
//            }
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            JsonParser jp = new JsonParser();
//            JsonElement je = jp.parse(tmp);
//            tmp = gson.toJson(je);
//            System.out.println(tmp);
            conn.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
