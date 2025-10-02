package com.demolitionwars.world;

import com.badlogic.gdx.physics.box2d.World;
import com.demolitionwars.entities.*;

import java.util.List;

/**
 * Generates and manages the game world terrain and NPCs.
 * Based on the original World.java but using Box2D physics.
 */
public class GameWorld {
    
    private static final float TILE_SIZE = 64f;
    private final World physicsWorld;
    
    public GameWorld(World physicsWorld) {
        this.physicsWorld = physicsWorld;
    }
    
    /**
     * Generate the complete game world with terrain and enemies
     */
    public void generate(List<GameEntity> entities) {
        generateTerrain(entities);
        generateEnemies(entities);
    }
    
    /**
     * Generate terrain blocks
     */
    private void generateTerrain(List<GameEntity> entities) {
        // Create ground layer
        generateGroundLayer(entities, 0, 30000, 0, 500);
        
        // Create walls around spawn area
        generateWall(entities, 2500, 500, 5, 20); // Left wall
        generateWall(entities, 3500, 500, 5, 20); // Right wall
        
        // Create enemy fortress
        generateFortress(entities, 25000, 500);
        
        // Add some scattered blocks for platforming
        for (int i = 0; i < 50; i++) {
            float x = 5000 + (i * 400);
            float y = 800 + (float)(Math.sin(i * 0.5) * 300);
            entities.add(new Block(physicsWorld, x, y, Block.BlockType.BRICK));
        }
    }
    
    /**
     * Generate ground layer
     */
    private void generateGroundLayer(List<GameEntity> entities, float startX, float endX, float startY, float height) {
        for (float x = startX; x < endX; x += TILE_SIZE) {
            for (float y = startY; y < startY + height; y += TILE_SIZE) {
                Block.BlockType type;
                if (y < startY + 100) {
                    type = Block.BlockType.UNBREAKABLE; // Bedrock
                } else if (y < startY + 200) {
                    type = Block.BlockType.STONE;
                } else if (y < startY + 400) {
                    type = Block.BlockType.DIRT;
                } else {
                    type = Block.BlockType.GRASS;
                }
                entities.add(new Block(physicsWorld, x, y, type));
            }
        }
    }
    
    /**
     * Generate a wall
     */
    private void generateWall(List<GameEntity> entities, float x, float y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                entities.add(new Block(physicsWorld, x + (i * TILE_SIZE), y + (j * TILE_SIZE), Block.BlockType.BRICK));
            }
        }
    }
    
    /**
     * Generate enemy fortress
     */
    private void generateFortress(List<GameEntity> entities, float x, float y) {
        // Outer walls
        generateWall(entities, x - 500, y, 2, 30); // Left wall
        generateWall(entities, x + 1500, y, 2, 30); // Right wall
        
        // Inner structure
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 25; j++) {
                float blockX = x + (i * TILE_SIZE);
                float blockY = y + (j * TILE_SIZE);
                
                // Create rooms and passages
                if ((i > 5 && i < 10 && j > 5 && j < 15) ||
                    (i > 15 && i < 20 && j > 10 && j < 20)) {
                    continue; // Empty space for rooms
                }
                
                Block.BlockType type;
                if (j > 20) {
                    type = Block.BlockType.STEEL;
                } else if (j > 15) {
                    type = Block.BlockType.STONE;
                } else {
                    type = Block.BlockType.BRICK;
                }
                entities.add(new Block(physicsWorld, blockX, blockY, type));
            }
        }
    }
    
    /**
     * Generate enemy NPCs
     */
    private void generateEnemies(List<GameEntity> entities) {
        // Add guards around the fortress
        for (int i = 0; i < 5; i++) {
            float x = 24000 + (i * 500);
            entities.add(new Guard(physicsWorld, x, 600, true));
        }
        
        // Add the king in the center of the fortress
        entities.add(new King(physicsWorld, 26000, 1200, true));
        
        // Add some merchants for buying weapons
        entities.add(new Merchant(physicsWorld, 4000, 600, false, Merchant.MerchantType.WEAPONS));
        entities.add(new Merchant(physicsWorld, 8000, 600, false, Merchant.MerchantType.EXPLOSIVES));
    }
}
