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
     */
    private void generateTerrain(List<GameEntity> entities) {
        // Layer positions (from bottom to top, matching original)
        int LAYER_BEDROCK = -1;
        int LAYER_STONE_1 = 0;
        int LAYER_STONE_2 = 1;
        int LAYER_DIRT_1 = 2;
        int LAYER_DIRT_2 = 3;
        int LAYER_GRASS = 4;
        
        // Generate layers across entire map width
        for (int x = 0; x < MAP_WIDTH; x += (int)TILE_SIZE) {
            createLayer(entities, Block.BlockType.UNBREAKABLE, LAYER_BEDROCK, x);
            createLayer(entities, Block.BlockType.STONE, LAYER_STONE_1, x);
            createLayer(entities, Block.BlockType.STONE, LAYER_STONE_2, x);
            createLayer(entities, Block.BlockType.DIRT, LAYER_DIRT_1, x);
            createLayer(entities, Block.BlockType.DIRT, LAYER_DIRT_2, x);
            createLayer(entities, Block.BlockType.GRASS, LAYER_GRASS, x);
        }
    }
    
    /**
     * Create a single terrain layer block
     */
    private void createLayer(List<GameEntity> entities, Block.BlockType type, int layer, int x) {
        float y = MAP_HEIGHT - (layer * TILE_SIZE);
        entities.add(new Block(physicsWorld, x, y, type));
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
                    float yPos = MAP_HEIGHT - ((5 + rowOffset) * TILE_SIZE);
                    
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
     */
    private void createObject(List<GameEntity> entities, int blockType, float x, float y, boolean team) {
        switch (blockType) {
            case 1: // Brick
            case 7: // Brick alt
                entities.add(new Block(physicsWorld, x, y, Block.BlockType.BRICK));
                break;
            case 6: // Steel
                entities.add(new Block(physicsWorld, x, y, Block.BlockType.STEEL));
                break;
            case 9: // Planks
                entities.add(new Block(physicsWorld, x, y, Block.BlockType.PLANKS));
                break;
            case 4: // Wool red
                entities.add(new Block(physicsWorld, x, y, Block.BlockType.WOOL_RED));
                break;
            case 5: // Wool white
                entities.add(new Block(physicsWorld, x, y, Block.BlockType.WOOL_WHITE));
                break;
            case 10: // Demolist (merchant)
                entities.add(new Merchant(physicsWorld, x, y, team, Merchant.MerchantType.EXPLOSIVES));
                break;
            case 11: // Blockseller (merchant)
                entities.add(new Merchant(physicsWorld, x, y, team, Merchant.MerchantType.BLOCKS));
                break;
            case 12: // Weaponclerk (merchant)
                entities.add(new Merchant(physicsWorld, x, y, team, Merchant.MerchantType.WEAPONS));
                break;
            case 14: // Guard
                entities.add(new Guard(physicsWorld, x, y, team));
                break;
            case 15: // King
                entities.add(new King(physicsWorld, x, y, team));
                break;
            // 2, 3, 8, 13 are doors, ladders, poles - skip for now
        }
    }
}
