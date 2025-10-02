package com.demolitionwars.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Player entity with Box2D physics.
 * 
 * TODO: Port from original Player.java:
 * - Movement controls (left/right)
 * - Jumping
 * - Inventory system
 * - Health system
 * - Shop interaction
 * - Weapon usage
 */
public class Player extends GameEntity {
    
    private static final float PLAYER_RADIUS = 32f; // Half of 64px tile
    private static final float MOVE_FORCE = 500f;
    private static final float JUMP_FORCE = 800f;
    private static final float MAX_VELOCITY = 300f;
    
    private int health = 100;
    private int money = 1000; // Starting money
    private boolean canJump = false;
    
    /**
     * Create player at given position
     */
    public Player(World world, float x, float y) {
        createBody(world, x, y);
        // TODO: Load player sprite texture
        // sprite = new Sprite(new Texture("sprites/speler_zwart.png"));
    }
    
    private void createBody(World world, float x, float y) {
        // Create dynamic body for player
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true; // Player shouldn't rotate
        
        body = world.createBody(bodyDef);
        
        // Create circular collision shape
        CircleShape shape = new CircleShape();
        shape.setRadius(PLAYER_RADIUS);
        
        // Create fixture with physics properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.0f; // No bouncing
        
        body.createFixture(fixtureDef);
        body.setUserData(this);
        
        shape.dispose();
    }
    
    @Override
    public void update(float delta) {
        // Limit horizontal velocity
        Vector2 vel = body.getLinearVelocity();
        if (Math.abs(vel.x) > MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * MAX_VELOCITY;
            body.setLinearVelocity(vel);
        }
    }
    
    /**
     * Move player left
     */
    public void moveLeft() {
        body.applyForceToCenter(-MOVE_FORCE, 0, true);
    }
    
    /**
     * Move player right
     */
    public void moveRight() {
        body.applyForceToCenter(MOVE_FORCE, 0, true);
    }
    
    /**
     * Make player jump
     */
    public void jump() {
        if (canJump) {
            body.applyLinearImpulse(0, JUMP_FORCE, body.getWorldCenter().x, body.getWorldCenter().y, true);
            canJump = false;
        }
    }
    
    /**
     * Set whether player can jump (called by collision detection)
     */
    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }
    
    /**
     * Take damage
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            destroy();
        }
    }
    
    /**
     * Add/remove money
     */
    public void addMoney(int amount) {
        money += amount;
    }
    
    /**
     * Check if player can afford something
     */
    public boolean canAfford(int cost) {
        return money >= cost;
    }
    
    /**
     * Spend money
     */
    public boolean spendMoney(int cost) {
        if (canAfford(cost)) {
            money -= cost;
            return true;
        }
        return false;
    }
    
    // Getters
    public int getHealth() { return health; }
    public int getMoney() { return money; }
}
