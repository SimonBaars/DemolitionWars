package com.diygames.Items;

import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;

public class ItemPistol extends ItemGun {
    /**
     * Set the item's price 'n name
     * @param game
     */
    public ItemPistol(DemolitionWars game){
        super(game);
        name="Pistol";
        price=250;
    }

    /**
     * Get a bullet shot by player
     */
    @Override
    public void use() {
        for(Obtainables inventoryItem : game.world.humans.get(0).inventory) {
            if (inventoryItem instanceof ItemBullet) {
                ItemBullet bullet =  (ItemBullet)inventoryItem;
                boolean usageDirection=false;
                if(game.world.humans.get(0).animationFrame>3){
                    usageDirection=true;
                }
                bullet.getShot(game.world.humans.get(0).getX(), game.world.humans.get(0).getY(), usageDirection, true);
                game.world.blocks.add(bullet);
                game.world.humans.get(0).inventory.remove(inventoryItem);
                break;
            }
        }
    }

    /**
     * Get a bullet shot by bot
     * @param botX
     * @param botY
     * @param usageDirection
     */
    public void getUsedByBot(int botX, int botY, boolean usageDirection){
        ItemBullet bullet =  new ItemBullet(game);
        bullet.getShot(botX, botY, usageDirection, false);
    }
}
