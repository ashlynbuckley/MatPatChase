package MatPatChase;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class JumpScarePanel extends JPanel {
	private BufferedImage jumpScareImage;
	 private String gameOverText = "You Died!";
	//jPanel overlay for game.java that allows me to display jumpscare
	
	public JumpScarePanel() {
        try {
            // Load the jumpscare image from file
            jumpScareImage = ImageIO.read(new File("jumpscare.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	 @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        setBackground(Color.BLACK);

	        // Draw the jumpscare image in the center of the panel
	        if (jumpScareImage != null) {
	            int x = (getWidth() - jumpScareImage.getWidth()) / 2;
	            int y = (getHeight() - jumpScareImage.getHeight()) / 2;
	            g.drawImage(jumpScareImage, x, y, this);
	        }
	    }
	 
	 @Override
	    public Dimension getPreferredSize() {
	        if (jumpScareImage != null) {
	            return new Dimension(jumpScareImage.getWidth(), jumpScareImage.getHeight());
	        }
	        return super.getPreferredSize();
	    }
	
}
