package com.diygames.demolitionwars;

import android.gameengine.icadroids.dashboard.DashboardTextView;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.engine.Viewport;
import android.gameengine.icadroids.input.MotionSensor;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.diygames.Humans.Human;
import com.diygames.Humans.Player;

/**
 * Main class of the game 'Demolition Wars'.
 * 
 * @author Simon Baars
 */
public class DemolitionWars extends GameEngine {

    public World world;
	
	/**
	 * Dashboard for displaying the score
	 */
    private DashboardTextView moneyDisplay;
    private DashboardTextView inventoryDisplay;
    private DashboardTextView healthDisplay;
    private DashboardTextView shopDisplay;
    private DashboardTextView gameOverDisplay;
    private DashboardTextView menuDisplay;

    public String shopDisplayString = "";

    public final int tileSize = 64;
    
    // Screen size and scaling factors
    public int screenWidth = 1920;  // Default reference width
    public int screenHeight = 1080; // Default reference height
    public float scaleFactorX = 1.0f;
    public float scaleFactorY = 1.0f;
    public float spriteScaleFactor = 1.15f; // Make sprites 25% larger

    public SpriteLoader imageLoader = new SpriteLoader();

    public boolean gameOver = false;
    
    // For handling the start button
    private boolean startButtonPressed = false;
    
    // For menu navigation
    private boolean menuButtonPressed = false;
    private long lastMenuButtonPressTime = 0;
    private static final long MENU_BUTTON_DELAY = 250; // 250ms delay between menu navigation button presses

    // Game menu
    private GameMenu gameMenu;

    String gameOverString = "";
	
	/**
	 * Initialize the game
	 */
	@Override
	protected void initialize() {
        // Determine screen dimensions and scaling factors
        detectScreenSize();

		// Set up control mechanisms to use
		TouchInput.use = false;
		MotionSensor.use = false;
		OnScreenButtons.use = true;

        Viewport.useViewport=true;

        setMapDimensions(30000, 4000);

        //setBackground("bg.png");
		//createTileEnvironment();

        world = new World(this);
        gameMenu = new GameMenu(this);

        setPlayerPositionOnScreen(2,2);

        setPlayerPositionTolerance(100, 100);

        world.optimizeCollisionsForAllHumans();

        moneyDisplay = createNewDisplay(10,10, 18);

        inventoryDisplay = createNewDisplay(10,10, 18);

        healthDisplay = createNewDisplay(10,10, 18);

        shopDisplay = createNewDisplay(10,10, 18);

        gameOverDisplay = createNewDisplay((int)(700 * scaleFactorX), (int)(200 * scaleFactorY), 30);
        
        menuDisplay = createNewDisplay((int)(screenWidth/2 - 100), (int)(screenHeight/2 - 150), 24);
    }

    /**
     * Detect screen size and calculate scaling factors
     */
    private void detectScreenSize() {
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        
        // Calculate scaling factors based on the reference resolution of 1920x1080
        scaleFactorX = (float) screenWidth / 1920.0f;
        scaleFactorY = (float) screenHeight / 1080.0f;
        
        System.out.println("Screen size: " + screenWidth + "x" + screenHeight);
        System.out.println("Scale factors: X=" + scaleFactorX + ", Y=" + scaleFactorY);
    }

    /**
    * Creates a new dashboard by only giving an x, an y and a textSize
     */
    private DashboardTextView createNewDisplay(int x, int y, int textSize){
        DashboardTextView display = new DashboardTextView(this);
        display.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        display.setTextColor(Color.BLACK);
        addToDashboard(display);
        display.setWidgetX(x);
        display.setWidgetY(y);
        return display;
    }

    /**
    * When you call this method all objects disappear and it's GAME OVER
     */
    public void setGameOver(){
        for(MovingObject object : world.blocks){
            object.setVisibility(false);
        }
        world.humans.get(0).setVisibility(false);
        gameOver=true;
        gameOverString = "Game Over\n\nPress B to reset";
    }

    /**
    * This method is called when you win the game (yay)
     */
    public void setGameWon(){
        for(MovingObject object : world.blocks){
            object.setVisibility(false);
        }
        world.humans.get(0).setVisibility(false);
        gameOver=true;
        gameOverString = "You Won!!!\n\nPress B to reset";
    }
    
    /**
    * Reset the game to initial state
    */
    public void resetGame() {
        // Stop processing to avoid concurrent modification issues
        gameThread.stopRunning();
        
        // Clear all game objects from the engine
        for (GameObject obj : items.toArray(new GameObject[0])) {
            deleteGameObject(obj);
        }
        
        // Clear collections
        items.clear();
        newItems.clear();
        world.blocks.clear();
        world.humans.clear();
        
        // Create a new world
        world = new World(this);
        gameOver = false;
        
        // Set player object
        setPlayer(world.humans.get(0));
        
        // Make sure collisions are optimized
        world.optimizeCollisionsForAllHumans();
        
        // Clear any existing game menu
        if (gameMenu != null && gameMenu.isMenuOpen()) {
            gameMenu.closeMenu();
        }
        
        // Restart game thread
        startThread();
    }



	/**
	 * Update the game. At this moment, we only need to update the Dashboard.
	 * Note: the Dashboard settings will be adjusted!!!
	 */
	@Override
	public void update() {
        super.update();
        
        // Current time for button delay
        long currentTime = System.currentTimeMillis();
        
        // Handle start button press
        if (OnScreenButtons.start && !startButtonPressed) {
            gameMenu.toggleMenu();
            startButtonPressed = true;
        } else if (!OnScreenButtons.start) {
            startButtonPressed = false;
        }
        
        // Handle menu navigation when menu is open
        if (gameMenu.isMenuOpen()) {
            boolean buttonPressed = OnScreenButtons.dPadUp || OnScreenButtons.dPadDown || OnScreenButtons.buttonA;
            boolean timeDelayMet = currentTime - lastMenuButtonPressTime > MENU_BUTTON_DELAY;
            
            // Process button press if either the button wasn't pressed before, or enough time has passed
            if (buttonPressed && (!menuButtonPressed || timeDelayMet)) {
                if (OnScreenButtons.dPadUp) {
                    gameMenu.moveSelectionUp();
                    lastMenuButtonPressTime = currentTime;
                } else if (OnScreenButtons.dPadDown) {
                    gameMenu.moveSelectionDown();
                    lastMenuButtonPressTime = currentTime;
                } else if (OnScreenButtons.buttonA) {
                    gameMenu.selectOption();
                }
                menuButtonPressed = true;
            } else if (!buttonPressed) {
                menuButtonPressed = false;
            }
            
            // Display menu
            this.menuDisplay.setTextString(gameMenu.getMenuText());
            
            // Hide other displays
            this.moneyDisplay.setTextString("");
            this.inventoryDisplay.setTextString("");
            this.healthDisplay.setTextString("");
            this.shopDisplay.setTextString("");
            this.gameOverDisplay.setTextString("");
            
            return;
        } else {
            this.menuDisplay.setTextString("");
        }
        
        // Handle reset in game over screen
        if (gameOver && OnScreenButtons.buttonB) {
            resetGame();
            return;
        }
        
        Player player = (Player)world.humans.get(0);
        if(gameOver) {
            this.moneyDisplay.setTextString("");
            this.inventoryDisplay.setTextString("");
            this.healthDisplay.setTextString("");
            this.shopDisplay.setTextString("");
            this.gameOverDisplay.setTextString(gameOverString);
        } else {
            if (player.inShop) {
                this.moneyDisplay.setTextString("");
                this.inventoryDisplay.setTextString("");
                this.healthDisplay.setTextString("");
                this.shopDisplay.setTextString(shopDisplayString);
            } else {
                this.moneyDisplay.setTextString("Money: " + player.money + " - ");
                if (player.inventory.size() > 0) {
                    this.inventoryDisplay.setTextString("Selected Item: " + (player.selectedItem + 1) + ". " + player.inventory.get(player.selectedItem).name + " - ");
                } else {
                    this.inventoryDisplay.setTextString("Selected Item: none -");
                }
                this.healthDisplay.setTextString("Health: " + player.health + "%");
                this.shopDisplay.setTextString("");
            }
        }
    }
}