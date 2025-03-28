package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockSupernova extends BlockExplosive {

    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockSupernova(DemolitionWars game){
        super(game, 600, 100, 100, true, 0.0);
        name="Supernova";
        setSpriteId(26);
        price=5000;
    }

    /**
     * This explosive has no specific explosion. Just a very big one :P.
     */
    public void createExplosion(){

    }
}
