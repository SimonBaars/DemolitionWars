package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockBrick extends BlockLandscape {

    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockBrick(DemolitionWars game){
        super(game);
        name="Bricks";
        setSpriteId(1);
        price = 75;
    }
}
