package com.demolitionwars.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Guard enemy that patrols an area and attacks the player.
 */
public class Guard extends GameEntity {
    
    private static final float GUARD_RADIUS = 32f;
    private static final float PATROL_SPEED = 100f;
    private static final float PATROL_DISTANCE = 300f;
    
    private int health = 100;
    private boolean team;
    private float homeX;
    private float patrolLeft;
    private float patrolRight;
    private boolean movingRight = true;
    
    public Guard(World world, float x, float y, boolean team) {
        this.team = team;
        this.homeX = x;
        this.patrolLeft = x - PATROL_DISTANCE;
        this.patrolRight = x + PATROL_DISTANCE;
        
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
        shape.setRadius(GUARD_RADIUS);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        
        body.createFixture(fixtureDef);
        body.setUserData(this);
        
        shape.dispose();
    }
    
    private void loadSprite() {
        try {
            String spriteName = team ? "guard_blauw" : "guard_zwart";
            Texture texture = new Texture(Gdx.files.internal("sprites/" + spriteName + ".png"));
            sprite = new Sprite(texture);
            sprite.setSize(GUARD_RADIUS * 2, GUARD_RADIUS * 2);
        } catch (Exception e) {
            Gdx.app.log("Guard", "Could not load sprite: " + e.getMessage());
        }
    }
    
    @Override
    public void update(float delta) {
        if (!active) return;
        
        Vector2 pos = body.getPosition();
        
        // Patrol logic
        if (movingRight) {
            body.setLinearVelocity(PATROL_SPEED, body.getLinearVelocity().y);
            if (pos.x > patrolRight) {
                movingRight = false;
            }
        } else {
            body.setLinearVelocity(-PATROL_SPEED, body.getLinearVelocity().y);
            if (pos.x < patrolLeft) {
                movingRight = true;
            }
        }
        
        // Update sprite position
        if (sprite != null) {
            sprite.setPosition(pos.x - GUARD_RADIUS, pos.y - GUARD_RADIUS);
        }
    }
    
    @Override
    public void render(SpriteBatch batch) {
        if (sprite != null && active) {
            sprite.draw(batch);
        }
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
