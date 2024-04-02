package MatPatChase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferStrategy;

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
    private static final int PLAYER_START_X = 70;
    private static final int PLAYER_START_Y = 200;
    private static final int ENEMY_START_X = 600;
    private static final int ENEMY_START_Y = 400;

    // Directory containing game images
    private static String workingDirectory;
    
    //for game state
    private boolean isGameInProgress; //set to false => "game over"
    private GameState gameState;
    private boolean anyKeyPressed = false; //anyKeyPressed is to get out of game over state 
    protected JumpScarePanel jumpScarePanel; //my game over state visually is a JPanel that is visible upon the game_over state being active
 
    //matpat and player
    private Player player; 
    private MatPat matPat;
    
    //images
    private Image janetImage;
    private Image matPatImage;
    private Image jumpscareImage;
    
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
        jumpscareImage = jumpscareIcon.getImage();        
        //final matthew patrick and player 
        this.player = new Player(janetImage, PLAYER_START_X, PLAYER_START_Y);
        this.matPat = new MatPat(matPatImage,ENEMY_START_X, ENEMY_START_Y);
        jumpScarePanel = new JumpScarePanel(jumpscareImage);
        add(jumpScarePanel);
        
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
        
        anyKeyPressed = true;
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
            

            // Pause to control the speed of animation
            try {
                Thread.sleep(50);
                switch (gameState) {
                case IN_PROGRESS:
                    if (isGameInProgress) {
                        // Update the positions of sprites
                        player.move();
                        matPat.move();

                        // Check for collisions
                        if (matPat.checkCollision(player)) {
                            // Handle collision
                        	System.out.println("Collided");
                            isGameInProgress = false;
                            gameState = GameState.JUMPSCARE;
                            updateGameState();
                            this.repaint();
                            break;
                        }
                    }
                    this.repaint();
                    break;

                case JUMPSCARE:
                    // Handle game over state
                	updateGameState();
                	if (!isGameInProgress) {
                    jumpScarePanel.setVisible(true);
                	}
                    break;
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Repaint the window
            this.repaint();
        }
    }

    
    public void updateGameState() {
		switch (gameState) {
		case IN_PROGRESS:
			//if the game is no longer in progress, you need to switch it to game_over
			if (!isGameInProgress) {
				gameState = GameState.JUMPSCARE;
				jumpScarePanel.setVisible(true);
			}
			break;
		case JUMPSCARE:
			//if you are in game over state and you press a key, it's confirming you wish to continue
//				resetGame(); 
//				isGameInProgress = true; //this boolean will be checked at the next run() loop
//				gameState = GameState.IN_PROGRESS;
//				anyKeyPressed = false; //resetting the flag
				//jumpScarePanel.setVisible(false); //hide the game over panel again
			break;
		}
	}
    
    public void resetGame() {
    	//reset player positions
    	player.setPosition(PLAYER_START_X, PLAYER_START_Y);
    	player.setPosition(ENEMY_START_X, ENEMY_START_Y);
    }

    // Main method to create and start the game application
    public static void main(String [] args){
        //SwingUtilities.invokeLater(() -> new Game());
        workingDirectory = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDirectory);
        Game test = new Game();
    }
}

