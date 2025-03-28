package com.diygames.Items;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;
import android.os.Handler;
import android.os.Looper;

import com.diygames.demolitionwars.DemolitionWars;

/**
 * Item that temporarily slows down game time
 * 
 * @author AI Assistant
 */
public class ItemTimeSlow extends Item implements IAlarm {
    
    private static final int SLOW_DURATION = 600; // 10 seconds at 60fps
    private transient Alarm durationAlarm;
    
    /**
     * Create a new time slow item
     * @param game Reference to main game
     */
    public ItemTimeSlow(DemolitionWars game) {
        super(game);
        name = "Time Slow";
        setSpriteId(10); // Use an appropriate sprite ID
        price = 1200;
        usesLeft = 1;
    }
    
    /**
     * Use the item to slow down game time
     */
    @Override
    public void use() {
        game.showMessage("Time slowed!");
        
        // Set slower frame throttling in the game
        game.setTimeScale(0.5f);
        
        // Setup alarm to restore normal speed after duration
        durationAlarm = new Alarm(0, SLOW_DURATION, this);
        durationAlarm.startAlarm();
        
        usesLeft--;
    }
    
    /**
     * Alarm handler for duration timeout
     */
    @Override
    public void triggerAlarm(int alarmNumber) {
        if (alarmNumber == 0) {
            // Restore normal speed
            game.setTimeScale(1.0f);
            game.showMessage("Time restored");
        }
    }
} 