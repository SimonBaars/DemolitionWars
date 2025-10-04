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
 * Merchant NPC that sells items to the player.
 */
public class Merchant extends GameEntity {
    
    private static final float ANIMATION_FRAME_DURATION = 0.15f;
    
    public enum MerchantType {
        WEAPONS,
        EXPLOSIVES,
        BLOCKS
    }
    
    private static final float MERCHANT_RADIUS = 32f;
    
    private boolean team;
    private MerchantType type;
    
    public Merchant(World world, float x, float y, boolean team, MerchantType type) {
        this.team = team;
        this.type = type;
        createBody(world, x, y);
        loadSprite();
    }
    
    private void createBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Merchants don't move
        bodyDef.position.set(x, y);
        
        body = world.createBody(bodyDef);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(MERCHANT_RADIUS);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true; // Merchants are sensors for interaction
        
        body.createFixture(fixtureDef);
        body.setUserData(this);
        
        shape.dispose();
    }
    
    private void loadSprite() {
        try {
            // Load sprite sheet (512x128 with 8 frames of 64x64 each)
            String spriteName = team ? "merchant_blauw" : "merchant_zwart";
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
            Gdx.app.log("Merchant", "Could not load sprite: " + e.getMessage());
        }
    }
    
    @Override
    public void update(float delta) {
        // Update animation time
        animationTime += delta;
        // Merchants don't move
    }
    
    public MerchantType getType() {
        return type;
    }
}
