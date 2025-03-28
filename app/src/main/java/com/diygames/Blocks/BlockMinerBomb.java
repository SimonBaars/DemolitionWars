package com.diygames.Blocks;

import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockMinerBomb extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockMinerBomb(DemolitionWars game){super(game, 50, 15.0, 50.0, false, 100.0);
        name="Minerbomb";
        setSpriteId(22);
        price=1000;
    }

    /**
     * This explosive has no specific explosion
     */
    public void createExplosion(){

    }
    /**
     * Receive all exploded blocks in your inventory
     * @param game
     */
    public Obtainables getBlock(Obtainables block){
        if(block instanceof BlockBrick){
            return new BlockBrick(game);
        } else if(block instanceof BlockDirt){
            return new BlockDirt(game);
        } else if(block instanceof BlockWoodenDoor){
            return new BlockWoodenDoor(game, false, (int)(Math.random()*2));
        } else if(block instanceof BlockGrass){
            return new BlockGrass(game);
        } else if(block instanceof BlockLadder){
            return new BlockLadder(game, true);
        } else if(block instanceof BlockPlanks){
            return new BlockPlanks(game);
        } else if(block instanceof BlockSteel){
            return new BlockSteel(game);
        } else if(block instanceof BlockSteelDoor){
            return new BlockSteelDoor(game, false, (int)(Math.random()*2));
        } else if(block instanceof BlockStone){
            return new BlockStone(game);
        } else if(block instanceof BlockWoodenPole){
            return new BlockWoodenPole(game);
        } else if(block instanceof BlockWool){
            return new BlockWool(game, (int)(Math.random()*4));
        }
        return null;
    }
}
