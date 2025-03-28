package com.diygames.Humans;

import com.diygames.Blocks.BlockGrenade;
import com.diygames.Blocks.BlockMinerBomb;
import com.diygames.Blocks.BlockNapalm;
import com.diygames.Blocks.BlockScatterbomb;
import com.diygames.Blocks.BlockSteel;
import com.diygames.Blocks.BlockSupernova;
import com.diygames.Blocks.BlockUnbreakableRock;
import com.diygames.Blocks.BlockWool;
import com.diygames.Items.ItemBullet;
import com.diygames.Items.ItemStrengthPotion;
import com.diygames.demolitionwars.DemolitionWars;

import java.util.ArrayList;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class King extends Bot {

    ArrayList<Integer> givenGifts = new ArrayList<Integer>();

    /**
     * Simply set the sprite and the walking distance
     * @param game
     * @param team
     * @param homeX
     */
    public King(DemolitionWars game, boolean team, int homeX){
        super(game, team, homeX, game.tileSize*4, game.tileSize*2);
        setSpriteId(33 + ((team) ? 1 : 0));
    }

    /**
     * Talk to the kill for free stuff with every kill (Dutch people love free stuff)
     */
	public void interact() {
        Player player = (Player)game.world.humans.get(0);
            if (player.killCounter == 0 && !givenGifts.contains(0)) {
                givenGifts.add(0);
                player.inventory.add(new ItemStrengthPotion(game));
            } else if (player.killCounter == 1 && !givenGifts.contains(1)) {
                givenGifts.add(1);
                player.inventory.add(new BlockUnbreakableRock(game));
            } else if (player.killCounter == 2 && !givenGifts.contains(2)) {
                givenGifts.add(2);
                player.inventory.add(new ItemBullet(game));
            } else if (player.killCounter == 3 && !givenGifts.contains(3)) {
                givenGifts.add(3);
                player.inventory.add(new BlockGrenade(game));
            } else if (player.killCounter == 4 && !givenGifts.contains(4)) {
                givenGifts.add(4);
                for (int i = 0; i < 6; i++) {
                    player.inventory.add(new BlockSteel(game));
                }
            } else if (player.killCounter == 5 && !givenGifts.contains(5)) {
                givenGifts.add(5);
                for (int i = 0; i < 4; i++) {
                    player.inventory.add(new BlockWool(game, i));
                }
            } else if (player.killCounter == 6 && !givenGifts.contains(6)) {
                givenGifts.add(6);
                player.inventory.add(new ItemStrengthPotion(game));
            } else if (player.killCounter == 7 && !givenGifts.contains(7)) {
                givenGifts.add(7);
                player.inventory.add(new BlockMinerBomb(game));
            } else if (player.killCounter == 8 && !givenGifts.contains(8)) {
                givenGifts.add(8);
                player.inventory.add(new BlockGrenade(game));
            } else if (player.killCounter == 9 && !givenGifts.contains(9)) {
                givenGifts.add(9);
                player.inventory.add(new ItemStrengthPotion(game));
            } else if (player.killCounter == 10 && !givenGifts.contains(10)) {
                givenGifts.add(10);
                player.inventory.add(new ItemStrengthPotion(game));
            } else if (player.killCounter == 11 && !givenGifts.contains(11)) {
                givenGifts.add(11);
                player.inventory.add(new BlockScatterbomb(game));
            } else if (player.killCounter == 12 && !givenGifts.contains(12)) {
                givenGifts.add(12);
                player.inventory.add(new BlockNapalm(game));
            } else if (player.killCounter == 13 && !givenGifts.contains(13)) {
                givenGifts.add(13);
                player.inventory.add(new BlockSupernova(game)); //Finish him :P
            }
    }
}
