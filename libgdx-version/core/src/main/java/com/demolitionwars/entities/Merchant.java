package com.demolitionwars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Merchant NPC that sells items to the player.
 */
public class Merchant extends GameEntity {
    
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
            String spriteName = team ? "merchant_blauw" : "merchant_zwart";
            Texture texture = new Texture(Gdx.files.internal("sprites/" + spriteName + ".png"));
            sprite = new Sprite(texture);
            sprite.setSize(MERCHANT_RADIUS * 2, MERCHANT_RADIUS * 2);
        } catch (Exception e) {
            Gdx.app.log("Merchant", "Could not load sprite: " + e.getMessage());
        }
    }
    
    @Override
    public void update(float delta) {
        // Merchants don't move
        if (sprite != null && body != null) {
            Vector2 pos = body.getPosition();
            sprite.setPosition(pos.x - MERCHANT_RADIUS, pos.y - MERCHANT_RADIUS);
        }
    }
    
    @Override
    public void render(SpriteBatch batch) {
        if (sprite != null && active) {
            sprite.draw(batch);
        }
    }
    
    public MerchantType getType() {
        return type;
    }
}
