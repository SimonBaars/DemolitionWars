package com.demolitionwars.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Explosive entity (TNT, grenades, bombs, etc.)
 * 
 * Ported from original weapons:
 * - TNT
 * - Firebomb
 * - Grenade
 * - Napalm
 * - Nuke
 * - Scatterbomb
 * - Supernova
 * 
 * Each has different explosion radius, damage, and cost.
 */
public class Explosive extends GameEntity {
    
    public enum ExplosiveType {
        TNT(100, 50, 200),
        FIREBOMB(150, 75, 300),
        GRENADE(80, 40, 150),
        NAPALM(200, 100, 500),
        NUKE(500, 250, 2000),
        SCATTERBOMB(120, 60, 350),
        SUPERNOVA(1000, 500, 5000);
        
        public final int explosionRadius;
        public final int damage;
        public final int cost;
        
        ExplosiveType(int explosionRadius, int damage, int cost) {
            this.explosionRadius = explosionRadius;
            this.damage = damage;
            this.cost = cost;
        }
    }
    
    private ExplosiveType type;
    private float fuseTime;
    private float elapsedTime = 0;
    private boolean exploded = false;
    
    /**
     * Create explosive at given position with initial velocity
     */
    public Explosive(World world, ExplosiveType type, float x, float y, float velocityX, float velocityY) {
        this.type = type;
        this.fuseTime = calculateFuseTime(type);
        
        createBody(world, x, y);
        body.setLinearVelocity(velocityX, velocityY);
        
        // TODO: Load appropriate sprite
    }
    
    private void createBody(World world, float x, float y) {
        // Create dynamic body for explosive
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        
        body = world.createBody(bodyDef);
        
        // Create circular collision shape
        CircleShape shape = new CircleShape();
        shape.setRadius(16f); // Smaller than player
        
        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f; // Some bounce
        
        body.createFixture(fixtureDef);
        body.setUserData(this);
        
        shape.dispose();
    }
    
    @Override
    public void update(float delta) {
        if (exploded) {
            return;
        }
        
        elapsedTime += delta;
        
        if (elapsedTime >= fuseTime) {
            explode();
        }
    }
    
    /**
     * Trigger explosion
     */
    private void explode() {
        exploded = true;
        destroy();
        
        // TODO: Implement explosion logic
        // - Query all bodies in explosion radius
        // - Apply damage to blocks and entities
        // - Apply impulse to physics bodies
        // - Create particle effects
        // - Play explosion sound
    }
    
    /**
     * Apply explosion damage to entities in radius
     * Called by game screen after explosion
     */
    public void applyExplosionDamage(World world) {
        Vector2 explosionPos = body.getPosition();
        float radius = type.explosionRadius;
        
        // TODO: Query all bodies in radius
        // Calculate damage based on distance
        // Apply impulse to dynamic bodies
        // Damage blocks and entities
    }
    
    /**
     * Calculate fuse time based on explosive type
     */
    private float calculateFuseTime(ExplosiveType type) {
        switch (type) {
            case GRENADE: return 3.0f;
            case TNT: return 4.0f;
            case FIREBOMB: return 2.5f;
            case NAPALM: return 3.5f;
            case NUKE: return 5.0f;
            case SCATTERBOMB: return 2.0f;
            case SUPERNOVA: return 6.0f;
            default: return 3.0f;
        }
    }
    
    // Getters
    public ExplosiveType getType() { return type; }
    public boolean hasExploded() { return exploded; }
    public float getExplosionRadius() { return type.explosionRadius; }
    public int getDamage() { return type.damage; }
}
