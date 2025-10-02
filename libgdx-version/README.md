# DemolitionWars - libGDX Version

This is a planned port of DemolitionWars to the libGDX game engine.

## Why libGDX?

libGDX provides:
- Proper 2D physics via Box2D integration
- Cross-platform support (Android, Desktop, Web)
- Better performance with optimized rendering
- Active community and documentation
- Built-in sprite batch rendering and asset management

## Project Structure

```
libgdx-version/
├── core/          # Platform-independent game logic
├── android/       # Android-specific launcher
└── desktop/       # Desktop launcher (for testing)
```

## Current Status

⚠️ **This is a framework starter** ⚠️

The full port is a substantial undertaking that requires:
1. Setting up complete libGDX project with Gradle
2. Porting all game entities and logic
3. Implementing Box2D physics for proper collision detection
4. Setting up sprite rendering and animations
5. Implementing the game loop and state management
6. Porting UI elements and menus
7. Asset loading and management

## To Complete This Port

1. Use libGDX setup tool to generate complete project structure:
   ```bash
   # Download from https://libgdx.com/dev/project-generation/
   ```

2. Copy existing sprite assets from `../app/src/main/res/drawable-mdpi/`

3. Implement core game classes:
   - GameScreen (main game logic)
   - Player entity with physics
   - Block/terrain system with Box2D
   - Weapons and explosions
   - AI for enemies

4. Set up Box2D world with proper physics parameters

## Original Game Mechanics to Port

- Grid-based world (30000x4000)
- Player movement and controls
- Block destruction system
- Weapon system (TNT, grenades, etc.)
- Shop/merchant system
- King victory condition
- Money/economy system

## Benefits of libGDX Port

- Proper physics engine (Box2D) vs custom collision detection
- Better performance
- Easier to maintain and extend
- Desktop version for faster testing
- Potential for web deployment via GWT
