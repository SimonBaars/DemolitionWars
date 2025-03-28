package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockNuke extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockNuke(DemolitionWars game){
        super(game, 300, 50.0, 50.0, false, 70.0);
        name="Nuke";
        setSpriteId(24);
        price = 2000;
    }

    /**
     * This explosive has no specific explosion
     */
    public void createExplosion(){

    }
}
