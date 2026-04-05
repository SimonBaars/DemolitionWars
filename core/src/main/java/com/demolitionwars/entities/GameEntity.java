package com.demolitionwars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Base class for all game entities.
 * 
 * Provides common functionality for entities with:
 * - Box2D physics body
 * - Sprite rendering (static or animated)
 * - Update/render lifecycle
 */
public abstract class GameEntity {
    
    protected Body body;
    protected Sprite sprite;
    protected Animation<TextureRegion> animation;
    protected float animationTime = 0f;
    protected boolean active = true;
    protected boolean facingRight = true;
    
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
        if (!active) return;
        
        if (animation != null && body != null) {
            // Render animated sprite
            TextureRegion currentFrame = animation.getKeyFrame(animationTime, true);
            float x = body.getPosition().x - currentFrame.getRegionWidth() / 2;
            float y = body.getPosition().y - currentFrame.getRegionHeight() / 2;
            float width = currentFrame.getRegionWidth();
            float height = currentFrame.getRegionHeight();
            
            // Draw with proper scaling based on facing direction
            if (!facingRight) {
                // Flip horizontally by drawing with negative width
                batch.draw(currentFrame, x + width, y, -width, height);
            } else {
                batch.draw(currentFrame, x, y, width, height);
            }
        } else if (sprite != null) {
            // Render static sprite
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
