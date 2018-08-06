package TextSearcher;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SearchPanel extends JPanel {
    private final JTextField whereField;
    private final JTextField filterField;
    private final JTextField whatField;

    public SearchPanel() {
        super(new GridLayout(3,2));

        this.add(new JLabel("What: ", SwingConstants.RIGHT));
        whatField = new JTextField(); this.add(whatField);
        this.add(new JLabel("Filter: ", SwingConstants.RIGHT));
        filterField = new JTextField(); this.add(filterField);
        this.add(new JLabel("Directory: ", SwingConstants.RIGHT));
        whereField = new JTextField(); this.add(whereField);
    }

    public SearchPanel(String what, String filter, String directory) {
        super(new GridLayout(3,2));

        this.add(new JLabel("What: ", SwingConstants.RIGHT));
        whatField = new JTextField(what); this.add(whatField);
        this.add(new JLabel("Filter: ", SwingConstants.RIGHT));
        filterField = new JTextField(filter); this.add(filterField);
        this.add(new JLabel("Directory: ", SwingConstants.RIGHT));
        whereField = new JTextField(directory); this.add(whereField);
    }

    public String getFilterValue() {
        return filterField.getText();
    }

    public String getWhatValue() {
        return whatField.getText();
    }

    public String getWhereValue() {
        return whereField.getText();
    }

    public void setFilterValue(String s) {
        filterField.setText(s);
    }

    public void setWhatValue(String s) {
        whatField.setText(s);
    }

    public void setWhereValue(String s) {
        whereField.setText(s);
    }
}
