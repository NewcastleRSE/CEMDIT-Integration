package uk.ac.ncl.cemdit.view;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import uk.ac.ncl.cemdit.controller.ComponentPointers;

import javax.swing.*;
import java.io.IOException;

public class TextPanes extends JTabbedPane {
    ComponentPointers componentPointers = ComponentPointers.getInstance();
    private RSyntaxTextArea provN = new RSyntaxTextArea();
    private RSyntaxTextArea policy = new RSyntaxTextArea();
    private RSyntaxTextArea changed = new RSyntaxTextArea();
    private RSyntaxTextArea newprov = new RSyntaxTextArea();
    private RSyntaxTextArea template = new RSyntaxTextArea();
    private static String theme = "/themes/provn.xml";
    /**
     * Name of PROV-N file - filename without path for setting title of tab
     */
    private String provnFile = "Untitled";
    /**
     * Name of policy while - filename without path for setting title of tab
     */
    private String policyFile = "Untitled";

    public TextPanes() {
        RTextScrollPane sp_provN;
        RTextScrollPane sp_policy;
        RTextScrollPane sp_changed;
        RTextScrollPane sp_newprov;
        RTextScrollPane sp_template;
        //newprov.setDocument(new SqlDocument());
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/provn", "uk.ac.ncl.cemdit.controller.PROVN_TokenMaker");

        // PROV-N text panel
        provN.setSyntaxEditingStyle("text/provn");
        provN.setCodeFoldingEnabled(true);
        setTheme(provN, theme);
        // Policy text panel
        policy.setSyntaxEditingStyle("text/provn");
        policy.setCodeFoldingEnabled(true);
        // PROV-N panel after policy has been applied
        changed.setSyntaxEditingStyle("text/provn");
        changed.setCodeFoldingEnabled(true);
        // Create a new file
        newprov.setSyntaxEditingStyle("text/provn");
        newprov.setCodeFoldingEnabled(true);
        // PROV-N Template panel
        template.setSyntaxEditingStyle("text/provn");
        template.setCodeFoldingEnabled(true);

        sp_provN = new RTextScrollPane(provN);
        sp_policy = new RTextScrollPane(policy);
        sp_changed = new RTextScrollPane(changed);
        sp_newprov = new RTextScrollPane(newprov);
        sp_template = new RTextScrollPane(template);
        sp_provN.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_policy.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_changed.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_newprov.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp_template.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        addTab("ProvN", sp_provN);              //0
        addTab("Policy", sp_policy);            //1
        addTab("Policy Applied", sp_changed);   //2
        addTab("New file", sp_newprov);         //3
        addTab("Template", sp_template);        //4
    }

    public RSyntaxTextArea getProvN() {
        return provN;
    }

    public RSyntaxTextArea getPolicy() {
        return policy;
    }

    public RSyntaxTextArea getChanged() {
        return changed;
    }

    public RSyntaxTextArea getNewprov() { return newprov; }

    public RSyntaxTextArea getTemplate() {return template; }

    public void setTheme(RSyntaxTextArea syntaxTextArea, String str_theme) {
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    str_theme));
            theme.apply(syntaxTextArea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProvnFile() {
        return provnFile;
    }

    public void setProvnFile(String provnFile) {
        this.provnFile = provnFile;
    }

    public String getPolicyFile() {
        return policyFile;
    }

    public void setPolicyFile(String policyFile) {
        this.policyFile = policyFile;
    }
}
