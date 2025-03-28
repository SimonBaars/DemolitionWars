package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Oguz Uncu
 */
public class ItemMachineGun extends ItemGun {
    /**
     * Set the item's price 'n name 'n uses
     * @param game
     */
    public ItemMachineGun(DemolitionWars game){
        super(game);
        name="Machine Gun";
        usesLeft=10;
        price=1500;
    }

    /**
     * Get a bullet shot by player
     */
    @Override
    public void use() {
                ItemBullet bullet =  new ItemBullet(game);
                boolean usageDirection=false;
                if(game.world.humans.get(0).animationFrame>3){
                    usageDirection=true;
                }
                bullet.getShot(game.world.humans.get(0).getX(), game.world.humans.get(0).getY(), usageDirection, true);
                game.world.blocks.add(bullet);
        usesLeft--;
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
