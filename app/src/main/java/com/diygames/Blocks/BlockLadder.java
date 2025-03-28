package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;

/**
 * Created by Simon Baars on 3/26/2015.
 */
public class BlockLadder extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockLadder(DemolitionWars game, boolean ladderType){
        super(game);
        setSpriteId(17 + ((ladderType) ? 1 : 0));
        name="Ladder";
        price = 100;
    }
}
