package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Oguz Uncu
 */
public class ItemWing extends Item {
    /**
     * Set the item's price 'n name 'n uses
     * @param game
     */
    public ItemWing(DemolitionWars game){
        super(game);
        name="Wing";
        usesLeft=70;
        price = 600;
    }

    /**
     * Fly high up in the sky :D
     */
	public void use() {
        game.world.humans.get(0).addToMomentum(0, 100);
        usesLeft--;
	}

}
