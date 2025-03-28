package com.diygames.Items;

import com.diygames.Blocks.Block;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Oguz Uncu
 */
public class ItemDrill extends Item {
    /**
     * Set the item's price 'n name 'n uses
     * @param game
     */
    public ItemDrill(DemolitionWars game){
        super(game);
        name="Drill";
        usesLeft=20;
        price=500;
    }
/**
 * Use the drill a.k.a. check all blocks for being near and remove the first one
 */
	public void use() {
        for(int i = 0; i<game.world.blocks.size(); i++) {
            if (game.world.blocks.get(i).distanceToPlayer()<game.tileSize*7 && game.world.blocks.get(i) instanceof Block) {
                game.world.blocks.get(i).die();
                game.world.humans.get(0).optimizeCollisions();
                usesLeft--;
                break;
            }
        }
	}

}
