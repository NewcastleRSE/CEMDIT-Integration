package uk.ac.ncl.cemdit.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoDBControllerTest {
    String host = "cluster0-3kdaq.mongodb.net";
    String dbname = "ProvStore";
    String username = "ProvStore";
    String password = "Pr0vSt0r3";
    MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://" + username + ":" + password + "@" + host + "/" + dbname);

    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase(dbname);

    @DisplayName("Get Collection")
    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @DisplayName("Get Collection")
    @org.junit.jupiter.api.Test
    void getCollection() {
        assertEquals(0,database.getCollection("Sensors").countDocuments());
   }


}