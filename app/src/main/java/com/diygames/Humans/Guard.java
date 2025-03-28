package com.diygames.Humans;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;

import com.diygames.Items.ItemBlade;
import com.diygames.Items.ItemGrenadeLauncher;
import com.diygames.Items.ItemHealthPotion;
import com.diygames.Items.ItemMachineGun;
import com.diygames.Items.ItemPistol;
import com.diygames.Items.ItemWeapon;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Guard extends Bot implements IAlarm {

    boolean shouldShoot = true;
    Alarm shootAlarm;

    /**
     * Add a random weapon to his inventory :D, and make him ready to attack
     * @param game
     * @param team
     * @param homeX
     */
    public Guard(DemolitionWars game, boolean team, int homeX){
        super(game, team, homeX, game.tileSize*3, game.tileSize*3);
        setSpriteId(27+((team)? 1 : 0));
        int weapon = (int)(Math.random()*4);
        if(weapon==0){
            inventory.add(new ItemBlade(game));
        } else if(weapon==1){
            inventory.add(new ItemPistol(game));
        } else if(weapon==2){
            inventory.add(new ItemMachineGun(game));
        } else if(weapon==3){
            inventory.add(new ItemGrenadeLauncher(game));
        }
        shootAlarm=new Alarm(8, 20, this);
        shootAlarm.startAlarm();
    }

    /**
     * Shoot :D
     * @param alarmId
     */
    public void triggerAlarm(int alarmId){
        super.triggerAlarm(alarmId);
        if(alarmId==8){
            shouldShoot=true;
        }
    }

    /**
     * Attack the player if he's near
     */
	public void attack() {
        boolean playerDirection=true;
        if(game.world.humans.get(0).getX()>getX()){
            playerDirection=false;
        }
        ItemWeapon weapon = (ItemWeapon)inventory.get(0);
        weapon.getUsedByBot(getX(), getY(), playerDirection);
	}

    /**
     * Checks if the bot should attack or not
     */
    public void update(){
        super.update();
        if(distanceToPlayer()<game.tileSize*9 && team!=game.world.humans.get(0).team && shouldShoot){
            attack();
            shouldShoot=false;
            shootAlarm.restartAlarm();
        }
    }

    /**
     * Talk to him and receive a free (but useless :P) health potion
     */
    public void interact(){
        if(team==game.world.humans.get(0).team){
            game.world.humans.get(0).health=100;
            boolean inventoryContainsHealthPotion = false;
            for(Obtainables inventoryObject : game.world.humans.get(0).inventory){
                if(inventoryObject instanceof ItemHealthPotion){
                    inventoryContainsHealthPotion=true;
                    break;
                }
            }
            if(!inventoryContainsHealthPotion) {
                game.world.humans.get(0).inventory.add(new ItemHealthPotion(game));
            }
        }
    }
}
