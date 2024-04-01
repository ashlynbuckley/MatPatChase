package MatPatChase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import java.awt.image.*;
import java.util.ArrayList;

enum GameState {
	IN_PROGRESS,
	JUMPSCARE
}

// The main class representing the Space Invaders game application
public class Game extends JFrame implements Runnable, KeyListener {

    // Define window size
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private static final Dimension WindowSize = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    
    // Initial Starting locations
    private static final int PLAYER_START_X = 250;
    private static final int PLAYER_START_Y = 330;
    private static final int ENEMY_START_X = 500;
    private static final int ENEMY_START_Y = 330;

    // Directory containing game images
    private static String workingDirectory;
    
    //for game state
    private boolean isGameInProgress; //set to false => "game over"
    private GameState gameState;
    private boolean anyKeyPressed = false; //anyKeyPressed is to get out of game over state "press any key to continue"
    private JumpScarePanel jumpScarePanel; //my game over state visually is a JPanel that is visible upon the game_over state being active
    
    //matpat and player
    private Player player; 
    private MatPat matPat;
    
    //images
    private Image janetImage;
    private Image matPatImage;
    
    // Speed of player
    private static final int PLAYER_SPEED_LEFT = -10;
    private static final int PLAYER_SPEED_UP = -10;
    private static final int PLAYER_SPEED_DOWN = 10;
    private static final int PLAYER_SPEED_RIGHT = 10;
    private static final int PLAYER_SPEED_INITIAL = 0;

    // Double Buffering
    private BufferStrategy strategy;

    // Constructor
    public Game() {

        // Add key listener to handle keyboard events
        addKeyListener(this);

        // Create and set up the window
        this.setTitle("MatPat Chase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window, centered on the screen
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        // Double Buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();     
        
        //load images
        ImageIcon matIcon = new ImageIcon(workingDirectory + "\\matPat.png");
        ImageIcon janetIcon = new ImageIcon(workingDirectory + "\\juiceBoxJanet.png");
        ImageIcon jumpscareIcon = new ImageIcon(workingDirectory + "\\jumpscare.png");
        
        janetImage = janetIcon.getImage();
        matPatImage = matIcon.getImage();
        
        //final matthew patrick and player 
        Player player = new Player(janetImage, PLAYER_START_X, PLAYER_START_Y);
        MatPat matPat = new MatPat(matPatImage,ENEMY_START_X, ENEMY_START_Y);
        
        //set game state
        gameState = GameState.IN_PROGRESS;
        isGameInProgress = true;
              
        // Start a new thread for animation
        Thread animationThread = new Thread(this);
        animationThread.start();
    }

    // Keyboard events - Controls movement of the player
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT)
            player.setXSpeed(PLAYER_SPEED_LEFT);
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
            player.setXSpeed(PLAYER_SPEED_RIGHT);
        else if (e.getKeyCode()==KeyEvent.VK_UP)
            player.setYSpeed(PLAYER_SPEED_UP);
        else if (e.getKeyCode()==KeyEvent.VK_DOWN)
            player.setYSpeed(PLAYER_SPEED_DOWN);
    }

    // Set speed back to initial value (0 as it is not moving)
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
            player.setXSpeed(0);
        else if (e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_DOWN)
            player.setYSpeed(PLAYER_SPEED_INITIAL);
    }

    // Event handler for key typed
    public void keyTyped(KeyEvent e){
        // Invoked when a key is typed
    }

    // Paints the images on the screen
    @Override
    public void paint(Graphics g) {
        // Double Buffering
        g = strategy.getDrawGraphics();

        // Clear the screen
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height/2);

        g.setColor(Color.BLUE);
        g.fillRect(0, 300, WindowSize.width, WindowSize.height/2);

        player.paint(g);
        matPat.paint(g);
        // Double buffering
        strategy.show();
    }

    // Main game loop
    @Override
    public void run() {
        while (true) {
           // Update the positions of sprites
            player.move();
            matPat.move();

            // Pause to control the speed of animation
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Repaint the window
            this.repaint();
        }
    }


    // Main method to create and start the game application
    public static void main(String [] args){
        SwingUtilities.invokeLater(() -> new Game());

    }
}

