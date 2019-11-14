package uk.ac.ncl.cemdit.dao.sqlite;

import org.junit.jupiter.api.Test;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorTest {
    Connector connector = Connector.getInstance();
    String connectionString = "d:/IdeaProjects/CEMDIT-Integration/sqlite/UrbanObservatory.db";

    @Test
    void connect() {
        try {
            Connection conn = connector.connect(connectionString);
            if (conn == null) {
                fail("Connection doesn't exist");
            } else {
                assertEquals("DATE,TIME,DATETIME,JULIANDAY,STRFTIME", conn.getMetaData().getTimeDateFunctions(), "Connected to database");
                conn.close();
            }
        } catch (SQLException ex) {
            fail("SQL Error.");
        }
    }

    @Test
    void connect1() {
    }

    @Test
    void readRecord() {
        String sql = "SELECT typeName FROM types where typeName=\"CO\"";
        ResultSet rs = Connector.readRecord(sql, connectionString);
        try {
                assertEquals("CO",rs.getString(1));
        } catch (SQLException e) {
            fail("SQL Exception.");
        }
    }

    @Test
    void provenanceTemplates() {
    }

    @Test
    void retrieveTemplateFromProvStore() {
    }

    @Test
    void getSensorReadingsHeadings() {
    }

    @Test
    void readSensorData() {
    }
}