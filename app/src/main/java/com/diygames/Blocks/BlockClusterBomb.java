package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * A cluster bomb that splits into multiple smaller explosions
 * 
 * @author AI Assistant
 */
public class BlockClusterBomb extends BlockExplosive {
    /**
     * Create the block, set the name, set the price and set the sprite
     * @param game
     */
    public BlockClusterBomb(DemolitionWars game){
        super(game, 150, 2.0, 30.0, false, 90.0);
        name = "Cluster Bomb";
        setSpriteId(21); // Reusing grenade sprite (can be changed)
        price = 800;
    }

    /**
     * Creates several smaller explosions around the main one
     */
    public void createExplosion(){
        // Create several TNT explosions in a cluster pattern
        for(int i = 0; i < 5; i++) {
            // Calculate positions in a circular pattern
            double angle = (i * 72) % 360; // 72 degrees between each bomb (360/5)
            int offsetX = (int)(Math.cos(Math.toRadians(angle)) * game.tileSize * 3);
            int offsetY = (int)(Math.sin(Math.toRadians(angle)) * game.tileSize * 3);
            
            // Create a TNT bomb at the calculated position
            BlockTNT childBomb = new BlockTNT(game);
            game.addGameObject(childBomb, getX() + offsetX, getY() + offsetY);
            
            // Make it explode with a short delay based on distance
            childBomb.timeTillExplosion = 10 + (i * 5);
            childBomb.setExplosionTimer();
        }
    }
} 