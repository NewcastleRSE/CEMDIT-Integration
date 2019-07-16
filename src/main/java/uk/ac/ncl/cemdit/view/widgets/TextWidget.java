package uk.ac.ncl.cemdit.view.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextWidget extends JPanel implements ActionListener {

    private TextField text;
    private String heading;
    private int columns = 30;

    public TextWidget(String heading, int columns) {
        super();
        this.columns = columns;
        init(heading, columns);
    }


    public TextWidget(String heading) {
        super();
        this.heading = heading;
        init(heading, this.columns);
    }

    private void init(String heading, int columns) {
        text = new TextField();
        Font font = getFont();
        int w = getFontMetrics(font).charWidth('w');
        text.setPreferredSize(new Dimension((w*columns), 75));
        text.setColumns(columns);
        setPreferredSize(new Dimension((w*columns), 75));
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(heading),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        getBorder()));
        text.setEditable(true);
        add(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public String getReturnString() {
        return text.getText();
    }

    public void setReturnString(String text) {
        this.text.setText(text);
    }
}
