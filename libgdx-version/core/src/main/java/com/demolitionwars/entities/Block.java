package com.demolitionwars.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Block/terrain entity.
 * 
 * Represents destructible terrain blocks from the original game.
 * Block types: brick, dirt, grass, planks, steel, stone, wool, etc.
 * 
 * TODO:
 * - Load appropriate sprite based on block type
 * - Implement destruction mechanics
 * - Different health values per block type
 * - Drop money/items on destruction
 */
public class Block extends GameEntity {
    
    public enum BlockType {
        BRICK(50),
        DIRT(20),
        GRASS(15),
        PLANKS(30),
        STEEL(100),
        STONE(70),
        UNBREAKABLE(-1),
        WOOL_RED(10),
        WOOL_WHITE(10),
        WOOL_BLACK(10),
        WOOL_BLUE(10);
        
        public final int health;
        
        BlockType(int health) {
            this.health = health;
        }
    }
    
    private static final float BLOCK_SIZE = 64f;
    
    private BlockType type;
    private int health;
    private int moneyValue;
    
    /**
     * Create block at given position
     */
    public Block(World world, float x, float y, BlockType type) {
        this.type = type;
        this.health = type.health;
        this.moneyValue = calculateMoneyValue(type);
        
        createBody(world, x, y);
        // TODO: Load sprite based on type
    }
    
    private void createBody(World world, float x, float y) {
        // Create static body for block
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        
        body = world.createBody(bodyDef);
        
        // Create box shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BLOCK_SIZE / 2, BLOCK_SIZE / 2);
        
        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        
        body.createFixture(fixtureDef);
        body.setUserData(this);
        
        shape.dispose();
    }
    
    @Override
    public void update(float delta) {
        // Blocks don't need regular updates unless they're falling/breaking
    }
    
    /**
     * Damage block
     * @param damage Amount of damage
     * @return Money dropped if block is destroyed, 0 otherwise
     */
    public int takeDamage(int damage) {
        if (type == BlockType.UNBREAKABLE) {
            return 0;
        }
        
        health -= damage;
        
        if (health <= 0) {
            destroy();
            return moneyValue;
        }
        
        return 0;
    }
    
    /**
     * Calculate money value based on block type
     */
    private int calculateMoneyValue(BlockType type) {
        switch (type) {
            case BRICK: return 5;
            case DIRT: return 2;
            case GRASS: return 1;
            case PLANKS: return 3;
            case STEEL: return 10;
            case STONE: return 7;
            case WOOL_RED:
            case WOOL_WHITE:
            case WOOL_BLACK:
            case WOOL_BLUE:
                return 1;
            default:
                return 0;
        }
    }
    
    // Getters
    public BlockType getType() { return type; }
    public int getHealth() { return health; }
    public boolean isDestructible() { return type != BlockType.UNBREAKABLE; }
}
