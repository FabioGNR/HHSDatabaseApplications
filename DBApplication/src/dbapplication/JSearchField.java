package dbapplication;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JTextField;

/**
 *
 * @author fabio
 */
public class JSearchField extends JTextField {
    private static final Image searchIcon = getIcon();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        // paint icon on top
        if(searchIcon != null) {
            int height = getSize().height;
            int size = height-8;
            g.drawImage(searchIcon, getSize().width-4-size, 4, size, size, null);
        }
    }
    
    private static Image getIcon() {
        File file;
        try {
            file = new File("searchIcon.png");
        }
        catch(Exception e)
        {
            return null;
        }      
        Image image;
        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {
            return null;
        }
        return image;
    }  
}
