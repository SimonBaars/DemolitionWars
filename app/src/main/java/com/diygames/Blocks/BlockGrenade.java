package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockGrenade extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockGrenade(DemolitionWars game){
        super(game, 50, 4, 20, false, 100.0);
        name="Grenade";
        setSpriteId(21);
        price = 250;
    }

    /**
     * Throw the grenade into the world
     * @param throwingStrength
     */
    public void throwIntoTheWorld(int throwingStrength){
        optimizeCollisions();
        int playerPosition=45;
        if (game.world.humans.get(0).animationFrame >3) {
            playerPosition=315;
        }
        addXTimesToMomentum(15, playerPosition, throwingStrength);
    }

    /**
     * Launch the grenade into the world with a grenade launcher :D
     * @param launchStrength
     * @param direction
     */
    public void launchIntoTheWorld(int launchStrength, boolean direction){
        optimizeCollisions();
        int playerPosition=80;
        if (direction) {
            playerPosition=280;
        }
        addXTimesToMomentum(15, playerPosition, launchStrength);
    }

    /**
     * This bomb has no specific explosion
     */
    public void createExplosion(){

    }
}
