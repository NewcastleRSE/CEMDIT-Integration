package uk.ac.ncl.cemdit.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton to controll access to the MongoDB database
 *
 * @author Jannetta S Steyn
 */

public class MongoDBController {

    private static MongoClient mongoDBController = null;
    private static MongoDatabase mongoDatabase = null;

    /**
     * Server: wudlorth
     * Database: UrbanObservatory
     * IP: 10.70.39.136
     */
    protected MongoDBController() {

    }

    public static MongoClient getInstance(String host, Integer port, String database, String username, String password) {
        if (mongoDBController == null) {
            mongoDBController = new MongoClient(host, port);
            mongoDatabase = mongoDBController.getDatabase(database);
        } else {
        }
        return mongoDBController;

    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

}
