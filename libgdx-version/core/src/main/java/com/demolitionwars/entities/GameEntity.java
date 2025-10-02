package com.demolitionwars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Base class for all game entities.
 * 
 * Provides common functionality for entities with:
 * - Box2D physics body
 * - Sprite rendering
 * - Update/render lifecycle
 */
public abstract class GameEntity {
    
    protected Body body;
    protected Sprite sprite;
    protected boolean active = true;
    
    /**
     * Update entity state
     * @param delta Time since last frame in seconds
     */
    public abstract void update(float delta);
    
    /**
     * Render entity sprite
     * @param batch SpriteBatch to draw with
     */
    public void render(SpriteBatch batch) {
        if (sprite != null && active) {
            // Sync sprite position with physics body
            if (body != null) {
                sprite.setPosition(
                    body.getPosition().x - sprite.getWidth() / 2,
                    body.getPosition().y - sprite.getHeight() / 2
                );
                sprite.setRotation((float) Math.toDegrees(body.getAngle()));
            }
            sprite.draw(batch);
        }
    }
    
    /**
     * Check if entity is active (alive)
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Mark entity as inactive (destroyed)
     */
    public void destroy() {
        active = false;
    }
    
    /**
     * Get physics body
     */
    public Body getBody() {
        return body;
    }
    
    /**
     * Get sprite
     */
    public Sprite getSprite() {
        return sprite;
    }
}
