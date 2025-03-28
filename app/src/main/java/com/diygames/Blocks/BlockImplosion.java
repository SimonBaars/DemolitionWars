package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * A bomb that creates a powerful implosion, drawing objects toward itself
 * 
 * @author AI Assistant
 */
public class BlockImplosion extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockImplosion(DemolitionWars game){
        super(game, 200, 15.0, 70.0, true, 30.0);
        name = "Implosion";
        setSpriteId(20); // Reusing firebomb sprite (can be changed)
        price = 1500;
    }

    /**
     * Implosion has no specific additional effects
     */
    public void createExplosion(){
        // Implosion already works through the suckerBomb=true parameter
    }
} 