package com.demolitionwars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * The King - defeating him wins the game!
 */
public class King extends GameEntity {
    
    private static final float KING_RADIUS = 32f;
    private static final float ANIMATION_FRAME_DURATION = 0.15f;
    
    private int health = 500; // King has lots of health
    private boolean team;
    
    public King(World world, float x, float y, boolean team) {
        this.team = team;
        createBody(world, x, y);
        loadSprite();
    }
    
    private void createBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        
        body = world.createBody(bodyDef);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(KING_RADIUS);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.0f; // King is heavier
        fixtureDef.friction = 0.5f;
        
        body.createFixture(fixtureDef);
        body.setUserData(this);
        
        shape.dispose();
    }
    
    private void loadSprite() {
        try {
            // Load sprite sheet (512x128 with 8 frames of 64x64 each)
            String spriteName = team ? "king_blauw" : "king_zwart";
            Texture texture = new Texture(Gdx.files.internal("sprites/" + spriteName + ".png"));
            TextureRegion[][] tmp = TextureRegion.split(texture, 64, 128);
            
            // Extract frames (8 frames in first row)
            TextureRegion[] frames = new TextureRegion[8];
            for (int i = 0; i < 8; i++) {
                frames[i] = tmp[0][i];
            }
            
            // Create animation
            animation = new Animation<>(ANIMATION_FRAME_DURATION, frames);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            
        } catch (Exception e) {
            Gdx.app.log("King", "Could not load sprite: " + e.getMessage());
        }
    }
    
    @Override
    public void update(float delta) {
        if (!active) return;
        
        // Update animation time
        animationTime += delta;
        
        // King doesn't move much, just stays in place
    }
    
    public boolean takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            destroy();
            return true;
        }
        return false;
    }
    
    public int getHealth() {
        return health;
    }
}
