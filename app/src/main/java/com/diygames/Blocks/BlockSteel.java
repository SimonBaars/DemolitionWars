package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockSteel extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockSteel(DemolitionWars game){
        super(game);setSpriteId(5);
        name="Steel block";
        price = 150;
    }
}
