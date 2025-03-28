package com.diygames.Humans;

import com.diygames.Items.ItemBlade;
import com.diygames.Items.ItemBullet;
import com.diygames.Items.ItemDrill;
import com.diygames.Items.ItemGrenadeLauncher;
import com.diygames.Items.ItemHealthPotion;
import com.diygames.Items.ItemMachineGun;
import com.diygames.Items.ItemPistol;
import com.diygames.Items.ItemStrengthPotion;
import com.diygames.Items.ItemWing;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Weaponclerk extends Salesman {
    /**
     * Add all his sales to his inventory
     * @param game
     * @param team
     * @param homeX
     */
    public Weaponclerk(DemolitionWars game, boolean team, int homeX){
        super(game, team, homeX);
        for(int i = 0; i<9; i++){
            inventory.add(getSale(i));
        }
        //Collections.sort(inventory);
    }

    /**
     * Return the sale as a new item
     * @param saleId
     * @return
     */
    public Obtainables getSale(int saleId) {
        switch (saleId) {
            case 0:
                return new ItemBlade(game);
            case 1:
                return new ItemBullet(game);
            case 2:
                return new ItemDrill(game);
            case 3:
                return new ItemGrenadeLauncher(game);
            case 4:
                return new ItemHealthPotion(game);
            case 5:
                return new ItemMachineGun(game);
            case 6:
                return new ItemPistol(game);
            case 7:
                return new ItemStrengthPotion(game);
            case 8:
                return new ItemWing(game);
        }
        return null;
    }
}
