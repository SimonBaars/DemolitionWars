package com.diygames.Humans;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;

import com.diygames.demolitionwars.MovingObject;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;

import java.util.ArrayList;

/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Human extends MovingObject implements IAlarm{

	public boolean team;

	public ArrayList<Obtainables> inventory = new ArrayList<Obtainables>();

	public int health = 100;

    public int animationFrame=0;

    protected Alarm myAlarm;

    protected boolean isWalking;

    /**
     * Set human to walk
     * @param game
     * @param team
     */
    public Human(DemolitionWars game, boolean team){
        super(game);
        this.team=team;
        setDirection(90);
        setFriction(0.05);
        myAlarm = new Alarm(0, 6, this);
        myAlarm.startAlarm();
    }

    /**
     * Change his walking feet n stuff. If it's a player you can change it's inAir state.
     * @param alarmNumber
     */
    public void triggerAlarm(int alarmNumber){
            switch(alarmNumber){
                case 0:
                    if(inAir!=1 && isWalking) {
                        animationFrame++;
                        if (animationFrame == 4) {
                            animationFrame = 0;
                        } else if (animationFrame == 8) {
                            animationFrame = 4;
                        }
                        setFrameNumber(animationFrame);
                    }
                    myAlarm.restartAlarm();
                    break;
                case 1:
                    inAir++;
                    break;
            }
    }

    /**
     * Optimize collsions if needed
     */
    public void update(){
        super.update();
        if(getX()<lastCollisionScanPosition[0]-(12*game.tileSize) || getX()>lastCollisionScanPosition[0]+(12*game.tileSize) || getY()<lastCollisionScanPosition[1]-(12*game.tileSize) || getY()>lastCollisionScanPosition[1]+(12*game.tileSize)){
            optimizeCollisions();
        }
    }
}
