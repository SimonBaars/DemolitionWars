package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockNapalm extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockNapalm(DemolitionWars game){
        super(game, 100, 0, 0, true, 0.0);
        name="Napalm";
        setSpriteId(23);
        price=1000;
    }

    /**
     * Napalm throws firebombs into the world
     */
    public void createExplosion(){
        for(int i = 0; i<360; i+=40){
            game.world.blocks.add(new BlockFirebomb(game));
            BlockExplosive addedExplosive = (BlockExplosive)game.world.blocks.get(game.world.blocks.size() - 1);
            game.addGameObject(addedExplosive, getX(), getY());
            addedExplosive.setExplosionTimer();
            addedExplosive.addXTimesToMomentum(100, i, 40);
        }
    }
}
