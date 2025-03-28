package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockStone extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockStone(DemolitionWars game){
        super(game);
        name="Stone";
        setSpriteId(7);
        price=80;
    }
}
