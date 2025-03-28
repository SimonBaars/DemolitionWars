package com.diygames.demolitionwars;

import com.diygames.Blocks.BlockBrick;
import com.diygames.Blocks.BlockDirt;
import com.diygames.Blocks.BlockDoor;
import com.diygames.Blocks.BlockGrass;
import com.diygames.Blocks.BlockLadder;
import com.diygames.Blocks.BlockLandscape;
import com.diygames.Blocks.BlockPlanks;
import com.diygames.Blocks.BlockSteel;
import com.diygames.Blocks.BlockSteelDoor;
import com.diygames.Blocks.BlockStone;
import com.diygames.Blocks.BlockUnbreakableRock;
import com.diygames.Blocks.BlockWoodenDoor;
import com.diygames.Blocks.BlockWoodenPole;
import com.diygames.Blocks.BlockWool;
import com.diygames.Humans.Demolist;
import com.diygames.Humans.Human;
import com.diygames.Humans.King;
import com.diygames.Humans.Player;
import com.diygames.Humans.Blockseller;
import com.diygames.Humans.Weaponclerk;
import com.diygames.Humans.Guard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * World class responsible for generating and managing the game world.
 * Contains terrain, blocks, and characters.
 *
 * @author Simon Baars
 */
public class World implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Block type constants
	private static final int BLOCK_NONE = 0;
	private static final int BLOCK_BRICK = 1;
	private static final int BLOCK_STEEL_DOOR = 2;
	private static final int BLOCK_WOODEN_POLE = 3;
	private static final int BLOCK_WOOL_0 = 4;
	private static final int BLOCK_WOOL_1 = 5;
	private static final int BLOCK_STEEL = 6;
	private static final int BLOCK_BRICK_ALT = 7;
	private static final int BLOCK_WOODEN_DOOR = 8;
	private static final int BLOCK_PLANKS = 9;
	private static final int HUMAN_DEMOLIST = 10;
	private static final int HUMAN_BLOCKSELLER = 11;
	private static final int HUMAN_WEAPONCLERK = 12;
	private static final int BLOCK_LADDER = 13;
	private static final int HUMAN_GUARD = 14;
	private static final int HUMAN_KING = 15;
	
	// Layer constants
	private static final int LAYER_BEDROCK = -1;
	private static final int LAYER_STONE_1 = 0;
	private static final int LAYER_STONE_2 = 1;
	private static final int LAYER_DIRT_1 = 2;
	private static final int LAYER_DIRT_2 = 3;
	private static final int LAYER_GRASS = 4;

	// Game objects
	public ArrayList<MovingObject> blocks = new ArrayList<>();
	private ArrayList<Obtainables> obtainables = new ArrayList<>();
    public ArrayList<Human> humans = new ArrayList<>();
    public transient DemolitionWars game;

    /**
     * Generate the world and its content
     * 
     * @param game Reference to the main game class
     */
    public World(DemolitionWars game) {
        this.game = game;
        
        // Create the player and add it to the world
        createPlayer();
        
        // Generate terrain layers and structures
        generateLayers();
        generateWorld();
    }
    
    /**
     * Creates the player character and positions it in the world
     */
    private void createPlayer() {
        humans.add(new Player(game, false));
        game.addGameObject(humans.get(0), 2974, 2848);
        game.setPlayer(humans.get(0));
    }

    /**
     * Generate both sides of the world with mirrored structures
     */
    private void generateWorld() {
        // World layout as a 2D grid
        // Each number corresponds to a block type defined by the constants
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
                {0,1,13,0,0,0,0,1,0,0,0,4,5,4,5,4,0,0,4,5,4,5,4,0,0,4,5,4,5,4,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,9,0,0,0,0},
                {0,1,13,0,0,0,8,1,8,0,0,3,10,3,0,3,0,0,3,11,3,0,3,0,0,3,12,3,0,3,0,0,0,0,2,6,6,6,6,2,0,14,14,0,2,7,7,7,7,2,0,14,14,0,2,9,9,9,9,2,0,0,14,14,0,8,9,8,0,0,14,0,8,9,8,0,0,0},
                {0,1,13,0,0,0,0,1,0,0,0,3,0,3,0,3,0,0,3,0,3,0,3,0,0,3,0,3,0,3,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,9,0,0,0,0},
        };

        placeWorldObjects(worldLayout);
    }
    
    /**
     * Place objects in the world based on the layout
     * 
     * @param worldLayout 2D array representing the world layout
     */
    private void placeWorldObjects(int[][] worldLayout) {
        // Track door IDs to link them properly
        int doorId = 0;
        
        // Generate both sides (team 0 and team 1)
        for (int team = 0; team < 2; team++) {
            int rowOffset = 0;
            
            // Place objects from bottom to top
            for (int row = worldLayout.length - 1; row >= 0; row--) {
                int reverseColumn = worldLayout[0].length - 1;
                
                // Place objects in each row
                for (int col = 0; col < worldLayout[0].length; col++) {
                    // Skip empty spaces
                    if (worldLayout[row][col] != BLOCK_NONE) {
                        // Calculate mirrored position for team 1
                        int mirrorCol = reverseColumn;
                        if (team == 0) {
                            mirrorCol = col;
                        }
                        
                        // Create the appropriate object
                        MovingObject newObject;
                        if (isHumanType(worldLayout[row][col])) {
                            newObject = createObject(worldLayout[row][col], (team != 0), 
                                        (mirrorCol * (game.tileSize * 3)) + (((game.tileSize * 3) * worldLayout[0].length) * team));
                        } else {
                            newObject = createObject(worldLayout[row][col], (team != 0), doorId);
                            
                            // Increment door ID if this is a door
                            if (newObject instanceof BlockDoor) {
                                doorId++;
                            }
                        }
                        
                        blocks.add(newObject);
                        
                        // Calculate position
                        int xPos = (mirrorCol * (game.tileSize * 3)) + (((game.tileSize * 3) * worldLayout[0].length) * team);
                        int yPos = game.getMapHeight() - ((5 + rowOffset) * (game.tileSize * 3));
                        
                        // Add to game with appropriate parameters
                        if (newObject instanceof Human) {
                            game.addGameObject(newObject, xPos, yPos, 1);
                        } else {
                            game.addGameObject(newObject, xPos, yPos);
                        }
                    }
                    reverseColumn--;
                }
                rowOffset++;
            }
            doorId++; // Doors must take mirror values too
        }
    }
    
    /**
     * Check if a block type is a human character
     * 
     * @param blockTypeId The block type ID to check
     * @return True if this is a human character type
     */
    private boolean isHumanType(int blockTypeId) {
        return blockTypeId == HUMAN_DEMOLIST || 
               blockTypeId == HUMAN_BLOCKSELLER || 
               blockTypeId == HUMAN_WEAPONCLERK || 
               blockTypeId == HUMAN_GUARD || 
               blockTypeId == HUMAN_KING;
    }

    /**
     * Create an object based on its type ID and metadata
     * 
     * @param blockTypeId The block type ID
     * @param metadata Boolean metadata (typically indicates team/side)
     * @param moreMetadata Additional metadata (typically used for door IDs)
     * @return The created MovingObject
     */
    public MovingObject createObject(int blockTypeId, boolean metadata, int moreMetadata) {
        switch (blockTypeId) {
            case BLOCK_BRICK:
                return new BlockBrick(game);
            case BLOCK_STEEL_DOOR:
                return new BlockSteelDoor(game, metadata, moreMetadata);
            case BLOCK_WOODEN_POLE:
                return new BlockWoodenPole(game);
            case BLOCK_WOOL_0:
                return new BlockWool(game, 0);
            case BLOCK_WOOL_1:
                return new BlockWool(game, 1);
            case BLOCK_STEEL:
                return new BlockSteel(game);
            case BLOCK_BRICK_ALT:
                return new BlockBrick(game);
            case BLOCK_WOODEN_DOOR:
                return new BlockWoodenDoor(game, metadata, moreMetadata);
            case BLOCK_PLANKS:
                return new BlockPlanks(game);
            case HUMAN_DEMOLIST:
                return new Demolist(game, metadata, moreMetadata);
            case HUMAN_BLOCKSELLER:
                return new Blockseller(game, metadata, moreMetadata);
            case HUMAN_WEAPONCLERK:
                return new Weaponclerk(game, metadata, moreMetadata);
            case BLOCK_LADDER:
                return new BlockLadder(game, metadata);
            case HUMAN_GUARD:
                return new Guard(game, metadata, moreMetadata);
            case HUMAN_KING:
                return new King(game, metadata, moreMetadata);
            default:
                return null;
        }
    }

    /**
     * Generate terrain layers across the entire map width
     */
    public void generateLayers() {
        for (int x = 0; x < game.getMapWidth(); x += game.tileSize * 3) {
            createLayer(new BlockUnbreakableRock(game), LAYER_BEDROCK, x);
            createLayer(new BlockStone(game), LAYER_STONE_1, x);
            createLayer(new BlockStone(game), LAYER_STONE_2, x);
            createLayer(new BlockDirt(game), LAYER_DIRT_1, x);
            createLayer(new BlockDirt(game), LAYER_DIRT_2, x);
            createLayer(new BlockGrass(game), LAYER_GRASS, x);
        }
    }

    /**
     * Create a single terrain layer block
     * 
     * @param layerType The block type to create
     * @param layer The layer number (affects vertical position)
     * @param x The x-coordinate
     */
    public void createLayer(BlockLandscape layerType, int layer, int x) {
        blocks.add(layerType);
        game.addGameObject(blocks.get(blocks.size() - 1), x, game.getMapHeight() - (layer * (game.tileSize * 3)));
    }

    /**
     * Check if a given integer is even
     * 
     * @param number The number to check
     * @return True if the number is even, false otherwise
     */
    public boolean isEven(int number) {
        return (number & 1) == 0;
    }

    /**
     * Optimize collision detection for all human characters
     * Only processes humans within a certain distance of the player
     */
    public void optimizeCollisionsForAllHumans() {
        // Optimize player collisions first (most important)
        if (!humans.isEmpty()) {
            humans.get(0).optimizeCollisions();
        }
        
        // Optimize collisions for other humans but only those close to the player
        if (!humans.isEmpty()) {
            Human player = humans.get(0);
            int playerX = player.getX();
            int playerY = player.getY();
            int visibilityDistance = 50 * game.tileSize; // Only process objects within this distance
            
            for (MovingObject object : blocks) {
                if (object instanceof Human) {
                    Human human = (Human) object;
                    
                    // Only optimize collisions for humans close to the player
                    int humanX = human.getX();
                    int humanY = human.getY();
                    
                    // Simple bounding box distance check
                    if (Math.abs(humanX - playerX) < visibilityDistance && 
                        Math.abs(humanY - playerY) < visibilityDistance) {
                        human.optimizeCollisions();
                    }
                }
            }
        }
    }
}
