package uk.ac.ncl.cemdit.controller;

import org.apache.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

/**
 * https://lucmoreau.wordpress.com/2017/03/30/prov-template-a-quick-start/
 */
public class ProvConvertItems {
    static private Logger logger = Logger.getLogger(ProvConvertItems.class);
    /**
     * Use the Provenance toolbox to convert a provn file to any of the supported file types. File type exported
     * to is determined by the extension of the file.
     *
     * @param provn_file
     * @param output_file
     */
    static public void convertProvN(String provn_file, String output_file) {
        try {
            InteropFramework intF = new InteropFramework();
            Document document = intF.readDocumentFromFile(provn_file);
            intF.writeDocument(output_file, document);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    static public void merge(String indexfilenames, String outfile) {
        String help = null;
        String version = null;
        String verbose = null;
        String debug = null;
        String logfile = null;
        String infile = null;
        String informat = null;
        //String outfile = null;
        String outformat = null;
        String namespaces = null;
        String title = null;
        String layout = null;
        String bindings = null;
        String bindingformat = null;
        String generator = null;
        String index = null;
        String flatten = "flatten";
        String merge = indexfilenames;
        String compare = null;
        String compareOut = null;
        String template = null;
        String packge = null;
        String location = null;
        int bindingsVersion = 1;
        boolean addOrderp = false;
        boolean listFormatsp = false;
        boolean allexpanded = false;
        InteropFramework interop = new InteropFramework(verbose,
                debug,
                logfile,
                infile,
                informat,
                outfile,
                outformat,
                namespaces,
                title,
                layout,
                bindings,
                bindingformat,
                bindingsVersion,
                addOrderp,
                allexpanded,
                template,
                packge,
                location,
                generator,
                index,
                merge,
                flatten,
                compare,
                compareOut,
                org.openprovenance.prov.xml.ProvFactory.getFactory());
        interop.run();
    }

    static public void bind(String bindver, String infile, String bindings, String outfile) {
        String help = null;
        String version = null;
        String verbose = null;
        String debug = null;
        String logfile = null;
        //String infile = null;
        String informat = null;
        //String outfile = null;
        String outformat = null;
        String namespaces = null;
        String title = null;
        String layout = null;
        //String bindings = null;
        String bindingformat = null;
        String generator = null;
        String index = null;
        String flatten = null;
        String merge = null;
        String compare = null;
        String compareOut = null;
        String template = null;
        String packge = null;
        String location = null;
        int bindingsVersion = new Integer(bindver);
        boolean addOrderp = false;
        boolean listFormatsp = false;
        boolean allexpanded = false;
        InteropFramework interop = new InteropFramework(verbose,
                debug,
                logfile,
                infile,
                informat,
                outfile,
                outformat,
                namespaces,
                title,
                layout,
                bindings,
                bindingformat,
                bindingsVersion,
                addOrderp,
                allexpanded,
                template,
                packge,
                location,
                generator,
                index,
                merge,
                flatten,
                compare,
                compareOut,
                org.openprovenance.prov.xml.ProvFactory.getFactory());
        interop.run();
    }
}
