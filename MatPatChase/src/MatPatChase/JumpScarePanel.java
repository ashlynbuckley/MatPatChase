package MatPatChase;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class JumpScarePanel extends JPanel {
	private Image jumpScareImage;
	//jPanel overlay for game.java that allows me to display jumpscare
	
	public JumpScarePanel(Image image) {
            //load the jumpscare image from file
            jumpScareImage = image;
            
    }
	
	@Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        //fillrect used to fill the full window
        g.fillRect(0, 0, 1000, 1000);
        
        //draw the jumpscare image in the center of the panel
        if (jumpScareImage != null) {
            int x = (getWidth() - jumpScareImage.getWidth(this)) / 2;
            int y = (getHeight() - jumpScareImage.getHeight(this)) / 2;
            g.drawImage(jumpScareImage, x, y, this);
        } else {
            System.out.println("This image is null"); //used to debug
        }
    }
}
