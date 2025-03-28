package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockScatterbomb extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockScatterbomb(DemolitionWars game){super(game, 50, 0, 0, false, 0);
        name="Scatterbomb";
        setSpriteId(25);
        price = 700;
    }

    /**
     * Scatterrrrr the woooorlld (#makeitamess)
     */
    public void createExplosion(){
        for(int i = 0; i<game.world.blocks.size(); ) { //First for-loop where stuff disappears
            if (distanceToThisObject(game.world.blocks.get(i)) < 1000  && !(game.world.blocks.get(i) instanceof BlockUnbreakableRock)) {
                int randomInt = (int)(Math.random()*2);
                if(randomInt==1) {
                    game.world.blocks.get(i).die();
                } else {
                    i++;
                    game.world.blocks.get(i).optimizeCollisions();
                    game.world.blocks.get(i).inAir=3;
                }
            } else {
                i++;
            }
        }
    }
}
