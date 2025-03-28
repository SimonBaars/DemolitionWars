package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;

/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockWoodenPole extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockWoodenPole(DemolitionWars game){
        super(game);
        name="Wooden Pole";
        setSpriteId(10);
        price = 25;
    }
}
