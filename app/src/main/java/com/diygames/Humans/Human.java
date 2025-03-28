package com.diygames.Humans;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;

import com.diygames.demolitionwars.MovingObject;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Base class for all human characters in the game.
 * 
 * @author Simon Baars
 */
public class Human extends MovingObject implements IAlarm, Serializable {

	private static final long serialVersionUID = 1L;

	// Team affiliation (true/false for different teams)
	public boolean team;

	// Inventory of items the human is carrying
	public ArrayList<Obtainables> inventory = new ArrayList<Obtainables>();

	// Health points (0-100)
	public int health = 100;

    // Current animation frame
    public int animationFrame = 0;
    
    // Animation direction (0-3 for right, 4-7 for left)
    protected int animationDirection = 0;

    // Alarm for animation timing (transient - not serialized)
    protected transient Alarm myAlarm;

    // Walking state
    protected boolean isWalking;
    
    // Optimization: Last time collision was optimized
    private transient long lastCollisionOptTime = 0;
    
    // How often to check if collision optimization is needed (milliseconds)
    private static final long COLLISION_CHECK_INTERVAL = 500;

    /**
     * Create a new human character
     * @param game Reference to main game
     * @param team Team affiliation
     */
    public Human(DemolitionWars game, boolean team) {
        super(game);
        
        if (game == null) {
            throw new IllegalArgumentException("Game reference cannot be null");
        }
        
        this.team = team;
        setDirection(90);
        setFriction(0.05);
        initAlarm();
    }
    
    /**
     * Initialize alarm - called in constructor and after deserialization
     */
    public void initAlarm() {
        if (myAlarm != null) {
            myAlarm.pauseAlarm();
        }
        
        myAlarm = new Alarm(0, 6, this);
        myAlarm.startAlarm();
    }

    /**
     * Handle alarm triggers for animations
     * @param alarmNumber The alarm that was triggered
     */
    @Override
    public void triggerAlarm(int alarmNumber) {
        if (game == null) return;
        
        switch (alarmNumber) {
            case 0: // Animation alarm
                handleAnimationAlarm();
                break;
            case 1: // Jump alarm
                if (inAir >= 0) {
                    inAir++;
                }
                break;
            default:
                // Unknown alarm - ignore
                break;
        }
    }
    
    /**
     * Set the animation direction (left or right)
     * @param isRightDirection true for right, false for left
     */
    public void setAnimationDirection(boolean isRightDirection) {
        // Keep the current frame number within its set (0-3 or 4-7)
        int frameOffset = animationFrame % 4;
        
        if (isRightDirection) {
            // Right-facing animation uses frames 0-3
            animationDirection = 0;
            animationFrame = frameOffset;
        } else {
            // Left-facing animation uses frames 4-7
            animationDirection = 4;
            animationFrame = frameOffset + 4;
        }
        
        setFrameNumber(animationFrame);
    }
    
    /**
     * Handle animation timing alarm
     */
    private void handleAnimationAlarm() {
        if (inAir != 1 && isWalking) {
            // Get the base frame depending on direction (0 for right, 4 for left)
            int baseFrame = animationDirection;
            
            // Calculate the next frame offset (0, 1, 2, 3)
            int frameOffset = (animationFrame % 4);
            frameOffset = (frameOffset + 1) % 4;
            
            // Set the new animation frame
            animationFrame = baseFrame + frameOffset;
            setFrameNumber(animationFrame);
        }
        
        // Restart alarm if it exists
        if (myAlarm != null) {
            myAlarm.restartAlarm();
        }
    }

    /**
     * Update human state each frame with performance optimizations
     */
    @Override
    public void update() {
        // Call parent update
        super.update();
        
        // Skip if game reference is missing
        if (game == null) return;
        
        // Only check collision optimization periodically for performance
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCollisionOptTime > COLLISION_CHECK_INTERVAL) {
            lastCollisionOptTime = currentTime;
            checkCollisionOptimization();
        }
    }
    
    /**
     * Check if collision optimization is needed based on movement
     */
    private void checkCollisionOptimization() {
        // Safety check for last position
        if (lastCollisionScanPosition == null) {
            optimizeCollisions();
            return;
        }
        
        // Check if moved far enough to require reoptimization
        int tileSize = (game != null) ? game.tileSize : 64;
        int threshold = 12 * tileSize;
        
        if (Math.abs(getX() - lastCollisionScanPosition[0]) > threshold || 
            Math.abs(getY() - lastCollisionScanPosition[1]) > threshold) {
            optimizeCollisions();
        }
    }
    
    /**
     * Take damage and check if dead
     * @param damage Amount of damage to take
     * @return true if human died from this damage
     */
    public boolean takeDamage(int damage) {
        health -= damage;
        
        // Ensure health doesn't go below 0
        if (health < 0) {
            health = 0;
        }
        
        return health <= 0;
    }
    
    /**
     * Check if human is dead
     * @return true if human has no health left
     */
    public boolean isDead() {
        return health <= 0;
    }
}
