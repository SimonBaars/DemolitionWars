package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;

public class ItemGun extends ItemWeapon {

    public ItemGun(DemolitionWars game){
        super(game);
    }

    /**
     * Weapon used by player
     */
    @Override
    public void use() {}

    /**
     * Weapon used by bot
     * @param botX
     * @param botY
     * @param usageDirection
     */
    @Override
    public void getUsedByBot(int botX, int botY, boolean usageDirection) {
    }

}
