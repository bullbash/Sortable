package ui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import main.Globals;

public class ImagePanel extends JPanel{
                        // ================ paintComponent =====================
    protected void paintComponent(Graphics g){
        if(Globals.distributionImage != null){
            g.drawImage(Globals.distributionImage, 0, 0, getWidth(), getHeight(), null);
            g.setColor(Color.white);
            g.drawString(""+Globals.patterns[0].pattern.length,                          10,               Globals.imagePanelHeight - 10);
            g.drawString(""+Globals.patterns[Globals.patterns.length - 1].pattern.length, getWidth() - 20, Globals.imagePanelHeight - 10);
        }
        else{
            g.drawString("Distribution will be shown when product processing is finished", 20, Globals.imagePanelHeight/2);
        }
    }

}
