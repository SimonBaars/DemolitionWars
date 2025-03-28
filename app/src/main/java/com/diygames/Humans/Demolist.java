package com.diygames.Humans;

import com.diygames.Blocks.BlockFirebomb;
import com.diygames.Blocks.BlockGrenade;
import com.diygames.Blocks.BlockMinerBomb;
import com.diygames.Blocks.BlockNapalm;
import com.diygames.Blocks.BlockNuke;
import com.diygames.Blocks.BlockScatterbomb;
import com.diygames.Blocks.BlockSupernova;
import com.diygames.Blocks.BlockTNT;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Demolist extends Salesman{
/**
 * Add his sales to his inventory and sort if wanted
 */
    public Demolist(DemolitionWars game, boolean team, int homeX){
        super(game, team, homeX);
        for(int i = 0; i<8; i++){
            inventory.add(getSale(i));
        }
        //Collections.sort(inventory);
    }

    /**
     * Get one of his sales as a new instance
     * @param saleId
     * @return
     */
    public Obtainables getSale(int saleId) {
        switch (saleId) {
            case 0:
                return new BlockFirebomb(game);
            case 1:
                return new BlockGrenade(game);
            case 2:
                return new BlockMinerBomb(game);
            case 3:
                return new BlockNapalm(game);
            case 4:
                return new BlockNuke(game);
            case 5:
                return new BlockScatterbomb(game);
            case 6:
                return new BlockSupernova(game);
            case 7:
                return new BlockTNT(game);
        }
        return null;
    }
}
