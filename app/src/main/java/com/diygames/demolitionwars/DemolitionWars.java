package com.diygames.demolitionwars;

import android.gameengine.icadroids.dashboard.DashboardTextView;
import android.gameengine.icadroids.engine.GameEngine;
import android.gameengine.icadroids.engine.Viewport;
import android.gameengine.icadroids.input.MotionSensor;
import android.gameengine.icadroids.input.OnScreenButtons;
import android.gameengine.icadroids.input.TouchInput;
import android.gameengine.icadroids.objects.GameObject;
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
    // Game world and state
    public World world;
    public boolean gameOver = false;
    public String gameOverString = "";
    
    // Game constants
    public static final int TILE_SIZE = 64;
    public static final int DEFAULT_SCREEN_WIDTH = 1920;
    public static final int DEFAULT_SCREEN_HEIGHT = 1080;
    public static final int MAP_WIDTH = 30000;
    public static final int MAP_HEIGHT = 4000;
    private static final int MENU_BUTTON_DELAY = 250; // 250ms delay between menu navigation
	
    // For backward compatibility with existing code
    public final int tileSize = TILE_SIZE;
    
	// UI elements
    private DashboardTextView moneyDisplay;
    private DashboardTextView inventoryDisplay;
    private DashboardTextView healthDisplay;
    private DashboardTextView shopDisplay;
    private DashboardTextView gameOverDisplay;
    private DashboardTextView menuDisplay;

    // Game state
    public String shopDisplayString = "";
    public SpriteLoader imageLoader = new SpriteLoader();
    
    // Screen size and scaling factors
    public int screenWidth = DEFAULT_SCREEN_WIDTH;
    public int screenHeight = DEFAULT_SCREEN_HEIGHT;
    public float scaleFactorX = 1.0f;
    public float scaleFactorY = 1.0f;
    public float spriteScaleFactor = 1.15f; // Make sprites larger
    
    // Input handling
    private boolean startButtonPressed = false;
    private boolean menuButtonPressed = false;
    private long lastMenuButtonPressTime = 0;

    // Game menu
    private GameMenu gameMenu;
	
	/**
	 * Initialize the game
	 */
	@Override
	protected void initialize() {
        // Determine screen dimensions and scaling factors
        detectScreenSize();

		// Set up control mechanisms
		configureInput();

        // Configure viewport
        Viewport.useViewport = true;
        setMapDimensions(MAP_WIDTH, MAP_HEIGHT);

        // Create game world
        initializeWorld();

        // Create UI elements
        createDashboards();
    }

    /**
     * Configure input methods
     */
    private void configureInput() {
        TouchInput.use = false;
        MotionSensor.use = false;
        OnScreenButtons.use = true;
    }
    
    /**
     * Initialize the game world
     */
    private void initializeWorld() {
        world = new World(this);
        gameMenu = new GameMenu(this);

        setPlayerPositionOnScreen(2, 2);
        setPlayerPositionTolerance(100, 100);
        world.optimizeCollisionsForAllHumans();
    }
    
    /**
     * Create all dashboard UI elements
     */
    private void createDashboards() {
        // Game info displays
        moneyDisplay = createNewDisplay(10, 10, 18);
        inventoryDisplay = createNewDisplay(10, 10, 18);
        healthDisplay = createNewDisplay(10, 10, 18);
        shopDisplay = createNewDisplay(10, 10, 18);
        
        // Centered displays
        int centerX = (int)(screenWidth / 2 - 100);
        int centerY = (int)(screenHeight / 2 - 150);
        gameOverDisplay = createNewDisplay((int)(700 * scaleFactorX), (int)(200 * scaleFactorY), 30);
        menuDisplay = createNewDisplay(centerX, centerY, 24);
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
        
        // Calculate scaling factors based on the reference resolution
        scaleFactorX = (float) screenWidth / DEFAULT_SCREEN_WIDTH;
        scaleFactorY = (float) screenHeight / DEFAULT_SCREEN_HEIGHT;
        
        System.out.println("Screen size: " + screenWidth + "x" + screenHeight);
        System.out.println("Scale factors: X=" + scaleFactorX + ", Y=" + scaleFactorY);
    }

    /**
     * Creates a new dashboard by only giving an x, an y and a textSize
     */
    private DashboardTextView createNewDisplay(int x, int y, int textSize) {
        DashboardTextView display = new DashboardTextView(this);
        display.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        display.setTextColor(Color.BLACK);
        addToDashboard(display);
        display.setWidgetX(x);
        display.setWidgetY(y);
        return display;
    }

    /**
     * Set game over state with proper message
     */
    public void setGameOver() {
        hideAllObjects();
        gameOver = true;
        gameOverString = "Game Over\n\nPress B to reset";
    }

    /**
     * Set game won state with proper message
     */
    public void setGameWon() {
        hideAllObjects();
        gameOver = true;
        gameOverString = "You Won!!!\n\nPress B to reset";
    }
    
    /**
     * Hide all game objects (used for game over or win)
     */
    private void hideAllObjects() {
        for (MovingObject object : world.blocks) {
            object.setVisibility(false);
        }
        
        if (!world.humans.isEmpty()) {
            world.humans.get(0).setVisibility(false);
        }
    }
    
    /**
     * Reset the game to initial state
     */
    public void resetGame() {
        // Stop processing temporarily
        pause();
        
        // Clear all game objects
        clearAllGameObjects();
        
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
        
        // Restart game processing
        resume();
    }
    
    /**
     * Clear all game objects and collections
     */
    private void clearAllGameObjects() {
        // Clear all game objects from the engine
        for (GameObject obj : items.toArray(new GameObject[0])) {
            deleteGameObject(obj);
        }
        
        // Clear collections
        items.clear();
        newItems.clear();
        world.blocks.clear();
        world.humans.clear();
    }

	/**
	 * Update game state and UI each frame
	 */
	@Override
	public void update() {
        super.update();
        
        // Handle menu button
        handleMenuButton();
        
        // Handle menu navigation
        if (gameMenu.isMenuOpen()) {
            handleMenuNavigation();
            return;
        }
        
        // Handle game over reset
        if (gameOver && OnScreenButtons.buttonB) {
            resetGame();
            return;
        }
        
        // Update UI displays
        updateDisplays();
    }
    
    /**
     * Handle the start button press to toggle menu
     */
    private void handleMenuButton() {
        if (OnScreenButtons.start && !startButtonPressed) {
            gameMenu.toggleMenu();
            startButtonPressed = true;
        } else if (!OnScreenButtons.start) {
            startButtonPressed = false;
        }
    }
    
    /**
     * Handle menu navigation with controlled input rate
     */
    private void handleMenuNavigation() {
        long currentTime = System.currentTimeMillis();
        boolean buttonPressed = OnScreenButtons.dPadUp || OnScreenButtons.dPadDown || OnScreenButtons.buttonA;
        boolean timeDelayMet = currentTime - lastMenuButtonPressTime > MENU_BUTTON_DELAY;
        
        // Process button press with rate limiting
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
        
        // Display menu and hide other displays
        displayMenu();
    }
    
    /**
     * Display menu and hide other UI elements
     */
    private void displayMenu() {
        menuDisplay.setTextString(gameMenu.getMenuText());
        
        // Hide other displays
        moneyDisplay.setTextString("");
        inventoryDisplay.setTextString("");
        healthDisplay.setTextString("");
        shopDisplay.setTextString("");
        gameOverDisplay.setTextString("");
    }
    
    /**
     * Update all UI displays based on game state
     */
    private void updateDisplays() {
        // Clear menu text
        menuDisplay.setTextString("");
        
        if (gameOver) {
            displayGameOver();
            return;
        }
        
        // Get player for display info
        Player player = (Player)world.humans.get(0);
        
        if (player.inShop) {
            displayShopInfo();
        } else {
            displayGameInfo(player);
        }
    }
    
    /**
     * Display game over message
     */
    private void displayGameOver() {
        moneyDisplay.setTextString("");
        inventoryDisplay.setTextString("");
        healthDisplay.setTextString("");
        shopDisplay.setTextString("");
        gameOverDisplay.setTextString(gameOverString);
    }
    
    /**
     * Display shop information
     */
    private void displayShopInfo() {
        moneyDisplay.setTextString("");
        inventoryDisplay.setTextString("");
        healthDisplay.setTextString("");
        shopDisplay.setTextString(shopDisplayString);
    }
    
    /**
     * Display in-game information about player state
     */
    private void displayGameInfo(Player player) {
        moneyDisplay.setTextString("Money: " + player.money + " - ");
        
        if (player.inventory.size() > 0) {
            inventoryDisplay.setTextString("Selected Item: " + (player.selectedItem + 1) + ". " + 
                                          player.inventory.get(player.selectedItem).name + " - ");
        } else {
            inventoryDisplay.setTextString("Selected Item: none -");
        }
        
        healthDisplay.setTextString("Health: " + player.health + "%");
        shopDisplay.setTextString("");
    }
}