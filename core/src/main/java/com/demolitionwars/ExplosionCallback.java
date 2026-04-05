package com.demolitionwars;

import com.badlogic.gdx.math.Vector2;
import com.demolitionwars.entities.Explosive;

/**
 * Notifies the game when an explosive detonates so radius damage can be applied in world space.
 */
public interface ExplosionCallback {
    void onExplosion(Explosive explosive, Vector2 centerWorld, float radiusWorld, int peakDamage);
}
