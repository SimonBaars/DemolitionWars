package com.diygames.Humans;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public abstract class Bot extends Human {

    int homeX;

    int currentHomeX;

    int negativeWalkDistance;

    int positiveWalkDistance;

    int gravityCheck=0;

    /**
     * Set all his variables
     * @param game
     * @param team
     * @param homeX
     * @param negativeWalkDistance
     * @param positiveWalkDistance
     */
    public Bot(DemolitionWars game, boolean team, int homeX, int negativeWalkDistance, int positiveWalkDistance){
        super(game, team);
        this.homeX=homeX;
        this.currentHomeX=homeX;
        this.negativeWalkDistance=negativeWalkDistance;
        this.positiveWalkDistance=positiveWalkDistance;
    }

    /**
     * Walk around the world
     */
	public void walk() {
        int momentumModifier = 270;
        if (getX() >= currentHomeX) {
            currentHomeX=homeX-negativeWalkDistance;
        } else if (getX() < currentHomeX) {
            momentumModifier = 90;
            currentHomeX=homeX+positiveWalkDistance;
        }
        if (momentumModifier == 90) {
            if (animationFrame > 3) {
                animationFrame -= 4;
                setFrameNumber(animationFrame);
            }
        } else {
            if (animationFrame < 4) {
                animationFrame += 4;
                setFrameNumber(animationFrame);
            }
        }
        addToMomentum(momentumModifier, 10);
    }

    /**
     * Check for gravity next x steps
     * @param x
     */
    public void checkGravityNextXSteps(int x){
        gravityCheck=x;
    }

    /**
     * It'll walk, and check for gravity here if needed.
     */
    public void update(){
        super.update();
        walk();
        if(gravityCheck>0){
            gravity();
            gravityCheck--;
        }
    }

    /**
     * He's lonely. Talk to him <3.
     */
    public abstract void interact();
}
