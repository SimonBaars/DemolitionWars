package com.diygames.Items;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;

import com.diygames.Humans.Human;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Oguz Uncu
 */
public class ItemBlade extends ItemWeapon implements IAlarm {

    private Alarm bladeAlarm;

    private boolean inCombat = false;

    boolean usedByBot;

    /**
     * Set the item's price 'n name
     * @param game
     */
    public ItemBlade(DemolitionWars game){
        super(game);
        name="Blade";
        bladeAlarm = new Alarm(9,4,this);
        price = 300;
    }

    /**
     * remove the blade from the world
     * @param alarmId
     */
    public void triggerAlarm(int alarmId){
        if(alarmId==9){
            game.world.blocks.remove(this);
            game.deleteGameObject(this);
        }
    }

    /**
     * Use the blade and add it to the world
     */
    @Override
    public void use() {
        int x = game.world.humans.get(0).getX();
        int y = game.world.humans.get(0).getY();
        boolean usageDirection=false;
        if(game.world.humans.get(0).animationFrame>3){
            usageDirection=true;
        }
        createBlade(x, y, usageDirection, false);
    }

    /**
     * Get used by a bot
     * @param botX
     * @param botY
     * @param usageDirection
     */
    public void getUsedByBot(int botX, int botY, boolean usageDirection){
        createBlade(botX, botY, usageDirection, true);
    }

    /**
     * Add the blade object to the world
     * @param x
     * @param y
     * @param usageDirection
     * @param usedBy
     */
    public void createBlade(int x, int y, boolean usageDirection, boolean usedBy){
        ItemBlade blade = new ItemBlade(game);
        game.world.blocks.add(this);
        game.addGameObject(blade, x+128-(256*((usageDirection)? 1 : 0)), y+128);
        blade.getUsedInCombat(usageDirection, usedBy);
    }

    /**
     * Use the blade in combat
     * @param usageDirection
     * @param usedBy
     */
    public void getUsedInCombat(boolean usageDirection, boolean usedBy){
        setSpriteId(31+((usageDirection)? 1 : 0));
        inCombat=true;
        usedByBot=usedBy;
        bladeAlarm.startAlarm();
    }

    /**
     * If the blade is used by a bot and hits the player the player will lose 100 health. If the blade is used by a player and hits a bot the bot will die. It's that easy.
     */
    public void update(){
        super.update();
        if(inCombat) {
            if (usedByBot) {
                if (getX() > game.world.humans.get(0).getX() - (2 * 64) && getY() > game.world.humans.get(0).getY() - (2 * 64) && getX() < game.world.humans.get(0).getX() + (4 * 64) && getY() < game.world.humans.get(0).getY() + (4 * 64)) {
                    game.world.humans.get(0).health -= 100;
                    die();
                }
            } else {
                for (int i = 0; i < game.world.blocks.size(); ) {
                    if (getX() > game.world.blocks.get(i).getX() - (2 * 64) && getY() > game.world.blocks.get(i).getY() - (2 * 64) && getX() < game.world.blocks.get(i).getX() + (4 * 64) && getY() < game.world.blocks.get(i).getY() + (4 * 64) && game.world.blocks.get(i) instanceof Human) {
                        game.world.blocks.get(i).die();
                        die();
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
    }
}
