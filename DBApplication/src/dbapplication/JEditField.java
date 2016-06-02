package dbapplication;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextField;

/**
 *
 * @author fabio
 */
public class JEditField extends JTextField {
    String placeholder;
    public JEditField(String placeholderText) {
        placeholder = placeholderText;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // als de invoer leeg is, laat dan placeholder tekst zien
        if(getText().isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString(placeholder, 5, 20);
        }
    }
    
    
    
}
