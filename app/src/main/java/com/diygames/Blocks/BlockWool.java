package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;

/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockWool extends BlockLandscape {
    /**
     * Create the block, set the name, set the price, set the wooltype and set the sprite
     * @param game
     */
    public BlockWool(DemolitionWars game, int woolType){
        super(game);
        name="Wool ";
        switch(woolType){
            case 0:
                name+="(Rood)";
                break;
            case 1:
                name+="(Wit)";
                break;
            case 2:
                name+="(Zwart)";
                break;
            case 3:
                name+="(Blauw)";
                break;
        }
        setSpriteId(11+woolType);
        price = 100;
    }
}