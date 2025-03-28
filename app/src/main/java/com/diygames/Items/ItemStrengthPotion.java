package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;

public class ItemStrengthPotion extends ItemPotion {
    /**
     * Set the item's price 'n name
     * @param game
     */
    public ItemStrengthPotion(DemolitionWars game){
        super(game);
        name="Strength Potion";
        price=200;
    }

    /**
     * Get 50 extra health :D
     */
    @Override
    public void use() {
        game.world.humans.get(0).health+=50;
        usesLeft--;
    }
}
