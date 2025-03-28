package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockUnbreakableRock extends BlockLandscape {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockUnbreakableRock(DemolitionWars game){
        super(game);
        name="Unbreakable Rock";
        setSpriteId(8);
        price=450;
    }
}
