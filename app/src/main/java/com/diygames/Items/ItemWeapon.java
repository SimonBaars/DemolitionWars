package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;

public abstract class ItemWeapon extends Item {

    public ItemWeapon(DemolitionWars game){
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
    public abstract void getUsedByBot(int botX, int botY, boolean usageDirection);

}
