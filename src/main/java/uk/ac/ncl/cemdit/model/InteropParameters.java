package uk.ac.ncl.cemdit.model;

public class InteropParameters {
    private String verbose = null;
    private String debug = null;
    private String logfile = null;
    private String infile = null;
    private String informat = null;
    private String outfile = null;
    private String outformat = null;
    private String namespaces = null;
    private String title = null;
    private String layout = null;
    private String bindings = null;
    private String bindingformat = null;
    private String generator = null;
    private String index = null;
    private String flatten = null;
    private String merge = null;
    private String compare = null;
    private String compareOut = null;
    private String template = null;
    private String bpackage = null;
    private String location = null;
    private int bindingsVersion = 3;
    private boolean addOrderp = false;
    private boolean listFormatsp = false;
    private boolean allexpanded = false;
    private String lastdir = "";

    public String getVerbose() {
        return verbose;
    }

    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getLogfile() {
        return logfile;
    }

    public void setLogfile(String logfile) {
        this.logfile = logfile;
    }

    public String getInfile() {
        return infile;
    }

    public void setInfile(String infile) {
        this.infile = infile;
    }

    public String getInformat() {
        return informat;
    }

    public void setInformat(String informat) {
        this.informat = informat;
    }

    public String getOutfile() {
        return outfile;
    }

    public void setOutfile(String outfile) {
        this.outfile = outfile;
    }

    public String getOutformat() {
        return outformat;
    }

    public void setOutformat(String outformat) {
        this.outformat = outformat;
    }

    public String getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(String namespaces) {
        this.namespaces = namespaces;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getBindings() {
        return bindings;
    }

    public void setBindings(String bindings) {
        this.bindings = bindings;
    }

    public String getBindingformat() {
        return bindingformat;
    }

    public void setBindingformat(String bindingformat) {
        this.bindingformat = bindingformat;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFlatten() {
        return flatten;
    }

    public void setFlatten(String flatten) {
        this.flatten = flatten;
    }

    public String getMerge() {
        return merge;
    }

    public void setMerge(String merge) {
        this.merge = merge;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getCompareOut() {
        return compareOut;
    }

    public void setCompareOut(String compareOut) {
        this.compareOut = compareOut;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getBpackage() {
        return bpackage;
    }

    public void setBpackage(String bpackage) {
        this.bpackage = bpackage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBindingsVersion() {
        return bindingsVersion;
    }

    public void setBindingsVersion(int bindingsVersion) {
        this.bindingsVersion = bindingsVersion;
    }

    public boolean isAddOrderp() {
        return addOrderp;
    }

    public void setAddOrderp(boolean addOrderp) {
        this.addOrderp = addOrderp;
    }

    public boolean isListFormatsp() {
        return listFormatsp;
    }

    public void setListFormatsp(boolean listFormatsp) {
        this.listFormatsp = listFormatsp;
    }

    public boolean isAllexpanded() {
        return allexpanded;
    }

    public void setAllexpanded(boolean allexpanded) {
        this.allexpanded = allexpanded;
    }

    public String getLastdir() {
        return lastdir;
    }

    public void setLastdir(String lastdir) {
        this.lastdir = lastdir;
    }

    public void resetValues() {
        verbose = null;
        debug = null;
        logfile = null;
        infile = null;
        informat = null;
        outfile = null;
        outformat = null;
        namespaces = null;
        title = null;
        layout = null;
        bindings = null;
        bindingformat = null;
        generator = null;
        index = null;
        flatten = null;
        merge = null;
        compare = null;
        compareOut = null;
        template = null;
        bpackage = null;
        location = null;
        bindingsVersion = 3;
        addOrderp = false;
        listFormatsp = false;
        allexpanded = false;
        lastdir = "";
    }

    public String toString() {
        return("-verbose: " + verbose +
                "\n" + "-debug " + debug +
                "\n" + "-logfile" + logfile +
                "\n" + "-flatten " + flatten +
                "\n" + "-index " + index +
                "\n" + "-merge " + merge +
                "\n" + "-informat " + informat +
                "\n" + "-infile " + infile +
                "\n" + "-outformat " + outformat +
                "\n" + "-outfile " + outfile +
                "\n" + "-namespaces " + namespaces +
                "\n" + "-title " + title +
                "\n" + "-layout " + layout +
                "\n" + "-bindings " + bindings +
                "\n" + "-bindformat " + bindingformat +
                "\n" + "-generator " + generator +
                "\n" + "-genorder " + (generator == null ? "" : generator) +
                "\n" + "-allexpanded " + allexpanded +
                "\n" + "-bindver " + bindingsVersion +
                "\n" + "-template " + template +
                "\n" + "-package " + bpackage +
                "\n" + "-location " + location +
                "\n" + "-compare " + compare +
                "\n" + "-compareOut " + compareOut);
    }
}
