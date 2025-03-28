package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockPlanks extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockPlanks(DemolitionWars game){
        super(game);
        name="Planks";
        setSpriteId(4);
        price=80;
    }
}
