package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;

public class ItemHealthPotion extends ItemPotion {
    /**
     * Set the item's price 'n name 'n uses
     * @param game
     */
    public ItemHealthPotion(DemolitionWars game){
        super(game);
        name="Health Potion";
        price=10;
        usesLeft=1;
    }

    /**
     * Set the player's health to 100
     */
    @Override
    public void use() {
        game.world.humans.get(0).health=100;
        usesLeft--;
    }
}
