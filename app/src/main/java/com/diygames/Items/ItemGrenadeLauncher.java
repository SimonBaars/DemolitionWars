package com.diygames.Items;

import android.gameengine.icadroids.alarms.Alarm;

import com.diygames.Blocks.BlockGrenade;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;

import java.util.ArrayList;

public class ItemGrenadeLauncher extends ItemGun {

    ArrayList<BlockGrenade> ammo = new ArrayList<BlockGrenade>();

    /**
     * Set the item's price 'n name
     * @param game
     */
    public ItemGrenadeLauncher(DemolitionWars game){
        super(game);
        name="Grenade Launcher";
        price=250;
    }

    /**
     * Shoot an grenade into the world
     */
    @Override
    public void use() {
        int i = 0;
        for(Obtainables inventoryItem : game.world.humans.get(0).inventory) {
            if (inventoryItem instanceof BlockGrenade) {
                BlockGrenade grenade =  (BlockGrenade)inventoryItem;
                boolean usageDirection=false;
                if(game.world.humans.get(0).animationFrame>3){
                    usageDirection=true;
                }
                game.addGameObject(grenade, game.world.humans.get(0).getX(), game.world.humans.get(0).getY());
                grenade.explosionTime = new Alarm(3,grenade.timeTillExplosion/2,grenade);
                grenade.explosionTime.startAlarm();
                grenade.launchIntoTheWorld(50 , usageDirection);
                game.world.blocks.add(grenade);
                game.world.humans.get(0).inventory.remove(inventoryItem);
                break;
            }
            i++;
        }
    }

    /**
     * Make the bot shoot an grenade into the world
     * @param botX
     * @param botY
     * @param usageDirection
     */
    @Override
    public void getUsedByBot(int botX, int botY, boolean usageDirection) {
        BlockGrenade grenade = new BlockGrenade(game);
        game.addGameObject(grenade, botX, botY);
        grenade.explosionTime = new Alarm(3,grenade.timeTillExplosion/2,grenade);
        grenade.explosionTime.startAlarm();
        grenade.launchIntoTheWorld(40 , usageDirection);
        game.world.blocks.add(grenade);
    }
}
