package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockGrass extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockGrass(DemolitionWars game){
        super(game);
        name="Grass";
        setSpriteId(3);
        price=55;
    }
}
