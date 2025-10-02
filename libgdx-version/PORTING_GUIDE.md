# DemolitionWars libGDX Porting Guide

This guide outlines the steps needed to complete the port of DemolitionWars from the custom Android game engine to libGDX with Box2D physics.

## Project Status

✅ **Complete:**
- Basic libGDX project structure
- Gradle build configuration for Android (API 31+)
- Main game class with screen management
- GameScreen with Box2D physics setup
- Android launcher
- GitHub Actions workflow for building APK

⚠️ **In Progress / TODO:**
- Entity system
- Physics bodies for game objects
- Sprite rendering
- Game logic port
- UI system

## Architecture Overview

### Original Game Structure
```
GameEngine (custom)
  ├── DemolitionWars (main game)
  ├── World (game world/map)
  ├── MovingObject (physics base class)
  ├── Human (character base)
  │   ├── Player
  │   ├── Bot (AI)
  │   │   ├── Merchant
  │   │   ├── Guard
  │   │   └── King
  └── Blocks (terrain)
```

### libGDX Structure (Target)
```
DemolitionWarsGame
  ├── GameScreen (main gameplay)
  ├── MenuScreen
  ├── World (Box2D world manager)
  ├── Entities
  │   ├── Player
  │   ├── NPC (merchants, guards, king)
  │   └── Projectile
  ├── Systems
  │   ├── PhysicsSystem
  │   ├── RenderSystem
  │   └── AISystem
  └── UI (HUD, shop interface)
```

## Step-by-Step Porting Guide

### Phase 1: Core Game Loop (Priority: HIGH)

1. **Create Entity Base Class**
   ```java
   public abstract class GameEntity {
       protected Body body;
       protected Sprite sprite;
       public abstract void update(float delta);
       public abstract void render(SpriteBatch batch);
   }
   ```

2. **Implement Player Entity**
   - Port player movement from original `Player.java`
   - Convert custom momentum system to Box2D forces
   - Map original controls to libGDX input
   - Implement health system

3. **Create Block/Terrain System**
   - Port block types from original game
   - Create Box2D static bodies for terrain
   - Implement destructible blocks (change to sensor on destruction)
   - Port collision detection

### Phase 2: Physics System (Priority: HIGH)

1. **Convert Momentum to Box2D**
   ```java
   // Original: addToMomentum(degrees, speed)
   // Convert to: body.applyForce(vector, point, wake)
   
   float radians = (float) Math.toRadians(degrees);
   Vector2 force = new Vector2(
       (float) Math.cos(radians) * speed,
       (float) Math.sin(radians) * speed
   );
   body.applyForce(force, body.getWorldCenter(), true);
   ```

2. **Implement Gravity**
   - Box2D world gravity handles this automatically
   - Original: custom gravity system in `MovingObject.gravity()`
   - libGDX: `World world = new World(new Vector2(0, -30f), true);`

3. **Collision Detection**
   - Original: Manual tile-based collision detection
   - libGDX: Box2D ContactListener
   ```java
   world.setContactListener(new ContactListener() {
       @Override
       public void beginContact(Contact contact) {
           // Handle collisions
       }
   });
   ```

### Phase 3: Weapons & Explosions (Priority: MEDIUM)

1. **Port Weapon System**
   - Convert from original Items: TNT, Firebomb, Grenade, etc.
   - Implement as physics bodies with explosion logic
   - Use Box2D raycasting for explosion damage

2. **Explosion Physics**
   ```java
   public void explode(Vector2 position, float radius, float force) {
       // Query all bodies in radius
       // Apply impulse based on distance
       world.QueryAABB(callback, x1, y1, x2, y2);
   }
   ```

### Phase 4: AI System (Priority: MEDIUM)

1. **Port Bot Behavior**
   - Convert from `Bot.java` walking system
   - Implement pathfinding (A* algorithm recommended)
   - Port merchant, guard, and king behaviors

2. **AI States**
   ```java
   public enum AIState {
       IDLE, PATROL, CHASE, ATTACK, FLEE
   }
   ```

### Phase 5: UI & Game State (Priority: MEDIUM)

1. **HUD Implementation**
   - Port dashboard displays (money, health, inventory)
   - Use Scene2D for UI (libGDX's UI framework)
   ```java
   Stage stage = new Stage(viewport);
   Table table = new Table();
   Label moneyLabel = new Label("Money: ", skin);
   ```

2. **Shop System**
   - Port shop interaction from original
   - Create shop UI screen
   - Implement item purchase logic

3. **Menu System**
   - Main menu
   - Pause menu
   - Game over screen

### Phase 6: Map & World (Priority: HIGH)

1. **Load Original Map Data**
   ```java
   // Original map: 30000x4000 tile grid
   // Read from World.java initialization
   // Convert to Box2D bodies
   ```

2. **Optimize Rendering**
   - Only render visible portion (viewport culling)
   - Use sprite batching
   - Consider chunk-based loading for large maps

### Phase 7: Assets & Polishing (Priority: LOW)

1. **Import Sprites**
   ```bash
   cp ../app/src/main/res/drawable-mdpi/*.png assets/sprites/
   ```

2. **Create Texture Atlas**
   - Combine sprites for better performance
   - Use libGDX TexturePacker

3. **Add Sound Effects**
   - Explosion sounds
   - Block destruction
   - Menu interactions

4. **Particle Effects**
   - Dust from explosions
   - Block debris
   - Fire and smoke

## Key Conversion Notes

### Coordinate System
- **Original**: Screen space coordinates (pixels)
- **libGDX**: World units (recommend 1 unit = 1 tile = 64 pixels)
- **Box2D**: Meters (recommend 1 meter = 64 pixels)

### Collision Optimization
- **Original**: Manual nearby object tracking
- **libGDX**: Box2D spatial hash (automatic)

### Rendering
- **Original**: Canvas drawing in GameView
- **libGDX**: SpriteBatch with viewport

## Testing Strategy

1. **Unit Tests**: Physics calculations, entity behaviors
2. **Integration Tests**: Entity interactions, collision responses
3. **Performance Tests**: Frame rate with many entities
4. **Platform Tests**: Test on multiple Android devices

## Performance Considerations

1. **Object Pooling**: Reuse projectile objects
2. **Viewport Culling**: Only render visible entities
3. **Sprite Batching**: Combine draw calls
4. **Box2D Bodies**: Destroy unused bodies promptly

## Build Commands

```bash
# Build Android APK
./gradlew android:assembleDebug

# Build Release APK
./gradlew android:assembleRelease

# Install on device
./gradlew android:installDebug
```

## Estimated Completion Time

- **Phase 1-2** (Core + Physics): 40-60 hours
- **Phase 3** (Weapons): 20-30 hours  
- **Phase 4** (AI): 20-30 hours
- **Phase 5** (UI): 15-25 hours
- **Phase 6** (Map): 15-20 hours
- **Phase 7** (Polish): 10-15 hours

**Total**: 120-180 hours for complete port

## Benefits of libGDX Port

✅ Proper physics engine (Box2D)
✅ Better performance
✅ Cross-platform (Android, Desktop, HTML5)
✅ Active community & extensive documentation
✅ Better tools (scene editor, particle editor)
✅ Easier to maintain and extend

## Next Steps

1. Copy sprite assets to `assets/sprites/`
2. Implement Player entity with Box2D body
3. Port map loading and create terrain bodies
4. Test basic movement and collision
5. Incrementally port remaining features
