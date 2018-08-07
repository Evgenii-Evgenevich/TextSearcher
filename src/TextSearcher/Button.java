package TextSearcher;


import javax.swing.*;
import java.awt.event.ActionListener;

public class Button extends JButton {

    public Button(String title, ActionListener actionListener) {
        super(title);
        this.addActionListener(actionListener);
    }
}
