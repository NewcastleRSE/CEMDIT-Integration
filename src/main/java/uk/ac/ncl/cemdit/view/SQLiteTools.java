package uk.ac.ncl.cemdit.view;

import org.apache.commons.cli.*;

public class SQLiteTools {

    static public void main(String[] args) {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option i = Option.builder("c").longOpt("create").required().hasArg().desc("Create database and tables").build();
        options.addOption(i);

        org.apache.commons.cli.CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String infile = cmd.getOptionValue("input");

            System.out.println("Input filename: " + infile);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar VCFCombine.jar\n" + "Version: 1\n"
                    + "Program for combining a VCF and its Annovar annotation (.avinput) file.", options);
        }
    }
}
