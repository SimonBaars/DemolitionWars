package com.diygames.demolitionwars;

import android.gameengine.icadroids.objects.graphics.AnimatedSprite;

import com.diygames.Blocks.BlockLadder;
import com.diygames.Blocks.BlockWoodenPole;

/**
 * Created by Simon Baars on 3/25/2015.
 */
public class SpriteLoader {

    private String[] spriteNames = {"speler_zwart", "brick", "dirt", "grass", "planks", "steel", "steel_door", "stone", "unbreakable_rock", "wooden_door", "woodenpole", "wool_red", "wool_white","wool_black", "wool_blue", "merchant_zwart", "merchant_blauw", "ladder1", "ladder2", "tnt", "firebomb", "grenade", "miner", "napalm", "nuke", "scatterbomb", "supernova", "guard_zwart", "guard_blauw", "bullet", "bullet2", "blade", "blade2", "king_blauw", "king_zwart"};

    private int[] humanSprites = {0,15,16, 27, 28, 33, 34};

    public AnimatedSprite[] sprites = new AnimatedSprite[spriteNames.length];

    /**
    * Load all sprites
     */
    public SpriteLoader(){
        for(int i = 0; i<spriteNames.length; i++){
            sprites[i] = new AnimatedSprite();
            if(inArray(humanSprites, i)){ //A human has an animated sprite
                sprites[i].loadAnimatedSprite(spriteNames[i], 8);
            } else {
                sprites[i].loadAnimatedSprite(spriteNames[i], 1);
            }
        }
    }

    /**
    * Check if an integer is in an array
     */
    public boolean inArray(int[] array, int value){
        for(int i = 0; i<array.length; i++){
            if(array[i]==value) {
                return true;
            }
        }
        return false;
    }

    /**
    * Returns true if an object has no collision
     */
    public boolean hasNoCollision(MovingObject block){
            if(block instanceof BlockWoodenPole || block instanceof BlockLadder){
                return true;
            }
        return false;
    }
}
