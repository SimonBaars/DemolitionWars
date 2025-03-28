package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockDirt extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockDirt(DemolitionWars game){
        super(game);
        name="Dirt";
        setSpriteId(2);
        price = 50;
    }
}
