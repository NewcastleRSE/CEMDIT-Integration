package uk.ac.ncl.cemdit.view.integration;

import org.apache.log4j.Logger;
import uk.ac.ncl.cemdit.model.integration.IntegrationDataModel;
import uk.ac.ncl.cemdit.model.integration.IntegrationModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CEMDITMainPanel extends JPanel implements ActionListener, ListSelectionListener {
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Panel containing the original query
     */
    private JPanel queryPanel = new JPanel();
    /**
     * Text area containing the original query
     */
    private JTextArea queryTextArea = new JTextArea();
    /**
     * Button to submit query
     */
    private JButton queryButton = new JButton("Run");

    /**
     * Panel containing the top ranked response (or else the selected response)
     */
    private ResultsPanel resultPanel;

    /**
     * Panel containing all the returned results
     */
    private ResponsePanel otherPanel;

    private IntegrationModel integrationModel = new IntegrationModel();

    private IntegrationDataModel integrationDataModel;

    public CEMDITMainPanel(IntegrationModel integrationModel, IntegrationDataModel integrationDataModel) {
        super();
        this.integrationModel = integrationModel;
        this.integrationDataModel = integrationDataModel;
        setup();
    }

    public CEMDITMainPanel() {
        super();
        setup();
    }

    private void setup() {
        FlowLayout queryLayout = new FlowLayout();
        queryLayout.setAlignment(FlowLayout.LEADING);
        queryPanel.setLayout(queryLayout);
        queryTextArea.setColumns(75);
        queryButton.addActionListener(this);
        queryTextArea.setText(integrationModel.getOriginalQuery());
        resultPanel = new ResultsPanel(integrationModel, integrationDataModel, this);
        otherPanel = new ResponsePanel(integrationModel.getOtherResponses(), this);

        setLayout(new BorderLayout());
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("CEM-DIT Data Visualisation Screen"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        this.getBorder()));


        queryPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Your Query"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        queryPanel.getBorder()));
        resultPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Top Ranked Response"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        resultPanel.getBorder()));
        otherPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Other Responses"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        otherPanel.getBorder()));

        queryPanel.add(queryTextArea);
        queryPanel.add(queryButton);
        add(queryPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(otherPanel, BorderLayout.PAGE_END);
    }

    public IntegrationModel getIntegrationModel() {
        return integrationModel;
    }

    public void setIntegrationModel(IntegrationModel integrationModel) {
        this.integrationModel = integrationModel;
    }

    public void setProvenancePanel() {
        resultPanel.setButtons(true, false, false);
    }

    public void setDataPanel() {
        resultPanel.setButtons(false, true, false);
    }

    public void setMatchPanel() {
        resultPanel.setButtons(false, false, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Run":
                break;
            case "View Provenance":
                if (e.getActionCommand().equals("View Provenance")) {

                    setProvenancePanel();
                }
                break;
            case "View Data":
                setDataPanel();
                this.revalidate();
                break;
            case "View Matches":
                setMatchPanel();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent  e) {
        JList jList = (JList)e.getSource();
        ListSelectionModel lsm = jList.getSelectionModel();

        for (int i = 0; i <= lsm.getMaxSelectionIndex(); i++) {
            if (lsm.isSelectedIndex(i)) {
                logger.debug("Selected: " + i);
            }
        }
    }

    public IntegrationDataModel getIntegrationDataModel() {
        return integrationDataModel;
    }

    public void setIntegrationDataModel(IntegrationDataModel integrationDataModel) {
        this.integrationDataModel = integrationDataModel;
    }
}
