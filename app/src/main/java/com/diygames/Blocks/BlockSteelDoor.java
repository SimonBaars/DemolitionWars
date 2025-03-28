package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockSteelDoor extends BlockDoor {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockSteelDoor(DemolitionWars game, boolean team, int doorId){
        super(game, team, doorId);
        name="Steel door";
        setSpriteId(6);
        price = 300;
    }
}
