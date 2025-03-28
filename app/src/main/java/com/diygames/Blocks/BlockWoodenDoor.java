package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockWoodenDoor extends BlockDoor {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockWoodenDoor(DemolitionWars game, boolean team, int doorId){
        super(game, team, doorId);
        name="Wooden Door";
        setSpriteId(9);
        price = 200;
    }
}
