package uk.ac.ncl.cemdit.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author  Administrator
 * @created March 10, 2019
 */
class InfoPanel extends JPanel implements ActionListener {
    private JTextArea taAttributes;
    private JLabel lbLbl_entityType;
    private JTextField tfEntityType;
    private JTextField tfEntityID;
    private JLabel lbLbl_entityID;
    private JLabel lbLbl_Attributes;

    /**
     *Constructor for the InfoPanel object
     */
    public InfoPanel() {
        super();
        setBorder( BorderFactory.createTitledBorder( "Entity AttributesEntityIDEntityIDEntityIDEntityIDEntityID" ) );

        GridBagLayout gbInfoPanel = new GridBagLayout();
        GridBagConstraints gbcInfoPanel = new GridBagConstraints();
        setLayout( gbInfoPanel );

        taAttributes = new JTextArea(30,20);
        JScrollPane scpAttributes = new JScrollPane( taAttributes );
        gbcInfoPanel.gridx = 5;
        gbcInfoPanel.gridy = 7;
        gbcInfoPanel.gridwidth = 15;
        gbcInfoPanel.gridheight = 22;
        gbcInfoPanel.fill = GridBagConstraints.BOTH;
        gbcInfoPanel.weightx = 1;
        gbcInfoPanel.weighty = 1;
        gbcInfoPanel.anchor = GridBagConstraints.NORTH;
        gbInfoPanel.setConstraints( scpAttributes, gbcInfoPanel );
        add( scpAttributes );

        lbLbl_entityType = new JLabel( "Type"  );
        gbcInfoPanel.gridx = 0;
        gbcInfoPanel.gridy = 3;
        gbcInfoPanel.gridwidth = 2;
        gbcInfoPanel.gridheight = 1;
        gbcInfoPanel.fill = GridBagConstraints.BOTH;
        gbcInfoPanel.weightx = 1;
        gbcInfoPanel.weighty = 0;
        gbcInfoPanel.anchor = GridBagConstraints.WEST;
        gbInfoPanel.setConstraints( lbLbl_entityType, gbcInfoPanel );
        add( lbLbl_entityType );

        tfEntityType = new JTextField( );
        gbcInfoPanel.gridx = 5;
        gbcInfoPanel.gridy = 3;
        gbcInfoPanel.gridwidth = 13;
        gbcInfoPanel.gridheight = 1;
        gbcInfoPanel.fill = GridBagConstraints.BOTH;
        gbcInfoPanel.weightx = 1;
        gbcInfoPanel.weighty = 0;
        gbcInfoPanel.anchor = GridBagConstraints.NORTH;
        gbInfoPanel.setConstraints( tfEntityType, gbcInfoPanel );
        add( tfEntityType );

        tfEntityID = new JTextField( );
        gbcInfoPanel.gridx = 5;
        gbcInfoPanel.gridy = 5;
        gbcInfoPanel.gridwidth = 5;
        gbcInfoPanel.gridheight = 1;
        gbcInfoPanel.fill = GridBagConstraints.BOTH;
        gbcInfoPanel.weightx = 1;
        gbcInfoPanel.weighty = 0;
        gbcInfoPanel.anchor = GridBagConstraints.NORTH;
        gbInfoPanel.setConstraints( tfEntityID, gbcInfoPanel );
        add( tfEntityID );

        lbLbl_entityID = new JLabel( "ID"  );
        gbcInfoPanel.gridx = 0;
        gbcInfoPanel.gridy = 5;
        gbcInfoPanel.gridwidth = 4;
        gbcInfoPanel.gridheight = 1;
        gbcInfoPanel.fill = GridBagConstraints.BOTH;
        gbcInfoPanel.weightx = 1;
        gbcInfoPanel.weighty = 0;
        gbcInfoPanel.anchor = GridBagConstraints.WEST;
        gbInfoPanel.setConstraints( lbLbl_entityID, gbcInfoPanel );
        add( lbLbl_entityID );

        lbLbl_Attributes = new JLabel( "Attributes"  );
        gbcInfoPanel.gridx = 0;
        gbcInfoPanel.gridy = 7;
        gbcInfoPanel.gridwidth = 4;
        gbcInfoPanel.gridheight = 1;
        gbcInfoPanel.fill = GridBagConstraints.BOTH;
        gbcInfoPanel.weightx = 1;
        gbcInfoPanel.weighty = 0;
        gbcInfoPanel.anchor = GridBagConstraints.WEST;
        gbInfoPanel.setConstraints( lbLbl_Attributes, gbcInfoPanel );
        add( lbLbl_Attributes );
    }

    /**
     */
    public void actionPerformed( ActionEvent e ) {
    }


    public JTextField getTfEntityType() {
        return tfEntityType;
    }

    public void setTfEntityType(JTextField tfEntityType) {
        this.tfEntityType = tfEntityType;
    }

    public JTextField getTfEntityID() {
        return tfEntityID;
    }

    public void setTfEntityID(JTextField tfEntityID) {
        this.tfEntityID = tfEntityID;
    }

    public JTextArea getTaAttributes() {
        return taAttributes;
    }

    public void setTaAttributes(JTextArea taAttributes) {
        this.taAttributes = taAttributes;
    }
}


