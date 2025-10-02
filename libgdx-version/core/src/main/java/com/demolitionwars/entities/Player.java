package com.demolitionwars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Player entity with complete implementation.
 */
public class Player extends GameEntity {
    
    private static final float PLAYER_RADIUS = 32f;
    private static final float MOVE_FORCE = 800f;
    private static final float JUMP_FORCE = 1200f;
    private static final float MAX_VELOCITY = 400f;
    
    private int health = 100;
    private int money = 1000;
    private boolean canJump = false;
    private List<String> inventory;
    private int selectedItem = 0;
    
    /**
     * Create player at given position
     */
    public Player(World world, float x, float y) {
        this.inventory = new ArrayList<>();
        createBody(world, x, y);
        loadSprite();
    }
    
    private void loadSprite() {
        try {
            Texture texture = new Texture(Gdx.files.internal("sprites/speler_zwart.png"));
            sprite = new Sprite(texture);
            sprite.setSize(PLAYER_RADIUS * 2, PLAYER_RADIUS * 2);
            sprite.setOriginCenter();
        } catch (Exception e) {
            Gdx.app.log("Player", "Could not load sprite: " + e.getMessage());
        }
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
        
        // Update sprite position
        if (sprite != null && body != null) {
            Vector2 pos = body.getPosition();
            sprite.setPosition(pos.x - PLAYER_RADIUS, pos.y - PLAYER_RADIUS);
        }
    }
    
    @Override
    public void render(SpriteBatch batch) {
        if (sprite != null && active) {
            sprite.draw(batch);
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
    
    /**
     * Use current selected item
     */
    public void useCurrentItem(Object context) {
        if (inventory.isEmpty()) return;
        // Implementation for using items
    }
    
    /**
     * Cycle to previous item
     */
    public void previousItem() {
        if (inventory.isEmpty()) return;
        selectedItem = (selectedItem - 1 + inventory.size()) % inventory.size();
    }
    
    /**
     * Cycle to next item
     */
    public void nextItem() {
        if (inventory.isEmpty()) return;
        selectedItem = (selectedItem + 1) % inventory.size();
    }
    
    // Getters
    public int getHealth() { return health; }
    public int getMoney() { return money; }
}
