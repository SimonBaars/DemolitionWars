package com.demolitionwars.world;

import com.badlogic.gdx.physics.box2d.World;
import com.demolitionwars.entities.*;

import java.util.List;

/**
 * Generates and manages the game world terrain and NPCs.
 * Based on the original World.java but using Box2D physics.
 * 
 * Note: Original game uses tileSize * 3 = 192px per tile
 * This port uses 192px tiles to match the original scale
 */
public class GameWorld {
    
    private static final float TILE_SIZE = 192f; // Match original: tileSize * 3
    private static final float BLOCK_SIZE = 64f; // Actual block size for rendering
    private static final int MAP_WIDTH = 30000;
    private static final int MAP_HEIGHT = 4000;
    
    private final World physicsWorld;
    
    public GameWorld(World physicsWorld) {
        this.physicsWorld = physicsWorld;
    }
    
    /**
     * Generate the complete game world with terrain and enemies
     */
    public void generate(List<GameEntity> entities) {
        generateTerrain(entities);
        generateWorld(entities);
    }
    
    /**
     * Generate terrain layers across the entire map (matching original)
     * Original uses inverted Y (Android top-left origin), we use bottom-left (Box2D)
     * Player spawns at (2974, 2848), grass should be at around y=2800
     * 
     * Each "tile" in the original is 192x192 (tileSize * 3), containing 3x3 blocks of 64x64
     */
    private void generateTerrain(List<GameEntity> entities) {
        // Calculate grass layer position to be just below player spawn
        float GRASS_LEVEL = 2800f; // Just below player spawn at 2848
        
        // Generate layers across entire map width
        // Using BLOCK_SIZE (64) for individual blocks, not TILE_SIZE (192)
        for (float x = 0; x < MAP_WIDTH; x += BLOCK_SIZE) {
            // Layers from bottom to top (6 layers total, each TILE_SIZE tall = 192)
            // But each layer needs multiple blocks vertically too
            for (int layerBlock = 0; layerBlock < 3; layerBlock++) {
                entities.add(new Block(physicsWorld, x, GRASS_LEVEL - 5 * TILE_SIZE + layerBlock * BLOCK_SIZE, Block.BlockType.UNBREAKABLE)); // Bedrock
                entities.add(new Block(physicsWorld, x, GRASS_LEVEL - 4 * TILE_SIZE + layerBlock * BLOCK_SIZE, Block.BlockType.STONE));
                entities.add(new Block(physicsWorld, x, GRASS_LEVEL - 3 * TILE_SIZE + layerBlock * BLOCK_SIZE, Block.BlockType.STONE));
                entities.add(new Block(physicsWorld, x, GRASS_LEVEL - 2 * TILE_SIZE + layerBlock * BLOCK_SIZE, Block.BlockType.DIRT));
                entities.add(new Block(physicsWorld, x, GRASS_LEVEL - 1 * TILE_SIZE + layerBlock * BLOCK_SIZE, Block.BlockType.DIRT));
                entities.add(new Block(physicsWorld, x, GRASS_LEVEL + layerBlock * BLOCK_SIZE, Block.BlockType.GRASS));
            }
        }
    }
    
    /**
     * Generate the structured world layout (matching original game's 2D array)
     */
    private void generateWorld(List<GameEntity> entities) {
        // World layout from original game - each number is a block type
        // 0=empty, 1=brick, 6=steel, 7=brick, 9=planks, 14=guard, 15=king
        int[][] worldLayout = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,1,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,6,0,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,0,0,7,0,0,0,0,0,0,9,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,0,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,13,0,0,15,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,13,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,13,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,13,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,13,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,13,0,0,0,0,1,0,0,0,0,4,5,4,5,4,0,0,4,5,4,5,4,0,0,4,5,4,5,4,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,9,0,0,0,0},
            {0,1,13,0,0,0,8,1,8,0,0,3,10,3,0,3,0,0,3,11,3,0,3,0,0,3,12,3,0,3,0,0,0,0,2,6,6,6,6,2,0,14,14,0,2,7,7,7,7,2,0,14,14,0,2,9,9,9,9,2,0,0,14,14,0,8,9,8,0,0,14,0,8,9,8,0,0,0},
            {0,1,13,0,0,0,0,1,0,0,0,3,0,3,0,3,0,0,3,0,3,0,3,0,0,3,0,3,0,3,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,9,0,0,0,0},
        };
        
        placeWorldObjects(entities, worldLayout);
    }
    
    /**
     * Place objects in the world based on the layout (matching original)
     */
    private void placeWorldObjects(List<GameEntity> entities, int[][] worldLayout) {
        // World structures start at grass level + some offset to be above ground
        float STRUCTURE_BASE_Y = 2800f + (6 * TILE_SIZE); // Start above grass layer
        
        // Generate both sides (team 0 and team 1)
        for (int team = 0; team < 2; team++) {
            int rowOffset = 0;
            
            // Place objects from bottom to top
            for (int row = worldLayout.length - 1; row >= 0; row--) {
                int reverseColumn = worldLayout[0].length - 1;
                
                // Place objects in each row
                for (int col = 0; col < worldLayout[0].length; col++) {
                    int blockType = worldLayout[row][col];
                    
                    // Skip empty spaces
                    if (blockType == 0) {
                        reverseColumn--;
                        continue;
                    }
                    
                    // Calculate mirrored position for team 1
                    int mirrorCol = (team == 0) ? col : reverseColumn;
                    
                    // Calculate position
                    float xPos = (mirrorCol * TILE_SIZE) + ((TILE_SIZE * worldLayout[0].length) * team);
                    float yPos = STRUCTURE_BASE_Y + (rowOffset * TILE_SIZE);
                    
                    // Create the appropriate object
                    createObject(entities, blockType, xPos, yPos, team != 0);
                    
                    reverseColumn--;
                }
                rowOffset++;
            }
        }
    }
    
    /**
     * Create object based on type ID
     * Each tile position (x, y) needs to create a 3x3 grid of 64x64 blocks
     */
    private void createObject(List<GameEntity> entities, int blockType, float x, float y, boolean team) {
        // For blocks, create 3x3 grid to fill the 192x192 tile
        Block.BlockType type = null;
        
        switch (blockType) {
            case 1: // Brick
            case 7: // Brick alt
                type = Block.BlockType.BRICK;
                break;
            case 6: // Steel
                type = Block.BlockType.STEEL;
                break;
            case 9: // Planks
                type = Block.BlockType.PLANKS;
                break;
            case 4: // Wool red
                type = Block.BlockType.WOOL_RED;
                break;
            case 5: // Wool white
                type = Block.BlockType.WOOL_WHITE;
                break;
            case 10: // Demolist (merchant) - spawn in center of tile
                entities.add(new Merchant(physicsWorld, x + TILE_SIZE / 2, y + TILE_SIZE / 2, team, Merchant.MerchantType.EXPLOSIVES));
                return;
            case 11: // Blockseller (merchant)
                entities.add(new Merchant(physicsWorld, x + TILE_SIZE / 2, y + TILE_SIZE / 2, team, Merchant.MerchantType.BLOCKS));
                return;
            case 12: // Weaponclerk (merchant)
                entities.add(new Merchant(physicsWorld, x + TILE_SIZE / 2, y + TILE_SIZE / 2, team, Merchant.MerchantType.WEAPONS));
                return;
            case 14: // Guard
                entities.add(new Guard(physicsWorld, x + TILE_SIZE / 2, y + TILE_SIZE / 2, team));
                return;
            case 15: // King
                entities.add(new King(physicsWorld, x + TILE_SIZE / 2, y + TILE_SIZE / 2, team));
                return;
            // 2, 3, 8, 13 are doors, ladders, poles - skip for now
            default:
                return;
        }
        
        // Create 3x3 blocks to fill the tile
        if (type != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    entities.add(new Block(physicsWorld, x + i * BLOCK_SIZE, y + j * BLOCK_SIZE, type));
                }
            }
        }
    }
}
