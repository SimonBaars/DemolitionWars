package com.demolitionwars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.demolitionwars.screens.GameScreen;

/**
 * Main game class for DemolitionWars (libGDX version)
 * 
 * This is a port of the original Android game to libGDX, utilizing:
 * - Box2D physics engine for realistic collisions and explosions
 * - Efficient sprite batch rendering
 * - Better performance and cross-platform support
 * 
 * Original game concept: Strategic conquest game where you use monetary
 * resources to destroy blocks and defeat the opposing king.
 */
public class DemolitionWarsGame extends Game {
    
    public static final String TITLE = "Demolition Wars";
    public static final String VERSION = "2.0-libgdx";
    
    // Game constants (from original game)
    public static final float WORLD_WIDTH = 30000f;
    public static final float WORLD_HEIGHT = 4000f;
    public static final float TILE_SIZE = 64f;
    public static final int STARTING_MONEY = 1000;
    
    // Screen resolution
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
    
    @Override
    public void create() {
        Gdx.app.log("DemolitionWars", "Starting game version " + VERSION);
        
        // Initialize the main game screen
        setScreen(new GameScreen(this));
    }
    
    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1); // Sky blue background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render current screen
        super.render();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log("DemolitionWars", "Game disposed");
    }
}
