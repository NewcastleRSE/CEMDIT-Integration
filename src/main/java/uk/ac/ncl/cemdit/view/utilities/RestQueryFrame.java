package uk.ac.ncl.cemdit.view.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import uk.ac.ncl.cemdit.model.urbanobservatory.Sensor;
import uk.ac.ncl.cemdit.model.urbanobservatory.vehicles.VehiclesSchema;
import uk.ac.ncl.cemdit.view.widgets.TextWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class RestQueryFrame extends JFrame implements ActionListener {
    Logger logger = Logger.getRootLogger();
    JPanel mainpanel = new JPanel();
    //private static String server = "http://uoweb3.ncl.ac.uk";
    private static String server = "http://api.newcastle.urbanobservatory.ac.uk";
    //    private static String restcall = "/api/v1.1/sensors/json";
    private static String restcall = "/api/v2.0a/sensors/entity";
    //    private static String parameters = "sensory_type=Traffic Flow";
    private static String parameters = "pageSize=200&metric=Camera image";
    private static String classname = "uk.ac.ncl.cemdit.model.urbanobservatory.Sensors";
    private static String str_mongodbprotocol = "mongodb+srv://";
    private static String str_mongodbserver = "cluster0-3kdaq.mongodb.net";
    private static String str_mongodb_database = "ProvStore";
    private static String str_mongodb_username = "ProvStore";
    private static String str_mongodb_password = "Pr0vSt0r3";
    private static String str_mongodb_collection = "Sensors";
    static private TextWidget txt_server = new TextWidget("REST Server", 50);
    static private TextWidget txt_restcall = new TextWidget("REST API Call", 50);
    static private TextWidget txt_parameters = new TextWidget("Parameters", 50);
    static private TextWidget txt_class = new TextWidget("Class", 50);
    static private JButton execute = new JButton("Execute");
    static private TextWidget txt_mongodbprotocol = new TextWidget("MongoDB Protocol", 50);
    static private TextWidget txt_mongodbserver = new TextWidget("MongoDB Server", 50);
    static private TextWidget txt_mongodbUsername = new TextWidget("MongoDB Username", 50);
    static private TextWidget txt_mongodbPassword = new TextWidget("MongoDB Password", 50);
    static private TextWidget txt_mongodb_database = new TextWidget("Database", 50);
    static private TextWidget txt_mongodb_collection = new TextWidget("Collection", 50);
    static private JButton insert = new JButton("Insert");
    private static String lastdir;
    private static RSyntaxTextArea txt_restreturn = new RSyntaxTextArea();
    private RTextScrollPane sp_restreturn = new RTextScrollPane(txt_restreturn);
    private VehiclesSchema sensors = null;
    //Class<?> sensors = null;

    RestQueryFrame() {
        super("REST Query");
        Logger logger = Logger.getRootLogger();
        try {
            File f = new File("server.properties");
            Properties properties = new Properties();
            InputStream is = new FileInputStream(f);
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream("server.properties");
            }
            properties.load(is);
            lastdir = properties.getProperty("lastdir");
            System.out.println(lastdir);
        } catch (Exception e) {
            System.out.println("Properties file not found");
        }

        txt_restreturn.setCodeFoldingEnabled(true);
        txt_restreturn.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
        //txt_restreturn.
        sp_restreturn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_restreturn.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel leftpanel = new JPanel();
        leftpanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 0;
        leftpanel.add(txt_server, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 1;
        leftpanel.add(txt_restcall, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 2;
        leftpanel.add(txt_parameters, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 3;
        leftpanel.add(txt_class, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 4;
        leftpanel.add(execute, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 5;
        leftpanel.add(txt_mongodbprotocol, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 6;
        leftpanel.add(txt_mongodbserver, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 7;
        leftpanel.add(txt_mongodbUsername, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 8;
        leftpanel.add(txt_mongodbPassword, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 9;
        leftpanel.add(txt_mongodb_database, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 10;
        leftpanel.add(txt_mongodb_collection, c);
        c.weighty = .25;
        c.gridx = 0;
        c.gridy = 11;
        leftpanel.add(insert, c);

        JPanel rightpanel = new JPanel();
        rightpanel.setLayout(new BorderLayout());

        rightpanel.add(sp_restreturn);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftpanel, rightpanel);
        splitPane.setResizeWeight(0.5);

        execute.addActionListener(this);
        insert.addActionListener(this);

        txt_server.setReturnString(server);
        txt_restcall.setReturnString(restcall);
        txt_parameters.setReturnString(parameters);
        txt_class.setReturnString(classname);

        txt_mongodbprotocol.setReturnString(str_mongodbprotocol);
        txt_mongodbUsername.setReturnString(str_mongodb_username);
        txt_mongodbPassword.setReturnString(str_mongodb_password);
        txt_mongodbserver.setReturnString(str_mongodbserver);
        txt_mongodb_database.setReturnString(str_mongodb_database);
        txt_mongodb_collection.setReturnString(str_mongodb_collection);

        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(splitPane);
        setContentPane(mainpanel);

        this.pack();
        this.setVisible(true);
        this.setSize(1024, 768);
    }

    public static void main(String[] args) {
        RestQueryFrame restQueryFrame = new RestQueryFrame();
        //System.out.println("\n============Output:============ \n" + callURL(server + restcall + "?" + parameters));
    }

    public static String callURL(String myURL) {
        System.out.println("Requested URL: " + myURL);
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + myURL, e);
        }
        return sb.toString();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Execute")) {
            try {
                server = txt_server.getReturnString();
                restcall = txt_restcall.getReturnString();
                parameters = txt_parameters.getReturnString();
                parameters = parameters.replaceAll(" ", "%20");
                String urlstring;
                if (restcall.equals("") || restcall == null) {
                    urlstring = server;
                } else {
                    urlstring = server + "/" + restcall;
                }
                URL url = new URL(urlstring);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (!(parameters.equals("") || parameters == null)) {
                    String[] tokens = parameters.split("&");
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if (tokens.length != 0) {
                        System.out.println("Number of tokens: " + tokens.length);
                        for (String t : tokens) {
                            //conn.setRequestProperty(t.split("=")[0],t.split("=")[1]);
                            // System.out.println(t.split("=")[0] + "="+t.split("=")[1]);
                        }
                    }
                }

              BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                StringBuilder result = new StringBuilder();
                String tmp;
                while ((tmp = br.readLine()) != null) {
                    result.append(tmp);
                }
                tmp = result.toString() + "\n";
                System.out.println(tmp);
                conn.disconnect();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(tmp);
                txt_restreturn.setText(gson.toJson(je));
//                txt_restreturn.read(br, null);
                txt_restreturn.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            //                Gson gson = new GsonBuilder().setPrettyPrinting().create();

//                Class<?> clazz = Class.forName(txt_class.getReturnString());

//                sensors = gson.fromJson(callURL(url), VehiclesSchema.class);
            //               txt_restreturn.setText(gson.toJson(sensors));

            txt_restreturn.setCodeFoldingEnabled(true);
            txt_restreturn.setSyntaxEditingStyle("JSON");

    }
        if(e.getActionCommand().

    equals("Insert"))

    {
        System.out.println("Insert");
        String protocol = txt_mongodbprotocol.getReturnString();
        String username = txt_mongodbUsername.getReturnString();
        String password = txt_mongodbPassword.getReturnString();
        String server = txt_mongodbserver.getReturnString();
        String dbname = txt_mongodb_database.getReturnString();
        String collection = txt_mongodb_collection.getReturnString();
        String connectionString = protocol + username + ":" + password + "@" + server + "/" + dbname;
        System.out.println(connectionString);
        MongoClientURI uri = new MongoClientURI(connectionString);

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(dbname);
        CodecRegistry codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        final MongoDatabase codecmongoDatabase = database.withCodecRegistry(codecRegistry);
        MongoCollection mongoCollection = codecmongoDatabase.getCollection("Sensors", Sensor.class);
//            if (sensors instanceof Sensors) {
//                java.util.List<Sensor> sensorList = sensors.getSensors();
//                sensorList.forEach(sensor -> {
//                    System.out.println(sensor.getSensorName());
//                    mongoCollection.insertOne(sensor);
//                });
//            }
    }

}
}
