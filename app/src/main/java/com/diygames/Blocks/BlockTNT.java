package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockTNT extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockTNT(DemolitionWars game){
        super(game, 50, 5.0, 50.0, false, 100.0);
        name="TNT";
        setSpriteId(19);
        price = 350;
    }

    /**
     * Regular bomb. Nothing special 'bout it
     */
    public void createExplosion(){

    }
}