package com.diygames.demolitionwars;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Game menu class that handles menu options when start button is pressed
 */
public class GameMenu implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Menu states
    public static final int MENU_CLOSED = 0;
    public static final int MENU_OPEN = 1;
    
    // Menu options
    public static final int OPTION_SAVE = 0;
    public static final int OPTION_LOAD = 1;
    public static final int OPTION_RESET = 2;
    public static final int OPTION_EXIT = 3;
    
    // Current menu state
    private int menuState = MENU_CLOSED;
    
    // Currently selected option
    private int selectedOption = 0;
    
    // Reference to main game
    private transient DemolitionWars game;
    
    // Menu options array
    private String[] menuOptions = {"Save", "Load", "Reset", "Exit"};
    
    /**
     * Create a new game menu
     * @param game Reference to main game
     */
    public GameMenu(DemolitionWars game) {
        this.game = game;
    }
    
    /**
     * Toggle menu state (open/close)
     */
    public void toggleMenu() {
        if (menuState == MENU_CLOSED) {
            openMenu();
        } else {
            closeMenu();
        }
    }
    
    /**
     * Open the menu
     */
    public void openMenu() {
        menuState = MENU_OPEN;
        selectedOption = 0;
    }
    
    /**
     * Close the menu
     */
    public void closeMenu() {
        menuState = MENU_CLOSED;
    }
    
    /**
     * Check if menu is open
     * @return true if menu is open
     */
    public boolean isMenuOpen() {
        return menuState == MENU_OPEN;
    }
    
    /**
     * Move selection up
     */
    public void moveSelectionUp() {
        if (menuState == MENU_OPEN) {
            selectedOption--;
            if (selectedOption < 0) {
                selectedOption = menuOptions.length - 1;
            }
        }
    }
    
    /**
     * Move selection down
     */
    public void moveSelectionDown() {
        if (menuState == MENU_OPEN) {
            selectedOption++;
            if (selectedOption >= menuOptions.length) {
                selectedOption = 0;
            }
        }
    }
    
    /**
     * Get the menu text to display
     * @return Menu text
     */
    public String getMenuText() {
        if (menuState == MENU_CLOSED) {
            return "";
        }
        
        StringBuilder menuText = new StringBuilder("MENU\n\n");
        
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                menuText.append("> ");
            } else {
                menuText.append("  ");
            }
            menuText.append(menuOptions[i]).append("\n");
        }
        
        return menuText.toString();
    }
    
    /**
     * Handle option selection
     */
    public void selectOption() {
        if (menuState == MENU_CLOSED) {
            return;
        }
        
        switch (selectedOption) {
            case OPTION_SAVE:
                saveGame();
                break;
            case OPTION_LOAD:
                loadGame();
                break;
            case OPTION_RESET:
                resetGame();
                break;
            case OPTION_EXIT:
                exitGame();
                break;
        }
    }
    
    /**
     * Save the current game state
     */
    private void saveGame() {
        try {
            FileOutputStream fos = new FileOutputStream(game.getFileStreamPath("savegame.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game.world);
            oos.close();
            fos.close();
            closeMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load a saved game
     */
    private void loadGame() {
        try {
            FileInputStream fis = new FileInputStream(game.getFileStreamPath("savegame.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            game.world = (World) ois.readObject();
            game.world.game = game; // Restore transient reference
            ois.close();
            fis.close();
            closeMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Reset the game to initial state
     */
    private void resetGame() {
        game.world = new World(game);
        game.gameOver = false;
        closeMenu();
    }
    
    /**
     * Exit the game
     */
    private void exitGame() {
        game.finish();
    }
} 