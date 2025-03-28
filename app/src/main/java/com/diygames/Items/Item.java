package com.diygames.Items;

import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Oguz Uncu
 */
public class Item extends Obtainables {

    public int usesLeft=1;

    /**
     * Init the item
     * @param game
     */
    public Item(DemolitionWars game){
        super(game);
    }

    /**
     * Use the item
     */
    @Override
    public void use() {}
}
