package com.diygames.Humans;

import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public abstract class Salesman extends Bot {
    /**
     * Set the salesman walking distance and sprite
     * @param game
     * @param team
     * @param homeX
     */
    public Salesman(DemolitionWars game, boolean team, int homeX){
        super(game, team, homeX, game.tileSize*3, game.tileSize*9);
        setSpriteId(15 + ((team) ? 1 : 0));
    }

    /**
     * Shopping is fun. No clothing in this game though, I'm sorry women :P.
     */
	public void interact() {
        ((Player)game.world.humans.get(0)).currentlyBuyingFrom=this;
        ((Player)game.world.humans.get(0)).selectedShopItem=0;
        ((Player)game.world.humans.get(0)).inShop=true;
	}

    /**
     * Update the dashboard.
     */
    public void update(){
        super.update();
        if(((Player)game.world.humans.get(0)).inShop && this==((Player)game.world.humans.get(0)).currentlyBuyingFrom) {
            game.shopDisplayString = "Wanna buy a perfect "+inventory.get(((Player) game.world.humans.get(0)).selectedShopItem).name+" for just "+inventory.get(((Player) game.world.humans.get(0)).selectedShopItem).price+" DestructionDollars?";
        }
    }

    /**
     * Get the sale as a new item to be used in the player's inventory.
     * @param saleId
     * @return
     */
    public abstract Obtainables getSale(int saleId);
}
