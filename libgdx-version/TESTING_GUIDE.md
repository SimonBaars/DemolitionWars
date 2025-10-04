# LibGDX Version - Testing Guide

## Quick Verification Checklist

### 1. Build and Run
```bash
cd libgdx-version
./gradlew android:assembleDebug  # For Android
# OR
./gradlew desktop:run            # For desktop testing (if desktop module exists)
```

### 2. Visual Checks (First 30 seconds)

#### ✓ Player Spawn
- [ ] Player spawns on solid ground (not floating in air)
- [ ] Player is positioned at approximately x=2974, y=2848
- [ ] Ground is visible below player
- [ ] Player sprite is visible and correctly sized

#### ✓ Animation
- [ ] Player sprite animates when moving (8-frame walk cycle)
- [ ] Player faces correct direction when moving left/right
- [ ] Other NPCs (guards, merchants, king) animate if visible

#### ✓ World/Map
- [ ] Terrain layers visible (grass on top, dirt below, stone further down)
- [ ] Structures visible (fortress walls, platforms)
- [ ] Map extends left and right from spawn
- [ ] Everything appears correctly scaled (not too small/large)

### 3. Control Tests (Desktop)

#### ✓ Keyboard Controls
- [ ] **A** or **Left Arrow**: Move left
- [ ] **D** or **Right Arrow**: Move right
- [ ] **W**, **Up Arrow**, or **Space**: Jump
- [ ] **F**: Place TNT (costs $200, should see explosive appear)
- [ ] **F1**: Toggle physics debug view (optional)

#### ✓ Movement Feel
- [ ] Player accelerates and decelerates smoothly
- [ ] Jump height feels appropriate
- [ ] Gravity pulls player down at reasonable speed
- [ ] Player can walk along the ground without jittering

### 4. Control Tests (Mobile/Touch)

#### ✓ Touch Zones
- [ ] **Tap left 1/3 of screen**: Move left
- [ ] **Tap right 1/3 of screen**: Move right
- [ ] **Tap top 1/3 of screen**: Jump
- [ ] **Tap bottom-right corner**: Place TNT
- [ ] **Multi-touch**: Can move and jump simultaneously

### 5. Camera Tests

#### ✓ Camera Behavior
- [ ] Camera follows player smoothly
- [ ] Player positioned approximately 1/3 from left edge of screen
- [ ] Camera shows terrain below and sky above
- [ ] Camera doesn't go past world boundaries
- [ ] When player moves, camera scrolls horizontally

### 6. UI Tests

#### ✓ Display
- [ ] Health shown in top-left: "Health: 100%"
- [ ] Money shown in top-left: "Money: $1000"
- [ ] Control instructions visible
- [ ] Text is readable (not too small)

### 7. Gameplay Tests

#### ✓ Basic Mechanics
- [ ] Player loses $200 when placing TNT
- [ ] TNT appears and falls with physics
- [ ] TNT explodes after delay (~4 seconds)
- [ ] Player can destroy blocks (if TNT works)
- [ ] Money increases when blocks destroyed

#### ✓ NPCs (if visible)
- [ ] Guards patrol back and forth
- [ ] Guards animate while moving
- [ ] Merchants stand still and animate
- [ ] King is visible in fortress (far right of map)

### 8. Performance Tests

#### ✓ Frame Rate
- [ ] Game runs smoothly (target 60 FPS)
- [ ] No stuttering during movement
- [ ] No lag when placing TNT
- [ ] Smooth scrolling when camera moves

## Common Issues and Solutions

### Issue: Player falls through ground
**Cause:** Terrain not generated at correct height  
**Check:** Verify grass level is at y=2800  
**Fix:** See `GameWorld.java` terrain generation

### Issue: Everything looks tiny
**Cause:** TILE_SIZE is 64 instead of 192  
**Check:** `GameWorld.java` TILE_SIZE constant  
**Fix:** Should be 192f, not 64f

### Issue: Player shows as static image
**Cause:** Sprite sheet not loading/splitting correctly  
**Check:** `Player.java` loadSprite() method  
**Fix:** Verify sprite sheet split into 8 frames

### Issue: No structures visible
**Cause:** World layout not generating  
**Check:** `GameWorld.java` generateWorld() method  
**Fix:** Verify worldLayout array is being processed

### Issue: Touch controls not working
**Cause:** Touch input not registered  
**Check:** `GameScreen.java` handleTouchInput() method  
**Fix:** Verify Gdx.input.isTouched() is being called

### Issue: Camera feels wrong
**Cause:** Player centered instead of at 1/3  
**Check:** `GameScreen.java` camera position calculation  
**Fix:** Should offset by -viewportWidth/3 and -viewportHeight/3

## Debug Mode

Press **F1** during gameplay to toggle Box2D physics debug renderer:
- Shows collision shapes in green/red
- Helps verify terrain and entity positions
- Useful for diagnosing physics issues

## Expected Values

### Player Stats (Initial)
- Health: 100%
- Money: $1000
- Position: (~2974, ~2848)
- Velocity: (0, 0) when stationary

### World Layout
- Total width: 30000 units
- Total height: 4000 units  
- Grass level: 2800 units
- Two fortress structures (one on each side)
- Guards patrol around coordinates x=24000-26000
- King position: approximately x=26000

### Physics Values
- Gravity: -50 units/s²
- Player move force: 800
- Jump impulse: 1200
- Max velocity: 400 units/s

## Advanced Testing

### Test Scenario 1: Basic Movement
1. Start game
2. Move left 100 units
3. Jump
4. Move right 200 units
5. Verify smooth camera follow

### Test Scenario 2: TNT Placement
1. Start game  
2. Press F to place TNT
3. Verify money decreases to $800
4. Wait 4 seconds
5. Verify TNT explodes

### Test Scenario 3: World Exploration
1. Start game
2. Move right for 30 seconds
3. Verify structures appear
4. Verify guards are visible
5. Continue to fortress
6. Verify king is visible

### Test Scenario 4: Multi-touch (Mobile)
1. Touch left side (move left)
2. While holding, touch top (jump)
3. Verify player moves left and jumps
4. Release and repeat right side
5. Verify multi-touch works

## Success Criteria

The libGDX version is working correctly if:
- ✅ Player spawns on solid ground
- ✅ Character animations work
- ✅ World scale matches original  
- ✅ Both keyboard and touch controls work
- ✅ Camera follows player properly
- ✅ Gameplay mechanics function (TNT, blocks, etc.)
- ✅ Performance is smooth (60 FPS)

If all checks pass, the port is successful! 🎉
