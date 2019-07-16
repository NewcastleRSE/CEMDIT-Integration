package uk.ac.ncl.cemdit.view.utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import uk.ac.ncl.cemdit.model.urbanobservatory.Sensor;
import uk.ac.ncl.cemdit.model.urbanobservatory.Sensors;

import javax.swing.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class UpdateDatabase {
    Logger logger = Logger.getRootLogger();
    JPanel mainpanel = new JPanel();
    String filename = "D:\\WindowsOneDrive\\OneDrive - Newcastle University\\CEMDIT\\UseCase_CCTV\\carcounts.csv";
    private static String str_mongodbprotocol = "mongodb+srv://";
    private static String str_mongodbserver = "cluster0-3kdaq.mongodb.net";
    private static String str_mongodb_database = "ProvStore";
    private static String str_mongodb_username = "ProvStore";
    private static String str_mongodb_password = "Pr0vSt0r3";
    private static String str_mongodb_collection = "Sensors";
    private Sensors sensors = null;

    static public void main(String[] args) {

    }

    public void insertDB() {
        String protocol = str_mongodbprotocol;
        String username = str_mongodb_username;
        String password = str_mongodb_password;
        String server = str_mongodbserver;
        String dbname = str_mongodb_database;
        String collection = str_mongodb_collection;
        String connectionString = protocol +  username + ":" + password + "@" + server + "/" + dbname;
        System.out.println(connectionString);
        MongoClientURI uri = new MongoClientURI(connectionString);

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(dbname);
        CodecRegistry codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        final MongoDatabase codecmongoDatabase = database.withCodecRegistry(codecRegistry);
        MongoCollection mongoCollection = codecmongoDatabase.getCollection("Sensors", Sensor.class);
        if (sensors!=null) {
            java.util.List<Sensor> sensorList = sensors.getSensors();
            sensorList.forEach(sensor -> {
                System.out.println(sensor.getSensorName());
                mongoCollection.insertOne(sensor);
            });
        }
    }

}

