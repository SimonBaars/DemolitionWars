package com.diygames.Blocks;


import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Block extends Obtainables {
    /**
     * Initialize the block
     * @param game
     */
    public Block(DemolitionWars game){
        super(game);
    }

    /**
     * Place the block and if it's an explosive set the explosion timer
     */
    public void use() {
        game.world.blocks.add(this);
        game.world.humans.get(0).nearbyObjects.add(this);
        int playerPosition=1;
        if (game.world.humans.get(0).animationFrame >3) {
            playerPosition=-1;
        }
        game.addGameObject(game.world.blocks.get(game.world.blocks.size() - 1), game.world.humans.get(0).getX() + (playerPosition * (3 * game.tileSize)), game.world.humans.get(0).getY() + (3 * game.tileSize));
        if(this instanceof BlockExplosive){
            BlockExplosive thisExplosive = (BlockExplosive)this;
            thisExplosive.setExplosionTimer();
        }
	}
}
