package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockFirebomb extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockFirebomb(DemolitionWars game){
        super(game, 50, 5.0, 50.0, true, 0.0);
        name="Firebomb";
        setSpriteId(20);
        price = 350;
    }
    public void createExplosion(){

    }
}
