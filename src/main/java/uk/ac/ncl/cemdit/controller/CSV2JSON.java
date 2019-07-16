package uk.ac.ncl.cemdit.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CSV2JSON {


    public CSV2JSON() {
    }

    /**
     * https://lucmoreau.wordpress.com/2017/03/30/prov-template-a-quick-start/
     * Convert the given csv file to json format
     * @param csvfile the csv file to convert
     * @param jsonfile the base name of the files to write to. There will be one file per row from the csv file.
     * @return return an arraylist<String> of json filenames
     */
    static public ArrayList<String> convert(File csvfile, String jsonfile) {
        ArrayList<String> filenames = new ArrayList<>();
        try {
            Scanner sc = new Scanner(csvfile);

            boolean first = true;
            int index = 0;
            String headline1 = sc.nextLine();
            String[] head = headline1.split(",");
            String headline2 = sc.nextLine();
            String[] type = headline2.split(",");
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] tokens = line.split(",");
                PrintWriter pw = new PrintWriter(new File(jsonfile + index + ".json"));
                filenames.add(jsonfile + index + ".json");
                pw.println("{\"var\":\n{");
                for (int token = 0; token < tokens.length; token++) {
                    if (token > 0) {
                        pw.println(",");
                    }
                    if (type[token].trim().equals("prov:QUALIFIED_NAME")) {
                        pw.print("\"" + head[token].trim() + "\": [{\"@id\":\"" + tokens[token].trim() + "\"}]");
                    } else if (type[token].trim().equals("xsd:string")) {
                        pw.print("\"" + head[token].trim() + "\": [{\"@value\": \"" + tokens[token] + "\", \"@type\": \"xsd:string\"} ]");
                    } else if (type[token].trim().equals("xsd:int")) {
                        pw.print("\"" + head[token].trim() + "\": [{\"@value\": \"" + tokens[token] + "\", \"@type\": \"xsd:int\"} ]");
                    } else {
                        pw.print("\"" + head[token].trim() + "\": [ {\"@value\": \"" + head[token].trim() + "\", \"@type\": \"" + type[token].trim() + "\"} ]");
                    }
                }
                pw.print("},\n");
                pw.print("\"context\":{\"ex\": \"http://example.org/\",\"uo\": \"http://urbanobservatory.ac.uk/\"}\n");
                pw.print("}\n");
                pw.close();
                index++;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filenames;
    }
}
