package MatPatChase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferStrategy;

enum GameState { //enum for game state
	IN_PROGRESS,
	JUMPSCARE
}

//the main class
public class Game extends JFrame implements Runnable, KeyListener {

    //define window size
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private static final Dimension WindowSize = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    
    //initial Starting locations
    private static final int PLAYER_START_X = 70;
    private static final int PLAYER_START_Y = 200;
    private static final int ENEMY_START_X = 600;
    private static final int ENEMY_START_Y = 400;

    //directory containing game images
    private static String workingDirectory;
    
    //for game state
    private boolean isGameInProgress; //set to false => "game over"
    private GameState gameState;
    protected JumpScarePanel jumpScarePanel; //my game over state visually is a JPanel that is visible upon the JUMPSCARE state being active
 
    //matpat and player objects
    private Player player; 
    private MatPat matPat;
    
    //images
    private Image janetImage;
    private Image matPatImage;
    private Image jumpscareImage;
    
    //set speed of player
    private static final int PLAYER_SPEED_LEFT = -10;
    private static final int PLAYER_SPEED_UP = -10;
    private static final int PLAYER_SPEED_DOWN = 10;
    private static final int PLAYER_SPEED_RIGHT = 10;
    private static final int PLAYER_SPEED_INITIAL = 0;

    //double buffering used to reduce flickering
    private BufferStrategy strategy;

    // Constructor
    public Game() {

        //add key listener to handle keyboard events
        addKeyListener(this);

        //create and set up the window
        this.setTitle("MatPat Chase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //display the window, centered on the screen
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        //double buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();    
        
        //load images
        ImageIcon matIcon = new ImageIcon(workingDirectory + "\\matPat.png");
        ImageIcon janetIcon = new ImageIcon(workingDirectory + "\\juiceBoxJanet.png");
        ImageIcon jumpscareIcon = new ImageIcon(workingDirectory + "\\jumpscare.png");
        
        janetImage = janetIcon.getImage();
        matPatImage = matIcon.getImage();
        jumpscareImage = jumpscareIcon.getImage();
        
        //jpane
        jumpScarePanel = new JumpScarePanel(jumpscareImage); //pass in jumpscare image to constructor
        jumpScarePanel.setBounds(0, 0, 800, 800);
        add(jumpScarePanel);
        
        //initialise matpat and player 
        this.player = new Player(janetImage, PLAYER_START_X, PLAYER_START_Y);
        this.matPat = new MatPat(matPatImage,ENEMY_START_X, ENEMY_START_Y);
       
        //set game state
        gameState = GameState.IN_PROGRESS;
        isGameInProgress = true;
        
        //start a new thread for run method
        Thread animationThread = new Thread(this);
        animationThread.start();
    } //end of constructor

    //keyboard events - Controls movement of the player
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

    //set speed back to initial value (0 as it is not moving)
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
            player.setXSpeed(0);
        else if (e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_DOWN)
            player.setYSpeed(PLAYER_SPEED_INITIAL);
    }

    //event handler for key typed
    public void keyTyped(KeyEvent e){
        //invoked when a key is typed
    }

    //paints the images on the screen
    @Override
    public void paint(Graphics g) {
        // Double Buffering
        g = strategy.getDrawGraphics();

        if(isGameInProgress) {
	        //you need to repaint background everytime to display changes properly (without it you get ghosting)
	        g.setColor(Color.LIGHT_GRAY);
	        g.fillRect(0, 0, WindowSize.width, WindowSize.height/2);
	
	        g.setColor(Color.BLUE);
	        g.fillRect(0, 300, WindowSize.width, WindowSize.height/2);
	       
	        //update to reflect movement
	        player.paint(g);
	        matPat.paint(g);
        }
        else {
        //if not game in progress then show the jpanel
		jumpScarePanel.paint(g);
        }
        //double buffering
        strategy.show();
    }

    //main game loop
    @Override
    public void run() {
        while (true) {
            //pause to control the speed of animation
            try {
                Thread.sleep(50);
                
                //game state
                switch (gameState) {
                case IN_PROGRESS:
                	updateGameState();
                    if (isGameInProgress) {
                        //update the positions of sprites
                    	player.move();
                    	matPat.move(player.x, player.y);

                        //check for collisions
                        if (matPat.checkCollision(player)) {
                            //handle collision
                            isGameInProgress = false; //you lose, game not in progress
                            updateGameState();
                            break;
                        }
                    }
                    break;

                case JUMPSCARE:
                	//nothing happens, game over
                    break;
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //repaint, reflect changes, implicitly calls paint
            this.repaint();
        }
    }

    //switches enum state
    public void updateGameState() {
        switch (gameState) {
            case IN_PROGRESS:
                if (!isGameInProgress) {
                    gameState = GameState.JUMPSCARE;
                    jumpScarePanel.setVisible(true);
                }
                break;
            case JUMPSCARE:
                //nothing needs to be implemented here
                break;
        }
    }

    //main method to create and start the game application
    public static void main(String [] args){
        SwingUtilities.invokeLater(() -> new Game());
        //this is a method of getting pathways of images without having to hardcode the full pathway
        workingDirectory = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDirectory);
        Game test = new Game();
    }
}

