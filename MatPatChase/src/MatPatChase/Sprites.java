package MatPatChase;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class Sprites{
    // Fields, x and y being the position of a sprite
    protected int x, y;
    protected int xSpeed = 0;
    protected int ySpeed = 0;
    protected int width, height;
    protected Image gameImage;
    protected int winWidth;

    // Constructor
    public Sprites(Image i, int xx, int yy) {
        x = xx;
        y = yy;
        // gameImage will be used to paint other objects of type Sprite2D
        gameImage = i;
    }


    // Method to move the sprite
    public void moveEnemy() {

        // Generate random movement
        Random random = new Random();
        int moveX = random.nextInt(7) - 3; // Random value
        int moveY = random.nextInt(7) - 3; // Random value

        // Update the position
        x += moveX;
        y += moveY;
    }

    // Method to move the sprite
    public void move(){
        x += xSpeed;
        y += ySpeed;
    }

    public void setXSpeed( int x ) {
        xSpeed=x;
    }

    public void setYSpeed( int y ) {
        ySpeed=y;
    }

    // Method to paint the sprites on the graphics context
    public void paint(Graphics gr) {
        gr.drawImage(gameImage, x, y, null);
    }
}
