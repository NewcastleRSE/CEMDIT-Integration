package uk.ac.ncl.cemdit.view;

public class StringsAndStuff {
    private static String about = "This is a GUI for the 'provconvert' utility. The GUI is being developed by\n" +
            "Jannetta Steyn as part of the CEM-DIT project. 'provconvert' is part of the Provenance Toolbox\n" +
            "(ProvToolbox) which is developed by Luc Moreau.";


    public static String getAbout() {
        return about;
    }

    public static void setAbout(String about) {
        StringsAndStuff.about = about;
    }

    public static String camelCase(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
