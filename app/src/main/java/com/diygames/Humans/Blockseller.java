package com.diygames.Humans;

import com.diygames.Blocks.BlockBrick;
import com.diygames.Blocks.BlockDirt;
import com.diygames.Blocks.BlockGrass;
import com.diygames.Blocks.BlockPlanks;
import com.diygames.Blocks.BlockSteel;
import com.diygames.Blocks.BlockStone;
import com.diygames.Blocks.BlockUnbreakableRock;
import com.diygames.Blocks.BlockWoodenPole;
import com.diygames.Blocks.BlockWool;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Blockseller extends Salesman {

    /**
    * Add all sales to his inventory
     */
    public Blockseller(DemolitionWars game, boolean team, int homeX){
        super(game, team, homeX);
        for(int i = 0; i<12; i++){
            inventory.add(getSale(i));
        }
        //Collections.sort(inventory);
    }

    /**
     * Get one of his sales as a new instance
     * @param saleId
     * @return
     */
    public Obtainables getSale(int saleId){
        switch(saleId){
            case 0:
                return new BlockBrick(game);
            case 1:
                return new BlockDirt(game);
            case 2:
                return new BlockGrass(game);
            case 3:
               return new BlockPlanks(game);
            case 4:
                return new BlockSteel(game);
            case 5:
                return new BlockStone(game);
            case 6:
                return new BlockUnbreakableRock(game);
            case 7:
                return new BlockWoodenPole(game);
            case 8:
                return new BlockWool(game, 0);
            case 9:
                return new BlockWool(game, 1);
            case 10:
                return new BlockWool(game, 2);
            case 11:
                return new BlockWool(game, 3);
        }
        return null;
    }

}
