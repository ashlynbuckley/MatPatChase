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
            // Load the jumpscare image from file
            jumpScareImage = image;
        
    }
	
	 @Override
	    public void paintComponent(Graphics g) {
		 System.out.println("Printed inside jumpscare class!!");
	        super.paintComponent(g);
	        setBackground(Color.BLACK);

	        // Draw the jumpscare image in the center of the panel
	        if (jumpScareImage != null) {
	            int x = 400;
	            int y = 400;
	            g.drawImage(jumpScareImage, x, y, this);
	        }
	    }
	 
	 @Override
	    public Dimension getPreferredSize() {
	        if (jumpScareImage != null) {
	            return new Dimension(400, 400);
	        }
	        return super.getPreferredSize();
	    }
	
}
