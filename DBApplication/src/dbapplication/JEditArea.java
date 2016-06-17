package dbapplication;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextArea;

/**
 *
 * @author RLH
 */
public class JEditArea extends JTextArea{
    
    String placeHolder;
    
    public JEditArea(String placeHolderText){
        placeHolder = placeHolderText;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(getText().isEmpty()){
            g.setColor(Color.GRAY);
            g.drawString(placeHolder, 5, 20);
        }
    }
}
